package com.cydeo.service.impl;

import com.cydeo.entity.User;
import com.cydeo.entity.common.UserPrincipal;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.SecurityService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    private final UserRepository userRepository;

    public SecurityServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // need to get User from DB (by username) and convert to Security User obj.
    // using UserPrincipal class (our mapper)

        User user = userRepository.findByUserNameAndIsDeleted(username,false);

        if (user == null) {     // validation
            throw new UsernameNotFoundException(username);
        }

        return new UserPrincipal(user); // user Entity converted to Security User
    }
}
