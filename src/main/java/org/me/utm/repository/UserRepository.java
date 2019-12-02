package org.me.utm.repository;

import java.util.List;

import org.me.utm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	List<User> findByFirstName(String firstName);
	List<User> findByLastName(String lastName);
	List<User> findByAge(int age);

}
