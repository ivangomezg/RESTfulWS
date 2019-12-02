package org.me.utm.service;

import java.util.List;

import org.me.utm.model.User;

public interface UserService {
	
	public List<User> getUsers();
	public User getUser(long id);
	public User createUser(User user);
	public void deleteUser(long id);
	public User updateUser(User user);

}
