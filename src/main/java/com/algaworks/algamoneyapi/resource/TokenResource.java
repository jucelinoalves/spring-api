package com.algaworks.algamoneyapi.resource;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algamoneyapi.config.property.ApiProperty;
import com.algaworks.algamoneyapi.config.property.ApiProperty.Seguranca;

@RestController
@RequestMapping("/tokens")
public class TokenResource {
	
	@Autowired
	private ApiProperty apiProperty;
	
	@DeleteMapping("/revoke")
	public void revoke(HttpServletRequest request,HttpServletResponse response) {
		Cookie cookie = new Cookie("refreshToken",null);
		cookie.setHttpOnly(apiProperty.getSeguranca().isEnableHttps());
		cookie.setSecure(false);
		cookie.setPath(request.getContextPath() + "/oauth/token");
		cookie.setMaxAge(0);
		
		response.addCookie(cookie);
		response.setStatus(HttpStatus.NO_CONTENT.value());
	}

}
