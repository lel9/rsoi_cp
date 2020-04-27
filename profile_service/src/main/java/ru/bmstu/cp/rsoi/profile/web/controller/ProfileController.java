package ru.bmstu.cp.rsoi.profile.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.bmstu.cp.rsoi.profile.domain.Profile;
import ru.bmstu.cp.rsoi.profile.model.ProfileIn;
import ru.bmstu.cp.rsoi.profile.model.ProfileOut;
import ru.bmstu.cp.rsoi.profile.model.ProfileOutShort;
import ru.bmstu.cp.rsoi.profile.service.ProfileService;
import ru.bmstu.cp.rsoi.profile.web.event.PaginatedResultsRetrievedEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/1.0/rsoi")
@Api(value = "Profile service")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(path = "/protected/profile", params = { "page", "size" })
    public List<ProfileOutShort> findPaginated(@RequestParam("page") int page,
                                               @RequestParam("size") int size,
                                               UriComponentsBuilder uriBuilder,
                                               HttpServletResponse response,
                                               HttpServletRequest request) {
        Page<Profile> resultPage = profileService.getProfiles(page, size);

        uriBuilder.path(request.getRequestURI());
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                Profile.class, uriBuilder, response, page, resultPage.getTotalPages(), size));

        return resultPage.getContent()
                .stream()
                .map(profile -> modelMapper.map(profile, ProfileOutShort.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/protected/profile/{name}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get profile by name", response = ProfileOut.class)
    public ProfileOut getProfile(@PathVariable String name) {
        Profile profile = profileService.getProfile(name);
        return modelMapper.map(profile, ProfileOut.class);
    }

    @PutMapping("/protected/profile/{name}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update profile")
    public void putProfile(@PathVariable String name, @RequestBody @Valid ProfileIn profile) {
        Profile map = modelMapper.map(profile, Profile.class);
        profileService.putProfile(name, map);
    }

}
