package ru.bmstu.cp.rsoi.drug.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.bmstu.cp.rsoi.drug.domain.Drug;
import ru.bmstu.cp.rsoi.drug.exception.NoSuchDrugException;
import ru.bmstu.cp.rsoi.drug.model.OperationOut;
import ru.bmstu.cp.rsoi.drug.repository.DrugRepository;
import ru.bmstu.cp.rsoi.drug.web.utility.MyBeansUtil;

import java.util.List;
import java.util.Optional;

@Service
public class DrugService {

    @Autowired
    private DrugRepository drugRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Drug getDrug(String id) {
        Optional<Drug> byId = drugRepository.findById(id);
        if (!byId.isPresent())
            throw new NoSuchDrugException();

        try {
            String routingKey = "operation";
            rabbitTemplate.convertAndSend("operationExchange", routingKey, new OperationOut(id, "R"));
        } catch (Exception ex) {
            // todo логгирование
        }

        return byId.get();
    }

    public Page<Drug> findDrugs(String text, int page, int size) {
        if (text.isEmpty())
            return drugRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "tradeName")));
        return drugRepository.findByTradeNameStartsWith(text, PageRequest.of(page, size)); // TODO полнотекстовый поиск
    }

    public String postDrug(Drug drug) {
        drug.setId(null);
        Drug save = drugRepository.save(drug);
        String id = save.getId();

        try {
            String routingKey = "operation";
            rabbitTemplate.convertAndSend("operationExchange", routingKey, new OperationOut(id, "C"));
        } catch (Exception ex) {
            // todo логгирование
        }

        return id;
    }

    public List<Drug> getDrugAnalogs(String id) {
        Optional<Drug> byId = drugRepository.findById(id);
        if (!byId.isPresent())
            throw new NoSuchDrugException();

        String activeSubstance = byId.get().getActiveSubstance();
        List<Drug> byActiveSubstance = drugRepository.findByActiveSubstance(activeSubstance);
        byActiveSubstance.removeIf(drug -> drug.getId().equals(id));
        return byActiveSubstance;
    }

    public void patchDrug(Drug drug, String id) {
        Optional<Drug> byId = drugRepository.findById(id);
        if (!byId.isPresent())
            throw new NoSuchDrugException();

        Drug target = byId.get();
        MyBeansUtil<Drug> util = new MyBeansUtil<>();
        util.copyNonNullProperties(target, drug);

        Drug saved = drugRepository.save(target);

        try {
            String routingKey = "drug.updated";
            rabbitTemplate.convertAndSend("eventExchange", routingKey, saved);
        } catch (Exception ex) {
            // todo логгирование
        }

        try {
            String routingKey = "operation";
            rabbitTemplate.convertAndSend("operationExchange", routingKey, new OperationOut(id, "U"));
        } catch (Exception ex) {
            // todo логгирование
        }

    }

    public Iterable<Drug> findByIds(List<String> ids) {
        return drugRepository.findAllById(ids);
    }
}
