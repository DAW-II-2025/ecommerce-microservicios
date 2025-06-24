package pe.edu.cibertec.repository.producto;

import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>{
}
