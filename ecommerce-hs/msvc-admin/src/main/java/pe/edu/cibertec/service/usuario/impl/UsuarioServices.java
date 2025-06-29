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
		logger.info("Intentando autenticar usuario: {}", username);

		try {
			Optional<Usuario> optional = repository.findByUsername(username);

			if (optional.isEmpty()) {
				logger.warn("Usuario no encontrado en la base de datos: {}", username);
				throw new UsernameNotFoundException("Usuario no encontrado: " + username);
			}

			Usuario usuario = optional.get();
			logger.info("Usuario encontrado: {}", usuario.getUsername());

			// Verificar que la contraseña no sea null o vacía
			if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
				logger.error("La contraseña del usuario {} está vacía o es null", username);
				throw new UsernameNotFoundException("Contraseña inválida para el usuario: " + username);
			}

			// Log de la contraseña encriptada (solo para debugging - quitar en producción)
			logger.debug("Contraseña encriptada encontrada para {}: {}", username,
					usuario.getPassword().substring(0, Math.min(10, usuario.getPassword().length())) + "...");

			// Crear authorities - puedes hacer esto dinámico basado en el rol del usuario
			List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
			logger.info("Authorities asignadas al usuario {}: {}", username, authorities);

			UserDetails userDetails = new User(
					usuario.getUsername(),
					usuario.getPassword(),
					true,  // enabled
					true,  // accountNonExpired
					true,  // credentialsNonExpired
					true,  // accountNonLocked
					authorities
			);

			logger.info("UserDetails creado exitosamente para usuario: {}", username);
			return userDetails;

		} catch (UsernameNotFoundException e) {
			// Re-lanzar las excepciones de usuario no encontrado
			throw e;
		} catch (Exception e) {
			logger.error("Error inesperado al cargar usuario {}: {}", username, e.getMessage(), e);
			throw new UsernameNotFoundException("Error al cargar el usuario: " + username, e);
		}
	}
}