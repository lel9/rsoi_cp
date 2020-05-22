package ru.bmstu.cp.rsoi.profile.service;

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
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private Logger log = Logger.getLogger(ProfileService.class.getName());

    public Profile getProfile(String id) {
        Optional<Profile> byId = profileRepository.findById(id);
        if (!byId.isPresent())
            throw new NoSuchProfileException();

        try {
            String routingKey = "operation";
            OperationOut operation = new OperationOut(id, "R");
            rabbitTemplate.convertAndSend("operationExchange", routingKey, operation);
            log.log(Level.INFO, "Operation was sent to RabbitMQ: " + operation);
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
        }
        return byId.get();
    }

    public void putProfile(String id, Profile profile) {
        checkAuth(id);

        profile.setId(id);
        Profile saved = profileRepository.save(profile);

        try {
            String routingKey = "profile.updated";
            rabbitTemplate.convertAndSend("eventExchange", routingKey, saved);
            log.log(Level.INFO, "information about updating profile was sent to RabbitMQ: " + saved.getId());
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
        }

        try {
            String routingKey = "operation";
            OperationOut operation = new OperationOut(id, "U");
            rabbitTemplate.convertAndSend("operationExchange", routingKey, operation);
            log.log(Level.INFO, "Operation was sent to RabbitMQ: " + operation);
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
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
