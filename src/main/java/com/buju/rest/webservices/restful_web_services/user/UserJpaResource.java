package com.buju.rest.webservices.restful_web_services.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.buju.rest.webservices.restful_web_services.jpa.PostRepository;
import com.buju.rest.webservices.restful_web_services.jpa.UserRepository;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;

@RestController
public class UserJpaResource {
	
	@Autowired
	private UserDaoService service;
	
/*	public UserResource(UserDaoService service) {
		super();
		this.service = service;
	}*/
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private PostRepository postRepository;
	
	

	public UserJpaResource(UserDaoService service, UserRepository repository, PostRepository postRepository) {
		super();
		this.service = service;
		this.repository = repository;
		this.postRepository = postRepository;
	}

	@GetMapping("/jpa/users")
	public List<User> retrieveAllUsers() {
		
		return repository.findAll();
	}
	
	//localhost:8080/users
	
	//EntityModel
	//WebMvcLinkBuilder
	
	@GetMapping("/jpa/users/{id}")
	public EntityModel<Optional<User>> retrieveUser(@PathVariable int id) {
		
		Optional<User> user = repository.findById(id);
		
		if(user.isEmpty())
			throw new UserNotFoundException("id:"+id);
		
		EntityModel<Optional<User>> entityModel = EntityModel.of(user);
		
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		entityModel.add(link.withRel("all-users"));
		
		return entityModel;
	}
	
	@PostMapping("/jpa/users")
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		
		user.setUsername(user.getUsername());
		
		User savedUser = repository.save(user);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId()).toUri(); // to the uri of the current request add a path id and replace id with the id of the created user
		
		return ResponseEntity.created(location).build();
	}
	
	@GetMapping("/jpa/users/{id}/posts")
	public List<Post> retrievePostsForUser(@PathVariable int id) {
		
		Optional<User> user = repository.findById(id);
				
				if(user.isEmpty())
					throw new UserNotFoundException("id:"+id);
		
		return user.get().getPosts();		
	}
	
	@PostMapping("/jpa/users/{id}/posts")
	public ResponseEntity<Object> createPostsForUser(@PathVariable int id, @Valid @RequestBody Post post) {
		
		Optional<User> user = repository.findById(id);
				
		if(user.isEmpty())
			throw new UserNotFoundException("id:"+id);
				
		post.setUser(user.get());
		
		Post savedPost = postRepository.save(post);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedPost.getId()).toUri(); // to the uri of the current request add a path id and replace id with the id of the created user
		
		return ResponseEntity.created(location).build();
		
	}
	
	
	
	
}
