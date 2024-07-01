package com.jake.userservice.service;


import com.jake.datacorelib.business.jpa.BusinessRepository;
import com.jake.datacorelib.user.dto.UserDTO;
import com.jake.datacorelib.user.jpa.User;
import com.jake.datacorelib.user.jpa.UserRepository;
import com.jake.userservice.exception.UserAlreadyExistsException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder encoder;

    @PostConstruct
    public void t() {
        UserDTO dto = new UserDTO();
        dto.setUsername("test");
        dto.setPassword("test");
        try {
            addNewUser(dto);

        } catch(Exception e) {
            //ignore
        }
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }

    public User addNewUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        /*Optional<Business> optionalBusiness = businessRepository.findById(userDTO.getBusinessId());
        if (optionalBusiness.isEmpty()) {
            throw new BusinessNotFoundException(userDTO.getBusinessId());
        }*/

        if(userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(userDTO.getUsername());
        }

        user.setRoles(null);
        user.setPassword(encoder.encode(userDTO.getPassword()));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(false); //Account will be enabled once added to a Business.
        return userRepository.save(user);
    }
}
