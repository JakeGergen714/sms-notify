package com.jake.userservice.controller;

import com.jake.datacorelib.business.dto.BusinessDTO;
import com.jake.datacorelib.business.jpa.Business;
import com.jake.datacorelib.user.dto.UserDTO;
import com.jake.datacorelib.user.jpa.User;
import com.jake.userservice.service.BusinessService;
import com.jake.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final BusinessService businessService;
    private final ModelMapper modelMapper;

   @CrossOrigin(origins = "http://192.168.1.241:8090", allowCredentials = "true")
    @GetMapping(value = "/user")
    public ResponseEntity<UserDTO> getMyUser(Authentication authentication) {
       log.info("TEST <{}>", ((Jwt) authentication.getPrincipal()).getClaimAsString("Roles"));
       String username = getUsername(authentication);
       log.info("GET /user <{}>",username);
        return ResponseEntity.ok(modelMapper.map(userService.getUserByUsername(username), UserDTO.class));
    }

    @PostMapping(value = "/user")
    public ResponseEntity<User> addUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.addNewUser(userDTO));
    }

    @PostMapping(value = "/business")
    public ResponseEntity<Business> addBusiness(Authentication authentication, @RequestBody BusinessDTO businessDTO) {
        String username = getUsername(authentication);
        log.info("POST /business <{}> <{}>",username, businessDTO);
        return ResponseEntity.ok(businessService.addNewBusiness(businessDTO, username));
    }



    private long getBusinessId(Authentication authentication) {
        return Long.valueOf(((Jwt) authentication.getPrincipal()).getClaimAsString("businessId"));
    }

    private String getUsername(Authentication authentication ) {
        return ((Jwt) authentication.getPrincipal()).getSubject();
    }
}
