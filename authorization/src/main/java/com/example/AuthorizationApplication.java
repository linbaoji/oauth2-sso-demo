package com.example;

import org.apache.catalina.filters.RequestDumperFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableAuthorizationServer
@RestController
public class AuthorizationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationApplication.class, args);
	}

	@Configuration
	static class MvcConfig extends WebMvcConfigurerAdapter {
		@Override
		public void addViewControllers(ViewControllerRegistry registry) {
			// registry.addViewController("login").setViewName("login");
			registry.addViewController("/").setViewName("index");
		}
	}

	@Configuration
	@Order(-20)
	static class LoginConfig extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			/*http.antMatcher("/**").authorizeRequests().antMatchers("/", "/login**", "/webjars/**").permitAll().anyRequest()
			.authenticated().and().formLogin().loginPage("/login").permitAll().and().requestMatchers()
					.antMatchers("/", "/login", "/oauth/authorize", "/oauth/confirm_access").and().authorizeRequests()
					.anyRequest().authenticated().and().csrf()
					.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
			*/
//		       http
//               .formLogin().loginPage("/login").permitAll()
//               .and()
//               .requestMatchers()
//               .antMatchers("/", "/login**", "/oauth/authorize", "/oauth/confirm_access")
//               .and()
//               .authorizeRequests()
//               .anyRequest().authenticated();
//		       

		      
				 
				 
				 http // "/**" 全部要求权限    “/login**” 和 “/webjars/**” 可自由访问
				 .antMatcher("/**").authorizeRequests().antMatchers("/login**", "/webjars/**").permitAll().anyRequest().authenticated()
				 	//.and()// 跨域请求攻击 ；详细http://www.jianshu.com/p/672b6390c25f
				 		//.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
					//.and()// 当验证失败时 要调到的页面。。默认跳“/login”
						//.exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login?error"))
					.and()// 登录的地址
						.formLogin().loginPage("/login").permitAll()//登入
					//.and()// 控制登出后的跳转
					//	.logout().logoutSuccessUrl("/login?logout").permitAll()
					.and()
						.rememberMe()// 记住密码( rememberMe)
						.key("czbs-remeberMe")
					.and()
						.sessionManagement()//关于SESSION 的配置
			       	 	.maximumSessions(1)
			       	 	.expiredUrl("/login?expired")
//					
//					
					;
		}
	}

	@Bean
	 public FilterRegistrationBean testFilterRegistration() {
	 
	   FilterRegistrationBean registration = new FilterRegistrationBean();
	   registration.setFilter(new MemberLoginFilter());
	   registration.addUrlPatterns("/oauth/authorize");	 
	   registration.setName("memberLoginFilter");
	   registration.setOrder(-100);
	   return registration;
	 }
	
	
	@Profile("!cloud")
	@Bean
	RequestDumperFilter requestDumperFilter() {
		return new RequestDumperFilter();
	}


}
