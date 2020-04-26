package ru.bmstu.cp.rsoi.profile.service;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.bmstu.cp.rsoi.profile.domain.Profile;
import ru.bmstu.cp.rsoi.profile.exception.NoSuchProfileException;
import ru.bmstu.cp.rsoi.profile.model.ProfileIn;
import ru.bmstu.cp.rsoi.profile.model.ProfileOutShort;
import ru.bmstu.cp.rsoi.profile.repository.ProfileRepository;

import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Exchange exchange;

    public Profile getProfile(String name) {
        Optional<Profile> byId = profileRepository.findById(name);
        if (!byId.isPresent())
            throw new NoSuchProfileException();

        return byId.get();
    }

    public void putProfile(String name, ProfileIn profileIn) {
        Profile profile = modelMapper.map(profileIn, Profile.class);
        profile.setName(name);
        profileRepository.save(profile);

        String routingKey = "profile.updated";
        ProfileOutShort profileOutShort = modelMapper.map(profile, ProfileOutShort.class);
        rabbitTemplate.convertAndSend(exchange.getName(), routingKey, profileOutShort);

    }

    public Page<Profile> getProfiles(int page, int size) {
        return profileRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name")));
    }
}
