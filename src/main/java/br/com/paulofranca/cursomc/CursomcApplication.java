package br.com.paulofranca.cursomc;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.paulofranca.cursomc.model.Categoria;
import br.com.paulofranca.cursomc.model.Cidade;
import br.com.paulofranca.cursomc.model.Cliente;
import br.com.paulofranca.cursomc.model.Endereco;
import br.com.paulofranca.cursomc.model.Estado;
import br.com.paulofranca.cursomc.model.ItemPedido;
import br.com.paulofranca.cursomc.model.Pagamento;
import br.com.paulofranca.cursomc.model.PagamentoComBoleto;
import br.com.paulofranca.cursomc.model.PagamentoComCartao;
import br.com.paulofranca.cursomc.model.Pedido;
import br.com.paulofranca.cursomc.model.Produto;
import br.com.paulofranca.cursomc.model.enums.EstadoPagamento;
import br.com.paulofranca.cursomc.model.enums.TipoCliente;
import br.com.paulofranca.cursomc.repositories.CategoriaRepository;
import br.com.paulofranca.cursomc.repositories.CidadeRepository;
import br.com.paulofranca.cursomc.repositories.ClienteRepository;
import br.com.paulofranca.cursomc.repositories.EnderecoRepository;
import br.com.paulofranca.cursomc.repositories.EstadoRepository;
import br.com.paulofranca.cursomc.repositories.ItemPedidoRepository;
import br.com.paulofranca.cursomc.repositories.PagamentoRepository;
import br.com.paulofranca.cursomc.repositories.PedidoRepository;
import br.com.paulofranca.cursomc.repositories.ProdutoRepository;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	@Override
	public void run(String... args) throws Exception {
		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritório");

		Produto p1 = new Produto(null, "Computador", 2000.00);
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null, "Mouse", 80.00);

		// adicionando produto(s) a sua respectiva categoria(s)
		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));

		// adicionando categoria(s) a seus respectivos produto(s)
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));

		System.out.println("------- Adicionando Categorias e Produtos --------------");
		this.categoriaRepository.saveAll(Arrays.asList(cat1, cat2));
		this.produtoRepository.saveAll(Arrays.asList(p1, p2, p3));
		System.out.println("----------- FIM --------------");

		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");

		Cidade c1 = new Cidade(null, "Uberlândia", est1);
		Cidade c2 = new Cidade(null, "São Paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est2);

		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c2, c3));

		System.out.println("------- Adicionando Estados e Cidades --------------");
		this.estadoRepository.saveAll(Arrays.asList(est1, est2));
		this.cidadeRepository.saveAll(Arrays.asList(c1, c2, c3));
		System.out.println("----------- FIM --------------");

		Cliente cli1 = new Cliente(null, "Maria Silva", "maria@gmail.com", "36378912377", TipoCliente.PESSOAFISICA);

		cli1.getTelefones().addAll(Arrays.asList("27363323", "93838393"));

		Endereco e1 = new Endereco(null, "Rua Flores", "300", "Apto 303", "Jardim", "38220834", cli1, c1);
		Endereco e2 = new Endereco(null, "Avenida Matos", "105", "Sala 800", "Centro", "38777012", cli1, c2);

		cli1.getEnderecos().addAll(Arrays.asList(e1, e2));

		System.out.println("------- Adicionando Clientes e Endereços --------------");
		this.clienteRepository.saveAll(Arrays.asList(cli1));
		this.enderecoRepository.saveAll(Arrays.asList(e1, e2));
		System.out.println("----------- FIM --------------");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Pedido ped1 = new Pedido(null, sdf.parse("30/09/2017 10:32"), cli1, e1);
		Pedido ped2 = new Pedido(null, sdf.parse("10/10/2017 19:35"), cli1, e2);

		Pagamento pagto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
		ped1.setPagamento(pagto1);

		Pagamento pagto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("20/10/2017 00:00"), null);
		ped2.setPagamento(pagto2);

		cli1.getPedidos().addAll(Arrays.asList(ped1, ped2));
		
		System.out.println("------- Adicionando Pedidos e Pagamentos --------------");
		this.pedidoRepository.saveAll(Arrays.asList(ped1, ped2));
		this.pagamentoRepository.saveAll(Arrays.asList(pagto1, pagto2));
		System.out.println("----------- FIM --------------");
		
		ItemPedido ip1 = new ItemPedido(ped1, p1, 0.00, 1, 2000.00);
		ItemPedido ip2 = new ItemPedido(ped1, p3, 0.00, 2, 80.00);
		ItemPedido ip3 = new ItemPedido(ped2, p2, 100.00, 1, 800.00);
		
		ped1.getItens().addAll(Arrays.asList(ip1, ip2));
		ped2.getItens().addAll(Arrays.asList(ip3));
		
		p1.getItens().addAll(Arrays.asList(ip1));
		p2.getItens().addAll(Arrays.asList(ip3));
		p3.getItens().addAll(Arrays.asList(ip2));
		
		this.itemPedidoRepository.saveAll(Arrays.asList(ip1, ip2, ip3));
	}
}