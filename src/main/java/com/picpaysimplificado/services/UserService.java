package com.picpaysimplificado.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.domain.user.UserType;
import com.picpaysimplificado.repositories.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository repository;

	public void validateTransaction(User sender, BigDecimal amount) throws Exception {
		if(sender.getUserType() == UserType.MERCHANT) {
			throw new Exception("Usuário Lojista não está autorizado para fazer transação");
		}
		
		if(sender.getBalance().compareTo(amount) < 0) {
			throw new Exception("Saldo insuficiente");
		}
	}
}

