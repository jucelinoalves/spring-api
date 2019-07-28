package com.algaworks.algamoneyapi.cors;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.algaworks.algamoneyapi.config.property.ApiProperty;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class CorsFilter implements Filter {

	@Autowired
	private ApiProperty apiProperty;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest requisicao = (HttpServletRequest) request;
		HttpServletResponse resposta = (HttpServletResponse) response;
		
		resposta.setHeader("Access-Control-Allow-Origin", apiProperty.getOriginPermitida());
		resposta.setHeader("Access-Control-Allow-Credentials","true");
		
		if ("OPTIONS".equals(requisicao.getMethod()) && apiProperty.getOriginPermitida().equals(requisicao.getHeader("Origin"))) {
			resposta.setHeader("Access-Control-Allow-Methods","POST, GET, DELETE, PUT, OPTIONS");
			resposta.setHeader("Access-Control-Allow-Headers","Authorization, Content-Type, Accept");
			resposta.setHeader("Access-Control-Max-Age","3600");
			
			resposta.setStatus(HttpServletResponse.SC_OK);
		} else {
			chain.doFilter(requisicao, resposta);
		}
	}

}
