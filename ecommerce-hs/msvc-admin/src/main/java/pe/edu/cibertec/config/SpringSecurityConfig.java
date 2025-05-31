package pe.edu.cibertec.config;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import pe.edu.cibertec.service.usuario.impl.UsuarioServices;

@Configuration
public class SpringSecurityConfig {


    @Bean
    PasswordEncoder passwordEncoder() {
	return new BCryptPasswordEncoder();
    }

    @Autowired
    private UsuarioServices usuarioServices;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
	AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
	authBuilder.userDetailsService(usuarioServices).passwordEncoder(passwordEncoder());
	return authBuilder.build();
    }
    
    @Bean 
    public SecurityFilterChain FilterChain(HttpSecurity http) throws Exception {
    	System.err.println(passwordEncoder().encode("admin"));
    	return http.authorizeHttpRequests((authz) -> authz
    		// TODOS LOS USUARIOS
    		.requestMatchers("/**")
    		.permitAll().requestMatchers(HttpMethod.POST, "/pago/crear-preferencia").hasAnyRole("CLIENTE")    		
    		.anyRequest().denyAll())    		
    		.build();
        }

}
