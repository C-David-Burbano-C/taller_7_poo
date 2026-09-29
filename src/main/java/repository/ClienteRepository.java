package repository;

import com.mycompany.sistema.reservas.dominio.modelo.Cliente;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author daferarte
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
}
