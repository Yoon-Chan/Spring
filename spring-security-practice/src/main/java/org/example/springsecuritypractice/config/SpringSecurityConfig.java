package org.example.springsecuritypractice.config;

import lombok.RequiredArgsConstructor;
import org.example.springsecuritypractice.user.UserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Security 설정 Config
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final UserService userService;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {


        // basic authentication
        http.httpBasic(AbstractHttpConfigurer::disable); // basic authentication filter 비활성화
        // csrf
        http.csrf(httpSecurityCsrfConfigurer -> {
        });
        // remember-me
        http.rememberMe(httpSecurityRememberMeConfigurer -> {
        });

        http.authorizeHttpRequests((authz) -> {
            authz
                    .requestMatchers("/", "/home", "/signup").permitAll()
                    .requestMatchers("/note").hasRole("USER")
                    .requestMatchers("/admin").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/notice").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/notice").hasRole("ADMIN")
                    .anyRequest().authenticated();
        });

        // login
        http.formLogin((form) -> {
            form
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .permitAll(); // 모두 허용
        });
        // logout
        http.logout(httpSecurityLogoutConfigurer -> {
            httpSecurityLogoutConfigurer
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/");
        });

        return http.build();
    }

//    @Override
//    public void configure(WebSecurity web) {
//        // 정적 리소스 spring security 대상에서 제외
////        web.ignoring().antMatchers("/images/**", "/css/**"); // 아래 코드와 같은 코드입니다.
//        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//    }
//
//    /**
//     * UserDetailsService 구현
//     *
//     * @return UserDetailsService
//     */
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return username -> {
//            User user = userService.findByUsername(username);
//            if (user == null) {
//                throw new UsernameNotFoundException(username);
//            }
//            return user;
//        };
//    }
}
