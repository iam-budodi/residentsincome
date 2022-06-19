package com.japhet.application.residentsincome.repository;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.japhet.application.residentsincome.model.User;

@RequestScoped
public class UserListProducer {
	
	@Inject
	private UserRepository userRepository;
	
	private List<User> users;
	
	@Produces
	@Named
	public List<User> getUsers() {
		return users;
	}
	
	public void onUserListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final User user) {
		retrieveAllUsers();
	}

	@PostConstruct
	private void retrieveAllUsers() {
		users = userRepository.findAllOrderedByName();
		
	}
}
