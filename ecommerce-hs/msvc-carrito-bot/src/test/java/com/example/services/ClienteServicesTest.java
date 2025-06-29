package com.example.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.example.pro.Repository.IClienteRepository;
import com.example.pro.model.Cliente;
import com.example.pro.services.Impl.ClienteServices;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class ClienteServicesTest {
	@Mock
    private IClienteRepository clienteRepository;
	
	@Mock
	private PasswordEncoder encoder;

    @InjectMocks
    private ClienteServices clienteServices;

    //Regresa clientes del repository
    @Test
    public void testGetAllClientes() {

        Cliente cliente1 = new Cliente();
        cliente1.setNombres("Juan");
        Cliente cliente2 = new Cliente();
        cliente2.setNombres("Ana");

        List<Cliente> listaM = Arrays.asList(cliente1, cliente2);
        when(clienteRepository.findAll()).thenReturn(listaM);

        List<Cliente> resultado = clienteServices.GetAllClientes();
        assertNotNull(resultado);                         
        assertEquals(2, resultado.size());               
        assertEquals("Juan", resultado.get(0).getNombres());  
        verify(clienteRepository, times(1)).findAll();    
    }
    
    // Usa PasswordEnconder correctamente
    @Test
    public void testSaveCliente() {

        Cliente cliente = new Cliente();
        cliente.setPassword("123");
        when(encoder.encode("123")).thenReturn("Camaleon");

        Cliente clienteGuardado = new Cliente();
        clienteGuardado.setPassword("Camaleon");
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteGuardado);

        Cliente resultado = clienteServices.SaveCliente(cliente);
        assertNotNull(resultado);
        assertEquals("Camaleon", resultado.getPassword());
        verify(encoder, times(1)).encode("123");
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }
    
    // Cliente existe
    @Test
    public void testUpdateCliente_Existe() {
 
        Integer id = 1;
        
        Cliente clienteI = new Cliente();
        clienteI.setNombres("Juan");
        clienteI.setApellidos("Perez");
        clienteI.setDireccion("Av. Lima");
        clienteI.setFechaNacimiento("1990-01-01");
        clienteI.setSexo('M');
        clienteI.setCorreo("juan@gmail.com");
        clienteI.setPassword("123");
        clienteI.setEstado('A');

        Cliente clienteBD = new Cliente();
        clienteBD.setPassword("oldpass");
        when(clienteRepository.findById(id)).thenReturn(Optional.of(clienteBD));
        when(encoder.encode("123")).thenReturn("Camaleon");
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteBD);

        Integer resultado = clienteServices.updateCliente(id, clienteI);
        assertEquals(1, resultado);
        assertEquals("Camaleon", clienteBD.getPassword());
        verify(clienteRepository, times(1)).findById(id);
        verify(clienteRepository, times(1)).save(clienteBD);
        verify(encoder, times(1)).encode("123");
    }
    
    //Cliente no existe
    @Test
    public void testUpdateCliente_NoExiste() {
  
        Integer id = 999;
        
        Cliente clienteInput = new Cliente();
        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        Integer resultado = clienteServices.updateCliente(id, clienteInput);
        assertEquals(0, resultado);
        verify(clienteRepository, never()).save(any());
        verify(encoder, never()).encode(anyString());
    }
    
    // Encuentra Cliente
    @Test
    public void testFindClienteById_Existe() {
    	
        int id = 1;
        
        Cliente clienteE = new Cliente();
        clienteE.setNombres("Ana");
        when(clienteRepository.findById(id)).thenReturn(Optional.of(clienteE));

        Cliente resultado = clienteServices.FindClienteById(id);
        assertNotNull(resultado);
        assertEquals("Ana", resultado.getNombres());
        verify(clienteRepository, times(1)).findById(id);
    }
    
    // No Encuentra Cliente
    @Test
    public void testFindClienteById_NoExiste() {
    	
        int id = 999;
        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        Cliente resultado = clienteServices.FindClienteById(id);
        assertNotNull(resultado);  
        assertNull(resultado.getNombres());
        verify(clienteRepository, times(1)).findById(id);
    }
    
    // Credenciales correcta
    @Test
    public void testVerificarCliente_CredencialesC() {
    	
        String correo = "cliente@gmail.com";
        String pass = "123";

        Cliente cliente = new Cliente();
        cliente.setCorreo(correo);
        cliente.setPassword(pass); 
        when(clienteRepository.findbyCorreo(correo)).thenReturn(Optional.of(cliente));

        Cliente resultado = clienteServices.VerificarCliente(correo, pass);
        assertNotNull(resultado);
        assertEquals(correo, resultado.getCorreo());
    }
    
    // Credenciales incorrectas
    @Test
    public void testVerificarCliente_CredencialesI() {
    	
        String correo = "cliente@gmail.com";
        String pass= "321";

        Cliente cliente = new Cliente();
        cliente.setCorreo(correo);
        cliente.setPassword("123");
        when(clienteRepository.findbyCorreo(correo)).thenReturn(Optional.of(cliente));

        Cliente resultado = clienteServices.VerificarCliente(correo, pass);
        assertNull(resultado);
    }
    
    // Credenciales inexistentes
    @Test
    public void testVerificarCliente_CredencialesIn() {
    	
        String correo = "nocliente@gmail.com";
        when(clienteRepository.findbyCorreo(correo)).thenReturn(Optional.empty());

        Cliente resultado = clienteServices.VerificarCliente(correo, "123");
        assertNull(resultado);
    }
    
    
    // Cliente con ese DNI existe
    @Test
    public void testGetByDni_Existe() {
    	
        String dni = "73094628";
        
        Cliente cliente = new Cliente();
        cliente.setNombres("Luis");
        cliente.setDni(dni);
        when(clienteRepository.findByDni(dni)).thenReturn(Optional.of(cliente));

        Cliente resultado = clienteServices.getByDni(dni);
        assertNotNull(resultado);
        assertEquals("Luis", resultado.getNombres());
        verify(clienteRepository, times(1)).findByDni(dni);
    }
    
    // Cliente con ese DNI NO existe
    @Test
    public void testGetByDni_NoExiste() {
    	
        String dni = "79851462";
        when(clienteRepository.findByDni(dni)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> {
            clienteServices.getByDni(dni);
        });
        verify(clienteRepository, times(1)).findByDni(dni);
    }
    
    // Cliente con ese correo existe
    @Test
    public void testVerificarCorreo_Existe() {
    	
        String correo = "cliente@gmail.com";
        
        Cliente cliente = new Cliente();
        cliente.setCorreo(correo);
        when(clienteRepository.findbyCorreo(correo)).thenReturn(Optional.of(cliente));

        Optional<Cliente> resultado = clienteServices.verificarCorreo(correo);
        assertTrue(resultado.isPresent());
        assertEquals(correo, resultado.get().getCorreo());
        verify(clienteRepository, times(1)).findbyCorreo(correo);
    }
    
    // Cliente con ese correo NO existe
    @Test
    public void testVerificarCorreo_NoExiste() {
    	
        String correo = "nocliente@gmail.com";
        when(clienteRepository.findbyCorreo(correo)).thenReturn(Optional.empty());

        Optional<Cliente> resultado = clienteServices.verificarCorreo(correo);
        assertFalse(resultado.isPresent());
        verify(clienteRepository, times(1)).findbyCorreo(correo);
    }
    
}
