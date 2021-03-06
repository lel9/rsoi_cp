package ru.bmstu.cp.rsoi.profile.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ResponseHeader;
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
@RequestMapping("/api/1.0")
@Api(value = "Profile service")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/protected/profile/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get profile by id", response = ProfileOut.class)
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public ProfileOut getProfile(@PathVariable String id) {
        Profile profile = profileService.getProfile(id);
        return modelMapper.map(profile, ProfileOut.class);
    }

    @PutMapping("/protected/profile/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update profile")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public void putProfile(@PathVariable String id, @RequestBody @Valid ProfileIn profile) {
        Profile map = modelMapper.map(profile, Profile.class);
        profileService.putProfile(id, map);
    }

}
