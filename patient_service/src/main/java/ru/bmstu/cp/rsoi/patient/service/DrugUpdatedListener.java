package ru.bmstu.cp.rsoi.patient.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.bmstu.cp.rsoi.patient.domain.Reception;
import ru.bmstu.cp.rsoi.patient.model.DrugIn;

public class DrugUpdatedListener {

    @Autowired
    private MongoTemplate mongoTemplate;

    @RabbitListener(queues="drugServiceQueue")
    public void receive(DrugIn message) {
        Query query = new Query(Criteria.where("drugs.id").is(message.getId()));
        Update update = new Update();
        update.set("drugs.$.trade_name", message.getTradeName());
        mongoTemplate.updateMulti(query, update, Reception.class);
    }

}
