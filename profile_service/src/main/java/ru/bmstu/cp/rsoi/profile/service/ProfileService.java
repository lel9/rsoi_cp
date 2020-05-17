package ru.bmstu.cp.rsoi.profile.service;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.bmstu.cp.rsoi.profile.domain.Profile;
import ru.bmstu.cp.rsoi.profile.exception.NoSuchProfileException;
import ru.bmstu.cp.rsoi.profile.model.OperationOut;
import ru.bmstu.cp.rsoi.profile.repository.ProfileRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Profile getProfile(String id) {
        Optional<Profile> byId = profileRepository.findById(id);
        if (!byId.isPresent())
            throw new NoSuchProfileException();

        try {
            String routingKey = "operation";
            rabbitTemplate.convertAndSend("operationExchange", routingKey, new OperationOut(id, "R"));
        } catch (Exception e) {
            e.printStackTrace(); // todo логгирование
        }
        return byId.get();
    }

    public void putProfile(String id, Profile profile) {
        checkAuth(id);

        profile.setId(id);
        Profile saved = profileRepository.save(profile);

        try {
            String routingKey = "profile.updated";
            rabbitTemplate.convertAndSend("operationExchange", routingKey, saved);
        } catch (Exception e) {
            e.printStackTrace(); // todo логгирование
        }

        try {
            String routingKey = "operation";
            rabbitTemplate.convertAndSend("operationExchange", routingKey, new OperationOut(id, "U"));
        } catch (Exception e) {
            e.printStackTrace(); // todo логгирование
        }
    }

    private void checkAuth(String profileId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities != null && authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
            return;
        if (authentication.getPrincipal().equals(profileId))
            return;
        throw new AccessDeniedException("Доступ запрещен");
    }
}
