package br.com.paulofranca.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.paulofranca.cursomc.model.Categoria;
import br.com.paulofranca.cursomc.repositories.CategoriaRepository;
import br.com.paulofranca.cursomc.services.exception.DataIntegrityException;
import br.com.paulofranca.cursomc.services.exception.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repository;

	public Categoria find(Integer id) {
		Optional<Categoria> categoria = repository.findById(id);

		return categoria.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}

	public Categoria insert(Categoria categoria) {
		categoria.setId(null);
		return this.repository.save(categoria);
	}

	public Categoria update(Categoria categoria) {
		this.find(categoria.getId());
		return this.repository.save(categoria);
	}

	public void delete(Integer id) {
		this.find(id);
		
		try {
			this.repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir uma categoria que possui produtos");
		}
	}
}
