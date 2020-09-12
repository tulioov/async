package br.com.spring.async.springbootasyncprovider.service;

import org.springframework.stereotype.Service;

import br.com.spring.async.springbootasyncprovider.model.User;

@Service
public class ProviderService {

    public User findUser(String user) throws InterruptedException {
        User usuario = new User();
        usuario.setName(user);
        return usuario;
    }
}
