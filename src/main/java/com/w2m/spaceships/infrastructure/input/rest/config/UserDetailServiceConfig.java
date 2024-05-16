package com.w2m.spaceships.infrastructure.input.rest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@PropertySource("classpath:application.properties")
public class UserDetailServiceConfig {

  @Value("${user.default.username}")
  private String defaultUsername;

  @Value("${user.default.password}")
  private String defaultPassword;

  @Value("${user.default.roles}")
  private String[] defaultRoles;

  @Value("${user.admin.username}")
  private String adminUsername;

  @Value("${user.admin.password}")
  private String adminPassword;

  @Value("${user.admin.roles}")
  private String[] adminRoles;

  @Bean
  public UserDetailsService userDetailsService(final BCryptPasswordEncoder bCryptPasswordEncoder) {
    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    manager.createUser(
        User.withUsername(defaultUsername)
            .password(bCryptPasswordEncoder.encode(defaultPassword))
            .roles(defaultRoles)
            .build());
    manager.createUser(
        User.withUsername(adminUsername)
            .password(bCryptPasswordEncoder.encode(adminPassword))
            .roles(adminRoles)
            .build());
    return manager;
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
