package edu.sabanciuniv.hotelbookingapp.security;

import edu.sabanciuniv.hotelbookingapp.model.Users;
import edu.sabanciuniv.hotelbookingapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepository.findByUsername(username);

        if (users != null) {
            return new org.springframework.security.core.userdetails.User(
                    users.getUsername(),
                    users.getPassword(),
                    getAuthorities(users));
        } else {
            throw new UsernameNotFoundException("Invalid username or password!");
        }
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Users users){
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + users.getRole().getRoleType().name()));
        return authorities;
    }

}
