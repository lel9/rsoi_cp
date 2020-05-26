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
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DrugService {

    @Autowired
    private DrugRepository drugRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private Logger log = Logger.getLogger(DrugService.class.getName());

    public Drug getDrug(String id) {
        Optional<Drug> byId = drugRepository.findById(id);
        if (!byId.isPresent())
            throw new NoSuchDrugException();

        try {
            String routingKey = "operation";
            OperationOut operation = new OperationOut(id, "R");
            rabbitTemplate.convertAndSend("operationExchange", routingKey, operation);
            log.log(Level.INFO, "Operation was sent to RabbitMQ: " + operation);
        } catch (Exception ex) {
            log.log(Level.SEVERE, ex.getMessage());
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
            OperationOut operation = new OperationOut(id, "C");
            rabbitTemplate.convertAndSend("operationExchange", routingKey, operation);
            log.log(Level.INFO, "Operation was sent to RabbitMQ: " + operation);
        } catch (Exception ex) {
            log.log(Level.SEVERE, ex.getMessage());
        }

        return id;
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
            log.log(Level.INFO, "information about updating drug was sent to RabbitMQ: " + saved.getId());
        } catch (Exception ex) {
            log.log(Level.SEVERE, ex.getMessage());
        }

        try {
            String routingKey = "operation";
            OperationOut operation = new OperationOut(id, "U");
            rabbitTemplate.convertAndSend("operationExchange", routingKey, operation);
            log.log(Level.INFO, "Operation was sent to RabbitMQ: " + operation);
        } catch (Exception ex) {
            log.log(Level.SEVERE, ex.getMessage());
        }

    }

    public Iterable<Drug> findByIds(List<String> ids) {
        return drugRepository.findAllById(ids);
    }

    public List<Drug> searchDrugs(String tradeName, String activeSubstance, String form,
                                  String composition, String description, String group,
                                  String atx, String pharmacodynamics, String pharmacokinetics,
                                  String indications, String contraindications, String withCaution,
                                  String pregnancyAndLactation, String directionForUse, String sideEffects,
                                  String overdose, String intearction, String specialInstruction,
                                  String vehicleImpact, String releaseFormVSDosage, String transportationСonditions,
                                  String storageСonditions, String storageLife, String vacationFromPharmacies,
                                  String manufacturer, String certificateOwner) {
        return drugRepository.findByActiveSubstance(activeSubstance);
    }
}
