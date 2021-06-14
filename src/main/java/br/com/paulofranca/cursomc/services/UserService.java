package br.com.paulofranca.cursomc.services;

import org.springframework.security.core.context.SecurityContextHolder;

import br.com.paulofranca.cursomc.security.UserSpringSecurity;

public class UserService {

	public static UserSpringSecurity authenticated() {
		try {
			return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		catch (Exception e) {
			return null;
		}
	}
}
