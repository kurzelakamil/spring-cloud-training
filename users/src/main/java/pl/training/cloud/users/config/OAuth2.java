package pl.training.cloud.users.config;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import pl.training.cloud.users.service.UsersService;

@Configuration
public class OAuth2 extends AuthorizationServerConfigurerAdapter {

    private static final String CLIENT_ID = "training";
    private static final String GRANT_TYPE = "password";
    private static final String SCOPE = "api";

    @Autowired
    @Setter
    private TokenStore tokenStore;
    @Autowired
    @Setter
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    @Autowired
    @Setter
    private AuthenticationManager authenticationManagerBean;
    @Autowired
    @Setter
    private UsersService usersService;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(tokenStore)
                .accessTokenConverter(jwtAccessTokenConverter)
                .authenticationManager(authenticationManagerBean)
                .userDetailsService(usersService);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(CLIENT_ID)
                .authorizedGrantTypes(GRANT_TYPE)
                .scopes(SCOPE);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.allowFormAuthenticationForClients();
    }

}
