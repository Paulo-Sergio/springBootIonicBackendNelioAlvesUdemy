package br.com.paulofranca.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.paulofranca.cursomc.dto.CategoriaDTO;
import br.com.paulofranca.cursomc.model.Categoria;
import br.com.paulofranca.cursomc.repositories.CategoriaRepository;
import br.com.paulofranca.cursomc.services.exception.DataIntegrityException;
import br.com.paulofranca.cursomc.services.exception.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository categoriaRepository;

	public Categoria find(Integer id) {
		Optional<Categoria> categoria = categoriaRepository.findById(id);

		return categoria.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}

	public Categoria insert(Categoria categoria) {
		categoria.setId(null);
		return this.categoriaRepository.save(categoria);
	}

	public Categoria update(Categoria categoria) {
		Categoria newObj = this.find(categoria.getId());
		updateData(newObj, categoria);
		return this.categoriaRepository.save(newObj);
	}

	public void delete(Integer id) {
		this.find(id);

		try {
			this.categoriaRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir uma categoria que possui produtos");
		}
	}

	public List<Categoria> findAll() {
		return this.categoriaRepository.findAll();
	}

	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return this.categoriaRepository.findAll(pageRequest);
	}

	public Categoria fromDTO(CategoriaDTO objDto) {
		return new Categoria(objDto.getId(), objDto.getNome());
	}

	private void updateData(Categoria newObj, Categoria categoria) {
		newObj.setNome(categoria.getNome());
	}
}
