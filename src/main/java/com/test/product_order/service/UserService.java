package com.test.product_order.service;

import com.test.product_order.entity.Role;
import com.test.product_order.entity.User;
import com.test.product_order.exception.BadRequest;
import com.test.product_order.dto.LoginRequest;
import com.test.product_order.dto.SignupRequest;
import com.test.product_order.dto.JwtResponse;
import com.test.product_order.repository.RoleRepository;
import com.test.product_order.repository.UserRepository;
import com.test.product_order.security.jwt.JwtUtils;
import com.test.product_order.security.services.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class UserService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    public List<User> findListUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        User findUser = userRepository.findById(id).orElse(null);
        if (findUser == null) throw new BadRequest("User is not found");
        return findUser;
    }

    public ResponseEntity<?> signIn(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));
    }

    public User signUp(SignupRequest signupRequest){
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new BadRequest("Username is already taken!");
        }

        User user = new User(signupRequest.getUsername(), encoder.encode(signupRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role findRole;
        if (signupRequest.getIsAdmin().equals(true)){
            findRole = roleRepository.findByName("ROLE_ADMIN");
            if (findRole == null){
                findRole = checkRoleExist("ROLE_ADMIN");
            }
        } else {
            findRole = roleRepository.findByName("ROLE_USER");
            if (findRole == null){
                findRole = checkRoleExist("ROLE_USER");
            }
        }
        roles.add(findRole);
        user.setRoles(roles);
        return userRepository.save(user);
    }

    private Role checkRoleExist(String roleName) {
        Role role = new Role();
        role.setName(roleName);
        return roleRepository.save(role);
    }


}
