package ru.bmstu.cp.rsoi.profile.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.bmstu.cp.rsoi.profile.domain.Profile;
import ru.bmstu.cp.rsoi.profile.model.ProfileIn;
import ru.bmstu.cp.rsoi.profile.model.ProfileOut;
import ru.bmstu.cp.rsoi.profile.service.ProfileService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/1.0/rsoi/profile")
@Api(value = "Profile service")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get profile by id", response = ProfileOut.class)
    public ProfileOut getProfile(@PathVariable String id) {
        Profile profile = profileService.getProfile(id);
        return modelMapper.map(profile, ProfileOut.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update profile")
    public void putProfile(@PathVariable String id, @RequestBody @Valid ProfileIn profile) {
        Profile map = modelMapper.map(profile, Profile.class);
        profileService.putProfile(id, map);
    }

}
