package ru.bmstu.cp.rsoi.profile.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.bmstu.cp.rsoi.profile.domain.Profile;
import ru.bmstu.cp.rsoi.profile.model.ProfileIn;
import ru.bmstu.cp.rsoi.profile.model.ProfileOut;
import ru.bmstu.cp.rsoi.profile.model.ProfileOutShort;
import ru.bmstu.cp.rsoi.profile.service.ProfileService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/1.0/rsoi/profile")
@Api(value = "Profile service")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ModelMapper modelMapper;

    @Secured({"ROLE_USER", "ROLE_OPERATOR", "ROLE_EXPERT", "ROLE_ADMIN"})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get profile by id", response = ProfileOut.class)
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public ProfileOut getProfile(@PathVariable String id) {
        Profile profile = profileService.getProfile(id, true);
        return modelMapper.map(profile, ProfileOut.class);
    }

    @Secured({"ROLE_INTERNAL_CLIENT"})
    @GetMapping("/{id}/dispayName")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get profile by id", response = ProfileOutShort.class)
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public ProfileOutShort getDisplayName(@PathVariable String id) {
        Profile profile = profileService.getProfile(id, false);
        return modelMapper.map(profile, ProfileOutShort.class);
    }

    @Secured({"ROLE_USER", "ROLE_OPERATOR", "ROLE_EXPERT", "ROLE_ADMIN"})
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update profile")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public void putProfile(@PathVariable String id, @RequestBody @Valid ProfileIn profile) {
        Profile map = modelMapper.map(profile, Profile.class);
        profileService.putProfile(id, map);
    }

}
