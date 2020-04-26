package ru.bmstu.cp.rsoi.profile.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.bmstu.cp.rsoi.profile.domain.Profile;
import ru.bmstu.cp.rsoi.profile.exception.ProfilesNotFoundException;
import ru.bmstu.cp.rsoi.profile.model.ProfileIn;
import ru.bmstu.cp.rsoi.profile.service.ProfileService;
import ru.bmstu.cp.rsoi.profile.web.event.PaginatedResultsRetrievedEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/1.0/rsoi")
@Api(value = "Profile service")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @GetMapping(path = "/protected/profile", params = { "page", "size" })
    public List<Profile> findPaginated(@RequestParam("page") int page,
                                       @RequestParam("size") int size,
                                       UriComponentsBuilder uriBuilder,
                                       HttpServletResponse response,
                                       HttpServletRequest request) {
        Page<Profile> resultPage = profileService.getProfiles(page, size);
        if (page > resultPage.getTotalPages()) {
            throw new ProfilesNotFoundException();
        }

        uriBuilder.path(request.getRequestURI());
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                Profile.class, uriBuilder, response, page, resultPage.getTotalPages(), size));

        return resultPage.getContent();
    }

    @GetMapping("/protected/profile/{name}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get profile by name", response = Profile.class)
    public Profile getProfile(@PathVariable String name) {
        return profileService.getProfile(name);
    }

    @PutMapping("/protected/profile/{name}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update profile")
    public void putProfile(@PathVariable String name, @RequestBody ProfileIn profile) {
        profileService.putProfile(name, profile);
    }

}
