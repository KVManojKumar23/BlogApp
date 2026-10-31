package org.com.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.com.dto.AuthRequest;
import org.com.dto.AuthResponse;
import org.com.dto.UserDTO;
import org.com.model.User;
import org.com.repo.UserRepo;
import org.com.service.Iservices.IUserServices;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServices implements IUserServices {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserServices(UserRepo userRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
            JwtService jwtService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public UserDTO convertToDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .phoneNum(user.getPhoneNum())
                .email(user.getEmail())
                .address(user.getAddress())
                .pin(user.getPin())
                .district(user.getDistrict())
                .state(user.getState())
                .country(user.getCountry())
                .roles(user.getRoles())
                .active(user.isActive())
                .build();
    }

    @Override
    public void createUser(UserDTO userDTO) {

        if (userDTO != null) {
            var checkEmail = this.userRepo.existsByEmail(userDTO.getEmail());
            if (!checkEmail) {
                User user = User.builder().name(userDTO.getName())
                        .phoneNum(userDTO.getPhoneNum())
                        .email(userDTO.getEmail())
                        .address(userDTO.getAddress())
                        .pin(userDTO.getPin())
                        .district(userDTO.getDistrict())
                        .state(userDTO.getState())
                        .country(userDTO.getCountry())
                        .roles(userDTO.getRoles())
                        .password(this.passwordEncoder.encode(userDTO.getPassword()))
                        .active(userDTO.isActive())
                        .build();
                this.userRepo.save(user);
            } else {
                throw new RuntimeException("User email is already exist");
            }
        } else {
            throw new RuntimeException("User Not Found!!");
        }
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        var id = userDTO.getId();
        User user = this.userRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not Found!!"));
        if (user != null) {
            user.setName(userDTO.getName());
            user.setPhoneNum(userDTO.getPhoneNum());
            user.setEmail(userDTO.getEmail());
            user.setAddress(userDTO.getAddress());
            user.setPin(userDTO.getPin());
            user.setDistrict(userDTO.getDistrict());
            user.setState(userDTO.getState());
            user.setCountry(userDTO.getCountry());
            user.setRoles(userDTO.getRoles());
            user.setActive(userDTO.isActive());
            this.userRepo.save(user);
        }
    }

    @Override
    public void deleteUser(int id) {
        User user = this.userRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not Found!!"));
        if (user != null) {
            user.setActive(false);
            this.userRepo.save(user);
        }
    }

    @Override
    public void parmentalyDeleteUser(int id) {
        User user = this.userRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not Found!!"));
        this.userRepo.delete(user);
    }

    @Override
    public UserDTO getUserById(int id) {
        User user = this.userRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not Found!!"));
        return this.convertToDto(user);
    }

    @Override
    public List<UserDTO> getUsers() {
        List<User> users = this.userRepo.findAll();
        return users.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void savePassword(int id, String password) {
        User user = this.userRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not Found!!"));
        if (user != null) {
            user.setPassword(this.passwordEncoder.encode(password));
            this.userRepo.save(user);
        } else {
            throw new RuntimeException("User Not Found!!");
        }
    }

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        Authentication authentication; 
        try {
            authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException ex){
            throw new BadCredentialsException("Invalid email or password");
        } catch (DisabledException ex) {
            throw new DisabledException("User account is disabled");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = this.userRepo.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = this.jwtService.generateToken(userDetails);

        Set<String> roleNames = user.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .name(user.getName())
                .roles(roleNames)
                .build();
    }
}
