package net.marcoreis.ecommerce.controlador;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import net.marcoreis.ecommerce.entidades.Cliente;
import net.marcoreis.ecommerce.negocio.ClienteService;

@SessionScoped
@ManagedBean
public class LoginBean extends BaseBean {
	private static final long serialVersionUID = 4169068378414919948L;
	protected static final Logger logger = Logger
			.getLogger(LoginBean.class);
	private boolean loggedIn;
	private ClienteService clienteService = new ClienteService();

	public String login() {
		cliente = clienteService
				.carregarCliente(getCliente().getEmail());
		if (cliente != null) {
			setCliente(cliente);
			setLoggedIn(true);
			return "inicio";
		} else {
			setLoggedIn(false);
			errorMsg("Usuário inválido");
			return null;
		}
	}

	@PostConstruct
	public void init() {
		setCliente(new Cliente());
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

}
