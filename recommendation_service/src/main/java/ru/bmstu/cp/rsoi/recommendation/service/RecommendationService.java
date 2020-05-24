package ru.bmstu.cp.rsoi.recommendation.service;

import org.apache.commons.codec.binary.Base64;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.bmstu.cp.rsoi.recommendation.domain.Profile;
import ru.bmstu.cp.rsoi.recommendation.domain.Recommendation;
import ru.bmstu.cp.rsoi.recommendation.exception.FailureWhenRecommendationAddException;
import ru.bmstu.cp.rsoi.recommendation.exception.NoProfileException;
import ru.bmstu.cp.rsoi.recommendation.model.OperationOut;
import ru.bmstu.cp.rsoi.recommendation.model.RecommendationIn;
import ru.bmstu.cp.rsoi.recommendation.model.Token;
import ru.bmstu.cp.rsoi.recommendation.repository.RecommendationRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class RecommendationService {

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private Logger log = Logger.getLogger(RecommendationService.class.getName());

    @Value( "${service.profile.host}" )
    private String profileServiceHost;

    @Value( "${service.profile.port}" )
    private Integer profileServicePort;

    @Value( "${service.session.host}" )
    private String sessionServiceHost;

    @Value( "${service.session.port}" )
    private Integer sessionServicePort;

    @Value( "${service.session.clientId}" )
    private String sessionServiceClientId;

    @Value( "${service.session.secret}" )
    private String sessionServiceSecret;

    private static Token TOKEN = new Token();

    public Page<Recommendation> getRecommendations(String drugId, int page, int size) {
        return recommendationRepository.findByDrugId(drugId, PageRequest.of(page, size, new Sort(Sort.Direction.DESC, "date")));
    }

    public Long getCountByDrugId(String drugId) {
        return recommendationRepository.countAllByDrugId(drugId);
    }

    public String postRecommendation(RecommendationIn recommendationIn) throws URISyntaxException {
        Recommendation recommendation = new Recommendation();
        recommendation.setDate(System.currentTimeMillis());
        recommendation.setDrugId(recommendationIn.getDrugId());
        recommendation.setText(recommendationIn.getText());
        recommendation.setAuthor(getProfile());
        Recommendation save = recommendationRepository.save(recommendation);
        String id = save.getId();

        try {
            String routingKey = "operation";
            OperationOut operation = new OperationOut(id, recommendationIn.getDrugId(), "C");
            rabbitTemplate.convertAndSend("operationExchange", routingKey, operation);
            log.log(Level.INFO, "Operation was sent to RabbitMQ: " + operation);
        } catch (Exception ex) {
            log.log(Level.SEVERE, ex.getMessage());
        }

        return id;
    }

    public void putRecommendation(String id, RecommendationIn recommendationIn) throws URISyntaxException {
        Optional<Recommendation> oldRecommendation = recommendationRepository.findById(id);
        if (!oldRecommendation.isPresent()) {
            Recommendation recommendation = new Recommendation();
            recommendation.setId(id);
            recommendation.setDate(System.currentTimeMillis());
            recommendation.setDrugId(recommendationIn.getDrugId());
            recommendation.setText(recommendationIn.getText());
            recommendation.setAuthor(getProfile());
            recommendationRepository.save(recommendation);
        } else {
            Recommendation recommendation = oldRecommendation.get();
            checkAuth(recommendation.getAuthor().getId());
            recommendation.setDrugId(recommendationIn.getDrugId());
            recommendation.setText(recommendationIn.getText());
            recommendationRepository.save(recommendation);
        }

        try {
            String routingKey = "operation";
            OperationOut operation = new OperationOut(id, recommendationIn.getDrugId(), "U");
            rabbitTemplate.convertAndSend("operationExchange", routingKey, operation);
            log.log(Level.INFO, "Operation was sent to RabbitMQ: " + operation);
        } catch (Exception ex) {
            log.log(Level.SEVERE, ex.getMessage());
        }
    }

    public void deleteRecommendation(String id) {
        Optional<Recommendation> recommendation = recommendationRepository.findById(id);
        if (recommendation.isPresent()) {
            checkAuth(recommendation.get().getAuthor().getId());
            recommendationRepository.deleteById(id);

            try {
                String routingKey = "operation";
                OperationOut operation = new OperationOut(id, recommendation.get().getDrugId(), "D");
                rabbitTemplate.convertAndSend("operationExchange", routingKey, operation);
                log.log(Level.INFO, "Operation was sent to RabbitMQ: " + operation);
            } catch (Exception ex) {
                log.log(Level.SEVERE, ex.getMessage());
            }
        }
    }

    private Profile getProfile() throws URISyntaxException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            return callProfileService(principal);
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                TOKEN = getToken();
                try {
                    return callProfileService(principal);
                } catch (HttpStatusCodeException ex2) {
                    if (ex2.getStatusCode() == HttpStatus.NOT_FOUND) {
                        throw new NoProfileException();
                    }
                    throw new FailureWhenRecommendationAddException();
                } catch (Exception ex3) {
                    throw new FailureWhenRecommendationAddException();
                }
            }
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new NoProfileException();
            }
            throw new FailureWhenRecommendationAddException();
        } catch (Exception ex) {
            throw new FailureWhenRecommendationAddException();
        }
    }

    private Profile callProfileService(Object principal) throws URISyntaxException {
        URI thirdPartyApi = new URI("http", null, profileServiceHost, profileServicePort, "/api/1.0/protected/profile/" + principal, null, null);
        ResponseEntity<Profile> exchange = restTemplate.exchange(thirdPartyApi,
                HttpMethod.GET, new HttpEntity<>(createHeaders()), Profile.class);
        Profile body = exchange.getBody();
        if (body == null)
            throw new NoProfileException();
        body.setId(principal.toString());
        return body;
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

    private Token getToken() throws URISyntaxException {
        try {
            LinkedMultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "client_credentials");

            URI thirdPartyApi = new URI("http", null, sessionServiceHost, sessionServicePort, "/oauth/token", null, null);
            ResponseEntity<Token> exchange = restTemplate.exchange(thirdPartyApi, HttpMethod.POST, new HttpEntity<>(params, createHeaders(sessionServiceClientId, sessionServiceSecret)), Token.class);
            return exchange.getBody();
        } catch (HttpStatusCodeException ex) {
            throw new FailureWhenRecommendationAddException();
        }

    }

    private HttpHeaders createHeaders(String username, String password){
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(StandardCharsets.UTF_8) );
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }

    private HttpHeaders createHeaders() {
        return new HttpHeaders() {{
            String authHeader = "Bearer " + TOKEN.getAccess_token();
            set("Authorization", authHeader);
        }};
    }
}
