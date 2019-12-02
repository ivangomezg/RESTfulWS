package org.me.utm.service;

import java.util.List;

import org.me.utm.model.User;
import org.me.utm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public List<User> getUsers() {
		return userRepository.findAll();
	}

	@Override
	public User getUser(long id) {
		return userRepository.findOne(id);
	}

	@Override
	public User createUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public void deleteUser(long id) {
		userRepository.delete(id);
	}

	@Override
	public User updateUser(User user) {
		return userRepository.save(user);
	}

}
