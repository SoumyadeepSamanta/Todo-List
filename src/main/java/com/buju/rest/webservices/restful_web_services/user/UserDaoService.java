package com.buju.rest.webservices.restful_web_services.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;

@Component
public class UserDaoService {
	
	private static List<User> users = new ArrayList();
	
	private static int usersCount = 0;
	
	static {
		
		users.add(new User(++usersCount,"Buju",LocalDate.now().minusYears(20)));
		users.add(new User(++usersCount,"Sammy",LocalDate.now().minusYears(25)));
		users.add(new User(++usersCount,"Adam",LocalDate.now().minusYears(30)));
		
	}
	
	public List<User> findAll() {
		
		return users;
	}
	
	public User findOne(int id) {
		
		
		java.util.function.Predicate<? super User> predicate = user -> user.getId().equals(id);
		return users.stream().filter(predicate).findFirst().orElse(null);
	}
	
	public User save(User user) {
		
		user.setId(++usersCount);
		users.add(user);
		return user;
		
	}
	
	public void deleteById(int id) {
		
		
		java.util.function.Predicate<? super User> predicate = user -> user.getId().equals(id);
		users.removeIf(predicate);

	}

}
