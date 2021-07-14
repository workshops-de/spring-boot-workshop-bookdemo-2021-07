package de.workshops.bookdemo.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//       auth.inMemoryAuthentication()
//           .withUser("admin").password("password").roles("USER", "ADMIN");
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {

            private List<User> users = Arrays.asList(
                    new User("alice", "$2a$10$IG4a9ZOTXqzJAPT3L7ju9OyQBL/6XHYZF4G51jTVSeDtJw/t43nAS", Arrays.asList(new SimpleGrantedAuthority("ADMIN"))),
                    new User("bob", "bpass", Arrays.asList(new SimpleGrantedAuthority("USER"))));
            
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return users.stream()
                    .filter(user -> user.getUsername().equals(username))
                    .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                        if (list.size() ==  0) {
                            throw new UsernameNotFoundException(username);
                        }
                        // deep copy notwendig
                        User result = list.get(0);
                        return new User(result.getUsername(), result.getPassword(), result.getAuthorities());
                    }));
            }
        };
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // super.configure(http);
        http
//            .formLogin()
//                .usernameParameter("user")
//                .passwordParameter("pass")
//            .and()
        	.httpBasic()
        		.and()
            .oauth2Login()
            	.and()
            .authorizeRequests()
                .antMatchers("/**").authenticated()
                .and()
            .csrf().disable();
    }


}