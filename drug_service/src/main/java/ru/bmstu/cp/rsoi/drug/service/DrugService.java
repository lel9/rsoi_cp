package ru.bmstu.cp.rsoi.drug.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bmstu.cp.rsoi.drug.domain.Drug;
import ru.bmstu.cp.rsoi.drug.exception.DrugAlreadyExistsException;
import ru.bmstu.cp.rsoi.drug.exception.NoSuchDrugException;
import ru.bmstu.cp.rsoi.drug.model.DrugIn;
import ru.bmstu.cp.rsoi.drug.model.DrugOut;
import ru.bmstu.cp.rsoi.drug.model.DrugOutShort;
import ru.bmstu.cp.rsoi.drug.model.ListDrugOutShort;
import ru.bmstu.cp.rsoi.drug.repository.DrugRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DrugService {

    @Autowired
    private DrugRepository drugRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Exchange exchange;

    public DrugOut getDrug(String id) {
        Optional<Drug> byId = drugRepository.findById(id);
        if (!byId.isPresent())
            throw new NoSuchDrugException();

        Drug drug = byId.get();
        return modelMapper.map(drug, DrugOut.class);
    }

    public String postDrug(DrugIn drugIn) {
        if (drugRepository.findByTradeName(drugIn.getTradeName()).isPresent())
            throw new DrugAlreadyExistsException(drugIn.getTradeName());

        Drug drug = modelMapper.map(drugIn, Drug.class);
        drug.setId(null);
        Drug save = drugRepository.save(drug);
        return save.getId();
    }

    public void putDrug(DrugIn drugIn, String id) {
        Optional<Drug> oldDrug = drugRepository.findById(id);
        if (!oldDrug.isPresent())
            throw new NoSuchDrugException();

        Optional<Drug> byTradeName = drugRepository.findByTradeName(drugIn.getTradeName());
        if (byTradeName.isPresent() && !byTradeName.get().getId().equals(id))
            throw new DrugAlreadyExistsException(drugIn.getTradeName());

        Drug drug = modelMapper.map(drugIn, Drug.class);
        drug.setId(id);
        drugRepository.save(drug);

        String routingKey = "drug.updated";
        DrugOutShort drugOutShort = modelMapper.map(drug, DrugOutShort.class);
        rabbitTemplate.convertAndSend(exchange.getName(), routingKey, drugOutShort);
    }

    public ListDrugOutShort findDrug(String text) {
        List<Drug> drugs = drugRepository.findByTradeNameStartsWith(text);
        List<DrugOutShort> res = new ArrayList<>();

        drugs.forEach(drug -> res.add(modelMapper.map(drug, DrugOutShort.class)));

        ListDrugOutShort list = new ListDrugOutShort();
        list.setDrugs(res);

        return list;
    }
}
