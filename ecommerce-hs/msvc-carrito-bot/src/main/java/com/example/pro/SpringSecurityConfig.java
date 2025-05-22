package com.example.pro;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.session.DefaultCookieSerializerCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.example.pro.jwt.filter.JwtAuthenticationFilter;
import com.example.pro.jwt.filter.JwtValidationFilter;
import com.example.pro.services.IUsuarioServices;
import com.example.pro.services.Impl.UsuarioServices;
import com.google.common.net.HttpHeaders;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import jakarta.servlet.http.HttpSession;

@Configuration
public class SpringSecurityConfig {

//    @Value("${ORIGIN_ANGULAR}")
//    private String origenAngular;

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
    @Order(1)
    public SecurityFilterChain webhookFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/dialogflow", "/webhook", "/pago") // <-- solo aplica a esta ruta
            .authorizeHttpRequests(authz -> authz.anyRequest().permitAll())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }


    @Bean
    @Order(2)
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	System.err.println(passwordEncoder().encode("admin"));
	return http.authorizeHttpRequests((authz) -> authz
		// TODOS LOS USUARIOS
		.requestMatchers("/", "/login", "/Producto/list", "/swagger-ui/**", "/Cliente/add", "/Cliente/verificar-correo")
		.permitAll().requestMatchers(HttpMethod.POST, "/pago/crear-preferencia").hasAnyRole("CLIENTE")
		.requestMatchers(HttpMethod.GET, "/Cliente/**").hasAnyRole("CLIENTE")
		.requestMatchers(HttpMethod.POST, "/Cliente/**").hasAnyRole("CLIENTE")
		.requestMatchers(HttpMethod.PUT, "/Cliente/**").hasAnyRole("CLIENTE")
		.requestMatchers(HttpMethod.GET, "/Venta/**").hasAnyRole("CLIENTE")
		.requestMatchers(HttpMethod.POST, "/Venta/**").hasAnyRole("CLIENTE")
		.requestMatchers(HttpMethod.PUT, "/Venta/**").hasAnyRole("CLIENTE").anyRequest().authenticated())
		.cors(cors -> cors.configurationSource(configurationSource()))
		.addFilter(new JwtAuthenticationFilter(authenticationManager(http)))
		.addFilter(new JwtValidationFilter(authenticationManager(http)))
		// Cambiamos la política de sesión a IF_REQUIRED para mayor compatibilidad
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			// Configurando el cookie de sesión explícitamente
			.sessionFixation().migrateSession())
		.csrf(csrf -> csrf.disable())
//		.logout(logout -> logout.logoutUrl("/logout").invalidateHttpSession(true)
//			.deleteCookies("JSESSIONID").logoutSuccessHandler((request, response, authentication) -> {
//			    response.setStatus(HttpServletResponse.SC_OK);
//			    response.getWriter().write("Sesión cerrada con éxito");
//			}))
		.build();
    }

    @Bean
    CorsConfigurationSource configurationSource() {
	CorsConfiguration config = new CorsConfiguration();
	config.setAllowedOrigins(Arrays.asList("https://proyectocarritoantonitrejo.netlify.app",
		"http://localhost:3001", "http://localhost:3000","https://proyecto-carrito-front.onrender.com"));
	config.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "OPTIONS"));
	config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin",
		"Access-Control-Request-Method", "Access-Control-Request-Headers"));
	config.setExposedHeaders(
		Arrays.asList("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "Set-Cookie"));
	config.setAllowCredentials(true);
	config.setMaxAge(3600L); // Cache preflight por 1 hora

	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	source.registerCorsConfiguration("/**", config);
	return source;
    }

    @Bean
    FilterRegistrationBean<CorsFilter> corsfilter() {
	FilterRegistrationBean<CorsFilter> corsbean = new FilterRegistrationBean<>(
		new CorsFilter(this.configurationSource()));
	corsbean.setOrder(Ordered.HIGHEST_PRECEDENCE);
	return corsbean;
    }

    // Opcional: añadir un servlet específico para rutas de pago que asegure
    // SameSite=None
    @Bean
    public FilterRegistrationBean<Filter> sameSiteCookieFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter((ServletRequest request, ServletResponse response, FilterChain chain) -> {
            chain.doFilter(request, response);

            if (response instanceof HttpServletResponse) {
                HttpServletResponse res = (HttpServletResponse) response;
                Collection<String> headers = res.getHeaders("Set-Cookie");
                List<String> modifiedHeaders = headers.stream()
                    .map(header -> header + "; SameSite=None; Secure")
                    .collect(Collectors.toList());

                res.setHeader("Set-Cookie", null);
                for (String header : modifiedHeaders) {
                    res.addHeader("Set-Cookie", header);
                }
            }
        });

        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }
    
    
    @Bean
    public DefaultCookieSerializerCustomizer cookieSerializerCustomizer() {
        return (serializer) -> {
            serializer.setSameSite("None");
            serializer.setUseSecureCookie(true);
            serializer.setUseHttpOnlyCookie(true); // opcional, pero recomendado
            serializer.setCookiePath("/"); // opcional
        };
    }

    


}
