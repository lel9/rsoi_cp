package ru.bmstu.cp.rsoi.profile.service;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bmstu.cp.rsoi.profile.domain.Profile;
import ru.bmstu.cp.rsoi.profile.exception.NoSuchProfileException;
import ru.bmstu.cp.rsoi.profile.repository.ProfileRepository;

import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Exchange exchange;

    public Profile getProfile(String id) {
        Optional<Profile> byId = profileRepository.findById(id);
        if (!byId.isPresent())
            throw new NoSuchProfileException();

        return byId.get();
    }

    public void putProfile(String id, Profile profile) {
        profile.setId(id);
        Profile saved = profileRepository.save(profile);

        try {
            String routingKey = "profile.updated";
            rabbitTemplate.convertAndSend(exchange.getName(), routingKey, saved);
        } catch (Exception e) {
            e.printStackTrace(); // todo логгирование
        }

    }
}
