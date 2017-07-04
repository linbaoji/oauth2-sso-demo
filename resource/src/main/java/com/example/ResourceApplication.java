package com.example;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.Filter;

import org.apache.catalina.filters.RequestDumperFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@EnableResourceServer
@RestController
public class ResourceApplication extends ResourceServerConfigurerAdapter {
	final List<Message> messages = Collections.synchronizedList(new LinkedList<>());

	public static void main(String[] args) {
		SpringApplication.run(ResourceApplication.class, args);
	}

	@RequestMapping("/user")
	@ResponseBody
	public Principal home(Principal user) {
		return user;
	}

	@RequestMapping(path = "api/messages", method = RequestMethod.GET)
	List<Message> getMessages(Principal principal) {
		return messages;
	}

	@RequestMapping(path = "api/messages", method = RequestMethod.POST)
	Message postMessage(Principal principal, @RequestBody Message message) {
		message.username = principal.getName();
		message.createdAt = LocalDateTime.now();
		messages.add(0, message);
		return message;
	}

	public static class Message {
		public String text;
		public String username;
		public LocalDateTime createdAt;
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER).and().authorizeRequests()
				.antMatchers(HttpMethod.GET, "/api/**").access("#oauth2.hasScope('read')")
				.antMatchers(HttpMethod.POST, "/api/**").access("#oauth2.hasScope('write')");
	}

    @Profile("!cloud")
    @Bean
    RequestDumperFilter requestDumperFilter() {
        return new RequestDumperFilter();
    }

	@Autowired
	private ResourceServerProperties sso;

	@Bean
	public ResourceServerTokenServices myUserInfoTokenServices() {
		return new CustomUserInfoTokenServices(sso.getUserInfoUri(), sso.getClientId());
	}

}
