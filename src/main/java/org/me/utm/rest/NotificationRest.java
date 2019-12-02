package org.me.utm.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.me.utm.model.Link;
import org.me.utm.model.Notification;
import org.me.utm.model.NotificationLinkListResource;
import org.me.utm.model.OptionsDoc;
import org.me.utm.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value="/api/v1/notify/")
public class NotificationRest {
	
	@Autowired
	NotificationService notificationService;
	
	private static final Logger logger = LogManager.getLogger();
	
	@RequestMapping(value="/", method=RequestMethod.OPTIONS)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> showOptions() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Allow", "OPTIONS,GET,POST");
		
		Map<HttpMethod, String> methods = new Hashtable<>(3);
		methods.put(HttpMethod.GET, "Lists notifications submitted.");
		methods.put(HttpMethod.OPTIONS, "Resource documentation.");
		methods.put(HttpMethod.POST, "Submits notification to send.");
		
		OptionsDoc options = new OptionsDoc();
		options.setMethods(methods);
		
		return new ResponseEntity<>(options, headers, HttpStatus.OK);
    }
	
	@GetMapping(value = "/", produces = { "application/json", "text/json" })
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> getNotificationsJSON() {
		ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentServletMapping();		
		
		List<Link> links = new ArrayList<Link>();
		links.add(new Link(builder.path("/").build().toString(), "api"));
		links.add(new Link(builder.path("/notify/").build().toString(), "self"));
		
		Map<String, Object> response = new Hashtable<>(2);
		response.put("_links", links);
		response.put("data", notificationService.getNotifications());
		return response;
	}
	
	@GetMapping(value = "/", produces = { "application/xml", "text/xml" })
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public NotificationLinkListResource getNotificationsXML() {
		ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentServletMapping();
		List<Link> links = new ArrayList<Link>();
		links.add(new Link(builder.path("/").build().toString(), "api"));
		links.add(new Link(builder.path("/notify/").build().toString(), "self"));

		NotificationLinkListResource notificationLinksResource = new NotificationLinkListResource();
		notificationLinksResource.setLinks(links);
		notificationLinksResource.setNotifications(notificationService.getNotifications());
		return notificationLinksResource;
	}

}
