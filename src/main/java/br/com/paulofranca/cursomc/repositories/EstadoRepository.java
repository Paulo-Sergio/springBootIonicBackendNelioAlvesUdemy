package br.com.paulofranca.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.paulofranca.cursomc.model.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer> {

}
