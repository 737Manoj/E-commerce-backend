package com.Manoj.user_service.controller;

import com.Manoj.user_service.dto.AuthRequest;
import com.Manoj.user_service.dto.UserRequestDTO;
import com.Manoj.user_service.dto.UserResponseDTO;
import com.Manoj.user_service.security.JwtService;
import com.Manoj.user_service.serivce.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        Cookie jwtCookie = new Cookie("JWT", token);
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(60*60);
        jwtCookie.setSecure(false);
        response.addCookie(jwtCookie);

        UserResponseDTO userResponseDTO = userService.getUserByUsername(authRequest.getUsername());
        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDTO userRequestDTO, HttpServletResponse response) {
        if(userService.getUserByEmail(userRequestDTO.getEmail())){return ResponseEntity.badRequest().body(Map.of("message", "Email already in use."));
        }
        UserResponseDTO createdUser = userService.createUser(userRequestDTO);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequestDTO.getUsername(), userRequestDTO.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        Cookie jwtCookie = new Cookie("token", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setSecure(false); //set true in production
        jwtCookie.setMaxAge(60*60);
        response.addCookie(jwtCookie);

        return new ResponseEntity<>(createdUser,HttpStatus.CREATED);
    }
    
}
