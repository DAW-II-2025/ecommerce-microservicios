package com.example.pro.services.Impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.pro.Repository.IClienteRepository;
import com.example.pro.model.Cliente;
import com.example.pro.services.IUsuarioServices;


@Service
public class UsuarioServices implements IUsuarioServices, UserDetailsService {
    @Autowired
    private IClienteRepository clienteRepository;

    private Logger logger = LoggerFactory.getLogger(UsuarioServices.class);


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	Optional<Cliente> optional = clienteRepository.findbyCorreo(username);
	if (optional.isEmpty()) {
	    logger.error("Error al iniciar sesion del usuario:" + username);
	    throw new UsernameNotFoundException(username);
	}
	Cliente cli = optional.orElseThrow();
	List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENTE"));
	return new User(cli.getCorreo(), cli.getPassword(), 
		true, true, true, true, authorities);
    }
    
    
    @Override
    public Cliente getUsuarioActual() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // Aquí debes obtener el usuario desde tu repositorio de usuarios
        return clienteRepository.findbyCorreo(username).orElse(null);
    }

}
