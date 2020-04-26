package ru.bmstu.cp.rsoi.recommendation.web.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.bmstu.cp.rsoi.recommendation.domain.Recommendation;
import ru.bmstu.cp.rsoi.recommendation.model.ProfileIn;

public class ProfileUpdatedListener {

    @Autowired
    private MongoTemplate mongoTemplate;

    @RabbitListener(queues="profileServiceQueue")
    public void receive(ProfileIn message) {
        Query query = new Query(Criteria.where("recommendations.author").is(message.getName()));
        Update update = new Update();
        update.set("author.$.display_name", message.getDisplayName());
        mongoTemplate.updateMulti(query, update, Recommendation.class);
    }

}
