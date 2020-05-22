package ru.bmstu.cp.rsoi.patient.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.bmstu.cp.rsoi.patient.domain.Reception;
import ru.bmstu.cp.rsoi.patient.model.reception.DrugIn;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DrugUpdatedListener {

    @Autowired
    private MongoTemplate mongoTemplate;

    private Logger log = Logger.getLogger(DrugUpdatedListener.class.getName());

    @RabbitListener(queues="drugServiceQueue")
    public void receive(DrugIn message) {
        Query query = new Query(Criteria.where("drugs.id").is(message.getId()));
        Update update = new Update();
        update.set("drugs.$.trade_name", message.getTradeName());
        update.set("drugs.$.release_form_vs_dosage", message.getReleaseFormVSDosage());
        update.set("drugs.$.manufacturer", message.getManufacturer());
        mongoTemplate.updateMulti(query, update, Reception.class);
        log.log(Level.INFO, "Drug was updated: " + message.toString());
    }

}
