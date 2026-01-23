package com.Manoj.user_service.serivce;

import com.Manoj.user_service.dto.UserRequestDTO;
import com.Manoj.user_service.dto.UserResponseDTO;
import com.Manoj.user_service.model.User;
import com.Manoj.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        User user = User.builder()
                .username(userRequestDTO.getUsername())
                .password(Objects.requireNonNull(bCryptPasswordEncoder.encode(userRequestDTO.getPassword())))
                .email(userRequestDTO.getEmail())
                .role(userRequestDTO.getRole() != null ? userRequestDTO.getRole() : "USER")
                .build();
        User savedUser = userRepository.save(user);
        return toUserResponseDTO(savedUser);
    }

    public UserResponseDTO getUserById(Long id){
        return userRepository.findById(id)
                .map(this::toUserResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        User existingUser =  userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        existingUser.setUsername(userRequestDTO.getUsername());
        existingUser.setEmail(userRequestDTO.getEmail());
        existingUser.setPassword(bCryptPasswordEncoder.encode(userRequestDTO.getPassword()));
        existingUser.setRole(userRequestDTO.getRole() != null  ? userRequestDTO.getRole() : "USER");
        User savedUser = userRepository.save(existingUser);
        return toUserResponseDTO(savedUser);
    }

    public void deleteUser(Long id){
        User user =  userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toUserResponseDTO)
                .collect(Collectors.toList());
    }

    private UserResponseDTO toUserResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

}

    class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {super(message);}
    }
