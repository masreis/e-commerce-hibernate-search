package net.marcoreis.ecommerce.controlador;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.persistence.EntityManager;

import net.marcoreis.ecommerce.entidades.Cliente;
import net.marcoreis.ecommerce.negocio.ClienteService;
import net.marcoreis.ecommerce.util.JPAUtil;

@ViewScoped
@ManagedBean
public class ClienteBean extends BaseBean {
	private static final long serialVersionUID = -2658024901938874346L;
	private Collection<Cliente> clientes;
	private ClienteService clienteService = new ClienteService();

	@PostConstruct
	public void init() {
		carregarClientes();
	}

	public void salvar() {
	}

	public void carregarClientes() {
		clientes = clienteService.carregarColecao(Cliente.class);
	}

	public Collection<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(Collection<Cliente> clientes) {
		this.clientes = clientes;
	}

}
