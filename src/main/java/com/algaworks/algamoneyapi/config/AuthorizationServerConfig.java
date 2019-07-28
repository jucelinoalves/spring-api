package com.algaworks.algamoneyapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter  {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
				.withClient("angular")
				.secret("$2a$10$1rkmuSWK2j2SsnBNJK5AB.M//S11MIB7biZEdnVOPYb2SX2ucmity")
				.scopes("read","write")
				.authorizedGrantTypes("password","refresh_token")			
				.accessTokenValiditySeconds(1800)
				.accessTokenValiditySeconds(3600 * 24)
			.and()
				.withClient("mobile")
				.secret("$2a$10$1rkmuSWK2j2SsnBNJK5AB.M//S11MIB7biZEdnVOPYb2SX2ucmity")
				.scopes("read")
				.authorizedGrantTypes("password","refresh_token")			
				.accessTokenValiditySeconds(1800)
				.accessTokenValiditySeconds(3600 * 24);
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
			.tokenStore(tokenStore())
			.accessTokenConverter(accessTokenConverter())
			.reuseRefreshTokens(false)
			.userDetailsService(userDetailsService)
			.authenticationManager(authenticationManager);
	
	}
	
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverterLocal = new JwtAccessTokenConverter();
		accessTokenConverterLocal.setSigningKey("algaworks");
		return accessTokenConverterLocal;
	}

	@Bean
	public TokenStore tokenStore() {		
		return new JwtTokenStore(accessTokenConverter());
	}
}
