package org.me.utm.rest;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.me.utm.model.OptionsDoc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import org.me.utm.model.Link;
import org.me.utm.model.Resource;

@RestController
@RequestMapping(value="/api/v1/")
public class IndexRest {
	
	@RequestMapping("/")
    public String home() {//non-rest welcome page
        return "Welcome to RestTemplate try some GET or PUT requests";
    }
	
	@RequestMapping(value="/", method=RequestMethod.OPTIONS)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> showOptions0() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Allow", "OPTIONS,GET");
		
		Map<HttpMethod, String> methods = new Hashtable<>(2);
		methods.put(HttpMethod.GET, "Lists resources available.");
		methods.put(HttpMethod.OPTIONS, "Resource documentation.");
		
		OptionsDoc options = new OptionsDoc();
		options.setMethods(methods);
		
		return new ResponseEntity<>(options, headers, HttpStatus.OK);
    }
	
	@GetMapping(value = "/", produces = { "application/json", "text/json" })
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> indexJSON() {
		ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentServletMapping();
		List<Link> links = new ArrayList<Link>();
		links.add(new Link(builder.path("/api/v1/").build().toString(), "self"));
		links.add(new Link(builder.path("").build().toString() + "user/", "user"));
		links.add(new Link(builder.path("").build().toString() + "directory/", "directory"));
		links.add(new Link(builder.path("").build().toString() + "file/", "file"));
		links.add(new Link(builder.path("").build().toString() + "notify/", "notify"));
		Map<String, Object> response = new Hashtable<>(1);
		response.put("_links", links);
		return response;
	}
	
	@GetMapping(value = "/", produces = { "application/xml", "text/xml" })
	@ResponseStatus(HttpStatus.OK)
	public Resource indexXML() {
		ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentServletMapping();
		Resource resource = new Resource();
		resource.addLink(new Link(builder.path("/api/v1/").build().toString(), "self"));
		resource.addLink(new Link(builder.path("").build().toString() + "user/", "user"));
		resource.addLink(new Link(builder.path("").build().toString() + "directory/", "directory"));
		resource.addLink(new Link(builder.path("").build().toString() + "file/", "file"));
		resource.addLink(new Link(builder.path("").build().toString() + "notify/", "notify"));
		return resource;
	}
	
}
