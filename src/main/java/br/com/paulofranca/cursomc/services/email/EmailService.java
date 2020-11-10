package br.com.paulofranca.cursomc.services.email;

import org.springframework.mail.SimpleMailMessage;

import br.com.paulofranca.cursomc.model.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido pedido);
	
	void sendEmail(SimpleMailMessage msg);
}
