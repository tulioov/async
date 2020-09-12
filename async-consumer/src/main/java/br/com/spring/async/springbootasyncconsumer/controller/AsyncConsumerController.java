package br.com.spring.async.springbootasyncconsumer.controller;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.spring.async.springbootasyncconsumer.model.User;
import br.com.spring.async.springbootasyncconsumer.service.AsyncConsumerService;

@RestController
@RequestMapping("/async-list-users")
public class AsyncConsumerController {
	
	private static final Logger logger = LoggerFactory.getLogger(AsyncConsumerController.class);

	@Autowired
	private AsyncConsumerService asyncConsumerService;
	
	@GetMapping
    public ResponseEntity<?> findUsers() throws Throwable {
		
		// Inicia o tempo
		long start = System.currentTimeMillis();
		
		Stream<User>            userStream;
		// Múltiplos Lookups
		CompletableFuture<User> user1 = asyncConsumerService.findUser("teste1");
		CompletableFuture<User> user2 = asyncConsumerService.findUser("teste2");
		CompletableFuture<User> user3 = asyncConsumerService.findUser("teste3");
		CompletableFuture<User> user4 = asyncConsumerService.findUser("teste4");
		
		// Aguarda para que todos terminem
		CompletableFuture.allOf(user1, user2, user3, user4).join();

		// Imprime resultado, incluindo o tempo decorrido
		logger.info("Tempo decorrido := " + (System.currentTimeMillis() - start));
		logger.info("--> " + user1.get());
		logger.info("--> " + user2.get());
		logger.info("--> " + user3.get());
		logger.info("--> " + user4.get());
		
		
        try {
        	//Transforma em Stream
			userStream = Stream.of(user1.get(), user2.get(),user3.get(), user4.get());
			
        } catch (Throwable e) {
			
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error := Erro interno ao consultar dados.");
        
        }
        
        //Retorna em List/Json
        return ResponseEntity.ok(userStream.collect(Collectors.toList()));
    }
}
