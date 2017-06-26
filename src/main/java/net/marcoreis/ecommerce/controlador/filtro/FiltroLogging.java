package net.marcoreis.ecommerce.controlador.filtro;

import java.io.IOException;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

//@WebFilter
public class FiltroLogging implements Serializable, Filter {

	private static final long serialVersionUID = 1472782644963167647L;
	private static Logger LOGGER = Logger.getLogger("historico");

	@Override
	public void doFilter(ServletRequest request,
			ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		// Run the other filters.
		filterChain.doFilter(request, response);

		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			// String uri = httpServletRequest.getRequestURI();
			// String regex = "((/{1}\\w+$)|(/{1}\\w+\\.faces$))";
			// Pattern p = Pattern.compile(regex);
			// Matcher m = p.matcher(uri);
			// while (m.find()) {
			// LOGGER.info("match " + m.group());
			// break;
			// }
			// LOGGER.info(message);
		}

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}
}