package org.me.utm.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.me.utm.model.Link;
import org.me.utm.model.OptionsDoc;
import org.me.utm.model.User;
import org.me.utm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import org.me.utm.rest.exception.ResourceNotFoundException;

@RestController
@RequestMapping(value="/api/v1/user/")
public class UserRest {
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value="/", method=RequestMethod.OPTIONS)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> showOptions() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Allow", "OPTIONS,GET,POST");
		
		Map<HttpMethod, String> methods = new Hashtable<>(3);
		methods.put(HttpMethod.GET, "Lists all users.");
		methods.put(HttpMethod.OPTIONS, "Resource documentation.");
		methods.put(HttpMethod.POST, "Creates new user.");
		
		OptionsDoc options = new OptionsDoc();
		options.setMethods(methods);
		
		return new ResponseEntity<>(options, headers, HttpStatus.OK);
    }
	
	@GetMapping(value = "/", produces = { "application/json", "text/json" })
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> getUsersJSON() {
		ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentServletMapping();
		
		List<Link> links = new ArrayList<Link>();
		links.add(new Link(builder.path("/api/v1/").build().toString(), "api"));
		links.add(new Link(builder.path("/user/").build().toString(), "self"));
		
		List<Link> data = new ArrayList<Link>();
		userService.getUsers().forEach(user -> data.add(new Link(ServletUriComponentsBuilder.fromCurrentServletMapping().path("/user/" + user.getId()).build().toString(), user.toString())));
		
		Map<String, Object> response = new Hashtable<>(2);
		response.put("_links", links);
		response.put("data", data);
		return response;
	}
	
	@PostMapping(value = "/", produces = { "application/json", "text/json" })
	@ResponseStatus(HttpStatus.OK)
	public User createUser(@RequestBody User user) {
    	return userService.createUser(user);
    }
	
	@PutMapping(value = "/", produces = { "application/json", "text/json" })
    public User updateUser(@RequestBody User user) {
    	return userService.updateUser(user);
    }
	
	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable("id") Long userId) {
		if (this.userService.getUser(userId) == null) {
			throw new ResourceNotFoundException("User not found");
		}
			
		this.userService.deleteUser(userId);
    }
	
	@GetMapping("/{id}")
	public Map<String, Object> getUser(@PathVariable("id") Long userId) {
		ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentServletMapping();
		
		if (this.userService.getUser(userId) == null) {
			throw new ResourceNotFoundException("User was not found");
		}
			
		List<Link> links = new ArrayList<Link>();
		links.add(new Link(builder.path("/api/v1/user/").build().toString(), "user"));
		links.add(new Link(builder.path(userId.toString()).build().toString(), "self"));
		
		Map<String, Object> response = new Hashtable<>(2);
		response.put("_links", links);
		response.put("data", userService.getUser(userId));
		return response;
	}


}
