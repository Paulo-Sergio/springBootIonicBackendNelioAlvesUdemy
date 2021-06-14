package br.com.paulofranca.cursomc.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.paulofranca.cursomc.dto.ClienteDTO;
import br.com.paulofranca.cursomc.dto.ClienteNewDTO;
import br.com.paulofranca.cursomc.model.Cidade;
import br.com.paulofranca.cursomc.model.Cliente;
import br.com.paulofranca.cursomc.model.Endereco;
import br.com.paulofranca.cursomc.model.enums.Perfil;
import br.com.paulofranca.cursomc.model.enums.TipoCliente;
import br.com.paulofranca.cursomc.repositories.ClienteRepository;
import br.com.paulofranca.cursomc.repositories.EnderecoRepository;
import br.com.paulofranca.cursomc.security.UserSpringSecurity;
import br.com.paulofranca.cursomc.services.exception.AuthorizationException;
import br.com.paulofranca.cursomc.services.exception.DataIntegrityException;
import br.com.paulofranca.cursomc.services.exception.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public Cliente find(Integer id) {
		UserSpringSecurity user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		Optional<Cliente> obj = clienteRepository.findById(id);

		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}

	@Transactional
	public Cliente insert(Cliente cliente) {
		cliente.setId(null);
		cliente = clienteRepository.save(cliente);
		enderecoRepository.saveAll(cliente.getEnderecos());
		return cliente;
	}

	public Cliente update(Cliente cliente) {
		Cliente newObj = this.find(cliente.getId());
		updateData(newObj, cliente);
		return this.clienteRepository.save(newObj);
	}

	public void delete(Integer id) {
		this.find(id);

		try {
			this.clienteRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir porque há pedidos relacionados");
		}
	}

	public List<Cliente> findAll() {
		return clienteRepository.findAll();
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return this.clienteRepository.findAll(pageRequest);
	}

	public Cliente fromDTO(ClienteDTO clienteDto) {
		return new Cliente(clienteDto.getId(), clienteDto.getNome(), clienteDto.getEmail(), null, null, null);
	}

	public Cliente fromDTO(ClienteNewDTO clienteNewDto) {
		Cliente cliente = new Cliente(null, clienteNewDto.getNome(), clienteNewDto.getEmail(), clienteNewDto.getCpfOuCnpj(), TipoCliente.toEnum(clienteNewDto.getTipo()), passwordEncoder.encode(clienteNewDto.getSenha()));
		Cidade cidade = new Cidade(clienteNewDto.getCidadeId(), null, null);
		Endereco endereco = new Endereco(null, clienteNewDto.getLogradouro(), clienteNewDto.getNumero(), clienteNewDto.getComplemento(), clienteNewDto.getBairro(), clienteNewDto.getCep(),
				cliente, cidade);
		cliente.getEnderecos().add(endereco);
		cliente.getTelefones().add(clienteNewDto.getTelefone1());
		if (clienteNewDto.getTelefone2() != null) {
			cliente.getTelefones().add(clienteNewDto.getTelefone2());
		}
		if (clienteNewDto.getTelefone3() != null) {
			cliente.getTelefones().add(clienteNewDto.getTelefone3());
		}
		return cliente;
	}

	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}

}
