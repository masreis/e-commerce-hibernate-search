package net.marcoreis.ecommerce.controlador.filtro;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import net.marcoreis.ecommerce.controlador.LoginBean;
import net.marcoreis.ecommerce.entidades.Cliente;

@WebFilter(filterName = "FiltroAcesso", urlPatterns = "/paginas/*")
public class FiltroAcesso implements Filter {
	private static Logger logger = Logger.getLogger("historico");

	@Override
	public void init(FilterConfig filterConfig)
			throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request,
			ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String reqURI = httpRequest.getRequestURI();
		boolean isLoggedIn = isLoggedIn(httpRequest);
		//
		// Registra que página o usuário acessou
		if (reqURI.indexOf("/paginas/") >= 0 && isLoggedIn) {
			logger.info(getUsuarioSessao(httpRequest).getEmail()
					+ " " + reqURI);
		}
		//
		if (reqURI.indexOf("/login.faces") >= 0
				|| reqURI.contains("javax.faces.resource")
				|| isLoggedIn) {
			chain.doFilter(request, response);
		} else {
			httpResponse.sendRedirect(
					request.getServletContext().getContextPath()
							+ "/paginas/login.faces");
		}

	}

	private boolean isLoggedIn(HttpServletRequest request) {
		try {
			HttpSession session = ((HttpServletRequest) request)
					.getSession(false);
			LoginBean bean = (LoginBean) session
					.getAttribute("loginBean");
			return bean.isLoggedIn();
		} catch (Exception e) {
			return false;
		}
	}

	private Cliente getUsuarioSessao(
			HttpServletRequest request) {
		HttpSession session = ((HttpServletRequest) request)
				.getSession(false);
		LoginBean bean = (LoginBean) session
				.getAttribute("loginBean");
		return bean.getCliente();
	}

	@Override
	public void destroy() {

	}

}
