package ru.bmstu.cp.rsoi.analyzer.service;

import org.apache.commons.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.bmstu.cp.rsoi.analyzer.exception.FailureWhenSearchException;
import ru.bmstu.cp.rsoi.analyzer.model.*;
import ru.bmstu.cp.rsoi.analyzer.model.drug.DrugOut;
import ru.bmstu.cp.rsoi.analyzer.model.drug.ListDrugOut;
import ru.bmstu.cp.rsoi.analyzer.model.reception.ListReceptionWithPatientOut;
import ru.bmstu.cp.rsoi.analyzer.model.reception.ReceptionWithPatientOut;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class AnalyzerService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ModelMapper modelMapper;

    private Logger log = Logger.getLogger(AnalyzerService.class.getName());

    @Value( "${service.drug.host}" )
    private String drugServiceHost;

    @Value( "${service.drug.port}" )
    private Integer drugServicePort;

    @Value( "${service.patient.host}" )
    private String patientServiceHost;

    @Value( "${service.patient.port}" )
    private Integer patientServicePort;

    @Value( "${service.session.host}" )
    private String sessionServiceHost;

    @Value( "${service.session.port}" )
    private Integer sessionServicePort;

    @Value( "${service.session.clientId}" )
    private String sessionServiceClientId;

    @Value( "${service.session.secret}" )
    private String sessionServiceSecret;

    private static Token TOKEN = new Token();

    public ListSearchResultsOut searchDrugs(ReceptionIn receptionIn) throws URISyntaxException {

        StateIn state = receptionIn.getState();
        Integer months = null;
        Integer years = null;
        Character sex = null;
        String lifeAnamnesis = null;
        String diseaseAnamnesis = null;
        String plaints = null;
        String objectiveInspection = null;
        String examinationsResults = null;
        String specialistsConclusions = null;

        if (state != null) {
            months = state.getMonths();
            years = state.getYears();
            sex = state.getSex();
            lifeAnamnesis = state.getLifeAnamnesis();
            diseaseAnamnesis = state.getDiseaseAnamnesis();
            plaints = state.getPlaints();
            objectiveInspection = state.getObjectiveInspection();
            examinationsResults = state.getExaminationsResults();
            specialistsConclusions = state.getSpecialistsConclusions();
        }
        DiagnosisIn diagnosis = receptionIn.getDiagnosis();
        String diagnosisText = null;
        if (diagnosis != null) {
            diagnosisText = diagnosis.getText();
        }

        List<ReceptionWithPatientOut> receptions = getReceptions(sex, years, months, lifeAnamnesis, diseaseAnamnesis, plaints,
                objectiveInspection, examinationsResults, specialistsConclusions, diagnosisText);

        if (receptions == null)
            return new ListSearchResultsOut(Collections.emptyList());

        List<SearchResult> results = new ArrayList<>();
        List<ReceptionWithPatientOut> collect = receptions.stream().filter(r -> {
            try {
                return parseDate(r.getDate()) != null && r.getPatient() != null && r.getPatient().getId() != null;
            } catch (DateTimeParseException e) {
                return false;
            }
        }).collect(Collectors.toList());

        List<ReceptionWithPatientOut> newList = new ArrayList<>();
        for (ReceptionWithPatientOut r: collect) {
            for (DrugOut d: r.getDrugs()) {
                ArrayList<DrugOut> drug = new ArrayList<>();
                drug.add(d);
                newList.add(new ReceptionWithPatientOut(
                        r.getId(), r.getPatient(), r.getDate(), r.getState(), r.getDiagnosis(), drug
                ));
            }
        }

        Map<DrugOut, List<ReceptionWithPatientOut>> map = newList.stream().collect(Collectors.groupingBy(r -> r.getDrugs().get(0)));
        for (Map.Entry<DrugOut, List<ReceptionWithPatientOut>> entry : map.entrySet()) {
            DrugOut key = entry.getKey();
            List<ReceptionWithPatientOut> value = entry.getValue();
            Map<String, List<ReceptionWithPatientOut>> patients = value.stream().collect(Collectors.groupingBy(r -> r.getPatient().getId()));
            List<ReceptionWithPatientOut> outcomes = new ArrayList<>();
            String pid = null;
            for (Map.Entry<String, List<ReceptionWithPatientOut>> p : patients.entrySet()) {
                if (p.getValue().size() > outcomes.size()) {
                    outcomes = p.getValue();
                    pid = p.getKey();
                }
            }
            if (pid != null) {
                //drugsOuts.add(modelMapper.map(key, DrugOutShort.class));
                DrugOut drug = getDrug(key.getId());
                List<DrugOutShort> drugsOuts = getAnalogs(drug.getActiveSubstance())
                        .stream()
                        .map(analog -> modelMapper.map(analog, DrugOutShort.class)).collect(Collectors.toList());
                List<ReceptionOut> outs = outcomes
                            .stream()
                            .map(outcome -> modelMapper.map(outcome, ReceptionOut.class))
                            .collect(Collectors.toList());
                results.add(new SearchResult(pid, drugsOuts, outs));
            }
        }
//        for (ReceptionWithPatientOut reception : receptions) {
//
//            String pid = reception.getPatient().getId();
//            if (pid == null || pid.isEmpty())
//                continue;
//
//            String date = reception.getDate();
//            if (date == null || date.isEmpty())
//                continue;
//
//            for (DrugOut drugOut : reception.getDrugs()) {
//
//                boolean alreadyInclude = false;
//                for (SearchResult result : results) {
//                    String drug = drugOut.getId();
//                    if (result.getDrug().get(0).getId().equals(drug)) {
//                        alreadyInclude = true;
//                        break;
//                    }
//                }
//
//                if (!alreadyInclude) {
//                    List<ReceptionOut> outs = getOutcomes(date, pid, drugOut.getId())
//                            .stream()
//                            .map(outcome -> modelMapper.map(outcome, ReceptionOut.class))
//                            .collect(Collectors.toList());
//
//                    List<DrugOutShort> drugsOuts = new ArrayList<>();
//                    drugsOuts.add(modelMapper.map(drugOut, DrugOutShort.class));
//                    List<DrugOutShort> analogs = getAnalogs(drugOut.getActiveSubstance())
//                            .stream()
//                            .map(analog -> modelMapper.map(analog, DrugOutShort.class))
//                            .collect(Collectors.toList());
//                    drugsOuts.addAll(analogs);
//                    results.add(new SearchResult(pid, drugsOuts, outs));
//                }
//            }
//        }

        try {
            String routingKey = "operation";
            OperationOut operation = new OperationOut(null, "C");
            rabbitTemplate.convertAndSend("operationExchange", routingKey, operation);
            log.log(Level.INFO, "Operation was sent to RabbitMQ: " + operation);
        } catch (Exception ex) {
            log.log(Level.SEVERE, ex.getMessage());
        }

        return new ListSearchResultsOut(results);
    }

    private DrugOut getDrug(String id) throws URISyntaxException {
        try {
            return callDrugServiceById(id);
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                TOKEN = getToken();
                try {
                    return callDrugServiceById(id);
                } catch (Exception ex3) {
                    throw new FailureWhenSearchException();
                }
            }
            throw new FailureWhenSearchException();
        } catch (Exception ex) {
            throw new FailureWhenSearchException();
        }
    }

    private List<DrugOut> getAnalogs(String activeSubstance) throws URISyntaxException {
        try {
            return callDrugService(activeSubstance);
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                TOKEN = getToken();
                try {
                    return callDrugService(activeSubstance);
                } catch (Exception ex3) {
                    throw new FailureWhenSearchException();
                }
            }
            throw new FailureWhenSearchException();
        } catch (Exception ex) {
            throw new FailureWhenSearchException();
        }
    }

    private List<ReceptionWithPatientOut> getOutcomes(String date, String patientId, String drug) throws URISyntaxException {
        try {
            return callPatientService(date, patientId, drug);
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                TOKEN = getToken();
                try {
                    return callPatientService(date, patientId, drug);
                } catch (Exception ex3) {
                    throw new FailureWhenSearchException();
                }
            }
            throw new FailureWhenSearchException();
        } catch (Exception ex) {
            throw new FailureWhenSearchException();
        }
    }

    private List<ReceptionWithPatientOut> getReceptions(Character sex,
                                                        Integer years,
                                                        Integer months,
                                                        String lifeAnamnesis,
                                                        String diseaseAnamnesis,
                                                        String plaints,
                                                        String objectiveInspection,
                                                        String examinationsResults,
                                                        String specialistsConclusions,
                                                        String diagnosisText) throws URISyntaxException {
        try {
            return callPatientService(sex, years, months, lifeAnamnesis, diseaseAnamnesis,
                    plaints, objectiveInspection, examinationsResults, specialistsConclusions, diagnosisText);
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                TOKEN = getToken();
                try {
                    return callPatientService(sex, years, months, lifeAnamnesis, diseaseAnamnesis,
                            plaints, objectiveInspection, examinationsResults, specialistsConclusions, diagnosisText);
                } catch (Exception ex3) {
                    throw new FailureWhenSearchException();
                }
            }
            throw new FailureWhenSearchException();
        } catch (Exception ex) {
            throw new FailureWhenSearchException();
        }
    }

    private List<DrugOut> callDrugService(String activeSubstance) {
        String url = "http://" + drugServiceHost + ":" + drugServicePort + "/api/1.0/private/drug/search";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                // Add query parameter
                .queryParam("activeSubstance", activeSubstance);
        URI thirdPartyApi = builder.buildAndExpand().encode().toUri();

        ResponseEntity<ListDrugOut> exchange = restTemplate.exchange(thirdPartyApi,
                HttpMethod.GET, new HttpEntity<>(createHeaders()), ListDrugOut.class);
        ListDrugOut body = exchange.getBody();
        if (body == null)
            throw new FailureWhenSearchException();
        return body.getResults();
    }

    private DrugOut callDrugServiceById(String id) {
        String url = "http://" + drugServiceHost + ":" + drugServicePort + "/api/1.0/public/drug/" + id;
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        URI thirdPartyApi = builder.buildAndExpand().encode().toUri();

        ResponseEntity<DrugOut> exchange = restTemplate.exchange(thirdPartyApi,
                HttpMethod.GET, new HttpEntity<>(createHeaders()), DrugOut.class);
        DrugOut body = exchange.getBody();
        if (body == null)
            throw new FailureWhenSearchException();
        return body;
    }

    private List<ReceptionWithPatientOut> callPatientService(String date,
                                                             String patientId,
                                                             String drugId) {
        String url = "http://" + patientServiceHost + ":" + patientServicePort + "/api/1.0/private/reception/search";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                // Add query parameter
                .queryParam("dateStart", date)
                .queryParam("patientId", patientId)
                .queryParam("drugId", drugId);
        URI thirdPartyApi = builder.buildAndExpand().encode().toUri();

        ResponseEntity<ListReceptionWithPatientOut> exchange = restTemplate.exchange(thirdPartyApi,
                HttpMethod.GET, new HttpEntity<>(createHeaders()), ListReceptionWithPatientOut.class);
        ListReceptionWithPatientOut body = exchange.getBody();
        if (body == null)
            throw new FailureWhenSearchException();
        return body.getResults();
    }

    private List<ReceptionWithPatientOut> callPatientService(Character sex,
                                                             Integer years,
                                                             Integer months,
                                                             String lifeAnamnesis,
                                                             String diseaseAnamnesis,
                                                             String plaints,
                                                             String objectiveInspection,
                                                             String examinationsResults,
                                                             String specialistsConclusions,
                                                             String diagnosisText) {
        String url = "http://" + patientServiceHost + ":" + patientServicePort + "/api/1.0/private/reception/search";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                // Add query parameter
                .queryParam("sex", sex)
                .queryParam("years", years)
                .queryParam("months", months)
                .queryParam("lifeAnamnesis", lifeAnamnesis)
                .queryParam("diseaseAnamnesis", diseaseAnamnesis)
                .queryParam("plaints", plaints)
                .queryParam("objectiveInspection", objectiveInspection)
                .queryParam("examinationsResults", examinationsResults)
                .queryParam("specialistsConclusions", specialistsConclusions)
                .queryParam("diagnosisText", diagnosisText);

        URI thirdPartyApi = builder.buildAndExpand().encode().toUri();

        ResponseEntity<ListReceptionWithPatientOut> exchange = restTemplate.exchange(thirdPartyApi,
                HttpMethod.GET, new HttpEntity<>(createHeaders()), ListReceptionWithPatientOut.class);
        ListReceptionWithPatientOut body = exchange.getBody();
        if (body == null)
            throw new FailureWhenSearchException();
        return body.getResults();
    }

    private Token getToken() throws URISyntaxException {
        try {
            LinkedMultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "client_credentials");

            URI thirdPartyApi = new URI("http", null, sessionServiceHost, sessionServicePort, "/oauth/token", null, null);
            ResponseEntity<Token> exchange = restTemplate.exchange(thirdPartyApi, HttpMethod.POST, new HttpEntity<>(params, createHeaders(sessionServiceClientId, sessionServiceSecret)), Token.class);
            return exchange.getBody();
        } catch (HttpStatusCodeException ex) {
            throw new FailureWhenSearchException();
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

    private OffsetDateTime parseDate(String date) {
        if (date == null || date.isEmpty())
            return null;
        return OffsetDateTime.parse(date);
    }
}
