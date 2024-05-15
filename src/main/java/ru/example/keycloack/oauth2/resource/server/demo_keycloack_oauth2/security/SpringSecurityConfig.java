package ru.example.keycloack.oauth2.resource.server.demo_keycloack_oauth2.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import ru.example.keycloack.oauth2.resource.server.demo_keycloack_oauth2.converter.KCRoleConverter;

@Configuration // данный класс будет считан как конфиг для spring контейнера
@EnableWebSecurity // включает механизм защиты адресов, которые настраиваются в SecurityFilterChain
@EnableGlobalMethodSecurity(prePostEnabled = true) // включение механизма для защиты методов по ролям
// в старых версиях spring security нужно было наследовать от спец. класса WebSecurityConfigurerAdapter
// Подробнее https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
public class SpringSecurityConfig {

    // создается спец. бин, который отвечает за настройки запросов по http (метод вызывается автоматически) Spring контейнером
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // конвертер для настройки spring security
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        // подключаем конвертер ролей
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KCRoleConverter());

        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/test/login").permitAll() // анонимные пользователи смогут выполнять запрос только по этому URI
                .anyRequest().authenticated() // остальной API будет доступен только аутентифицированным пользователям
        )
                .oauth2ResourceServer(oauth2 -> oauth2  // добавляем конвертер ролей из JWT в Authority (Role)
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter)
                        )
                );
        return http.build();
    }

}
