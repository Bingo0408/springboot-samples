package org.bingo.sample.security.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

/**HardCode用户名和密码以及权限
 * user-test拥有USER权限
 * admin-test拥有ADMIN,USER权限*/
//@Service
class UserDetailsServiceImpl implements UserDetailsService {

//    @Autowired
    private PasswordEncoder passwordEncoder;

    String password = passwordEncoder.encode("test");

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("user".equals(username)) {
            List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
            return new User(username, password, authorities);
        } else if ("admin".equals(username)) {
            List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
            return new User(username, password, authorities);
        } else {
            throw new UsernameNotFoundException("User Not Found!");
        }
    }
}