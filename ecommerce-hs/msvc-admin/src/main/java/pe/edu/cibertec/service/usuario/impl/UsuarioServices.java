package pe.edu.cibertec.service.usuario.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pe.edu.cibertec.model.usuario.Usuario;
import pe.edu.cibertec.repository.usuario.IUsuarioRepository;

@Service
public class UsuarioServices implements UserDetailsService {

	@Autowired
	private IUsuarioRepository repository;

	private Logger logger = LoggerFactory.getLogger(UsuarioServices.class);

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> optional = repository.findByUsername(username);
		if (optional.isEmpty()) {
			logger.error("Error al iniciar sesion del usuario:" + username);
			throw new UsernameNotFoundException(username);
		}
		Usuario usu = optional.orElseThrow();
		List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
		return new User(usu.getUsername(), usu.getPassword(), true, true, true, true, authorities);
	}

}
