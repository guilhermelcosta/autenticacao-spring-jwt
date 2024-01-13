package org.spring.autenticacaojwt.config;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.spring.autenticacaojwt.componentes.JWTComp;
import org.spring.autenticacaojwt.seguranca.JWTFiltroAutenticacao;
import org.spring.autenticacaojwt.seguranca.JWTFiltroAutorizacao;
import org.spring.autenticacaojwt.seguranca.UsuarioDetailsService;
import org.spring.autenticacaojwt.service.PasswordEncoderImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.spring.autenticacaojwt.util.ConstantesUtil.*;

@Slf4j(topic = "SEGURANCA_CONFIG")
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SegurancaConfig {

    private final UsuarioDetailsService usuarioDetailsService;
    private final JWTComp jwtComp;

    public SegurancaConfig(UsuarioDetailsService usuarioDetailsService, JWTComp jwtComp) {
        this.usuarioDetailsService = usuarioDetailsService;
        this.jwtComp = jwtComp;
    }

    /**
     * Cria camada de segurança Filter Chain
     *
     * @param httpSecurity configuração para papéis de usuário
     * @return configuração do httpSecurity
     * @throws Exception lança exceção caso haja problema na configuração
     */
    @Bean
    public SecurityFilterChain filterChain(@NotNull HttpSecurity httpSecurity) throws Exception {
        log.info(">>> filterChain: iniciando camada de segurança Filter Chain");
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(usuarioDetailsService).passwordEncoder(new PasswordEncoderImpl());
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationManager(authenticationManager)
                .addFilter(new JWTFiltroAutenticacao(authenticationManager, this.jwtComp))
                .addFilter(new JWTFiltroAutorizacao(authenticationManager, this.jwtComp, this.usuarioDetailsService))
                .authorizeHttpRequests(request -> {
                    request.requestMatchers(CAMINHOS_PUBLICOS).permitAll();
                    request.requestMatchers(HttpMethod.POST, CAMINHOS_PUBLICOS_POST).permitAll();
                    request.anyRequest().authenticated();
                })
                .build();
    }

    /**
     * Configuração de cors
     *
     * @return configuração de cors
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        log.info(">>> corsConfigurationSource: iniciando configuração de Cors");
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.setAllowedMethods(METODOS_PERMITIDOS_CORS);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
