package com.picpaysimplificado.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.picpaysimplificado.domain.transaction.Transaction;
import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.repositories.TransactionRepository;

@Service
public class TransactionService {
	@Autowired
	private UserService userService;
	@Autowired
	private TransactionRepository repository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private NotificationServices notificationServices;
	
	public Transaction createTransaction(TransactionDTO transaction) throws Exception {
		User sender = this.userService.findUserById(transaction.senderId());
		User receiver = this.userService.findUserById(transaction.receiverId());
		
		userService.validateTransaction(sender, transaction.Value());
		
		boolean isAuthorized = this.authorizeTransaction(sender, transaction.Value());
		if(!isAuthorized) {
			throw new Exception("Transação não autorizada");
		}
		
		Transaction newtransaction = new Transaction();
		newtransaction.setAmount(transaction.Value());
		newtransaction.setSender(sender);
		newtransaction.setReceiver(receiver);
		newtransaction.setTimestamp(LocalDateTime.now());
		
		
		sender.setBalance(sender.getBalance().subtract(transaction.Value()));
		receiver.setBalance(receiver.getBalance().add(transaction.Value()));
		
		this.repository.save(newtransaction);
		this.userService.saveUser(receiver);
		this.userService.saveUser(sender);
		
		this.notificationServices.sendNotification(sender, "Transação realizada com sucesso");
		this.notificationServices.sendNotification(receiver, "Transação recebida com sucesso");
		
		return newtransaction;
		}
	
	
	public boolean authorizeTransaction(User sender, BigDecimal value) {
		ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity("https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc", Map.class);
		if(authorizationResponse.getStatusCode() == HttpStatus.OK) {
			String message =  (String) authorizationResponse.getBody().get("message");
			return "Autorizado".equalsIgnoreCase(message);
		}return false;
	}
}
	

