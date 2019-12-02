package org.me.utm.rest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.me.utm.model.File;
import org.me.utm.model.FileLinkListResource;
import org.me.utm.model.Link;
import org.me.utm.model.OptionsDoc;
import org.me.utm.rest.exception.ResourceNotFoundException;
import org.me.utm.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value="/api/v1/directory/")
public class DirectoryRest {
	
	@Autowired
	FileService fileService;
	
	@RequestMapping(value="/", method=RequestMethod.OPTIONS)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> showOptions() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Allow", "OPTIONS,GET");
		
		Map<HttpMethod, String> methods = new Hashtable<>(2);
		methods.put(HttpMethod.GET, "Lists specified directory contents in parameter 'dir'.");
		methods.put(HttpMethod.OPTIONS, "Resource documentation.");
		
		OptionsDoc options = new OptionsDoc();
		options.setMethods(methods);
		
		return new ResponseEntity<>(options, headers, HttpStatus.OK);
    }
	
	@GetMapping(path="/", headers="Accept=*/*", produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> getFilesJSON(@RequestParam("dir") String dir) {
		ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentServletMapping();

		Path path = Paths.get(dir);
		List<Path> paths = new ArrayList<Path>();
		
		if(!Files.exists(path)) {
			throw new ResourceNotFoundException(dir + " does not exist.");
		}
			
		List<File> files = new ArrayList<File>();
		paths = fileService.walkDir(path, paths);
		paths.forEach(f -> files.add(
				new File(f.getFileName().toString(), path.toAbsolutePath().toString().replaceAll("\\\\", "/"), f.toAbsolutePath().toString().replaceAll("\\\\", "/"), String.valueOf(f.toFile().length()), 
						new Link(ServletUriComponentsBuilder.fromCurrentServletMapping().path("/file/?path="+f.toAbsolutePath()).build().toString().replaceAll("\\\\", "/"), "download"))));
		
		List<Link> links = new ArrayList<Link>();
		links.add(new Link(builder.path("/api/v1/").build().toString(), "api"));
		links.add(new Link(builder.path("/directory/").build().toString(), "self"));
				
		Map<String, Object> response = new Hashtable<>(2);
		response.put("_links", links);
		response.put("data", files);
		return response;
    }
	
	@GetMapping(path="/xml", headers="Accept=*/*", produces = { "application/xml", "text/xml" })
	@ResponseStatus(HttpStatus.OK)
	public FileLinkListResource getFilesXML(@RequestParam("dir") String dir) {
		ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentServletMapping();
		
		Path path = Paths.get(dir);
		List<Path> paths = new ArrayList<Path>();
		
		if(!Files.exists(path))
			throw new ResourceNotFoundException(dir + " does not exist.");

		List<File> files = new ArrayList<File>();
		paths = fileService.walkDir(path, paths);
		paths.forEach(f -> files.add(
				new File(f.getFileName().toString(), path.toAbsolutePath().toString().replaceAll("\\\\", "/"), f.toAbsolutePath().toString().replaceAll("\\\\", "/"), String.valueOf(f.toFile().length()), 
						new Link(ServletUriComponentsBuilder.fromCurrentServletMapping().path("/file/?path="+f.toAbsolutePath()).build().toString().replaceAll("\\\\", "/"), "download"))));
		
		List<Link> links = new ArrayList<Link>();
		links.add(new Link(builder.path("/api/v1/").build().toString(), "api"));
		links.add(new Link(builder.path("/directory/").build().toString(), "self"));

		FileLinkListResource fileLinksResource = new FileLinkListResource();
		fileLinksResource.setLinks(links);
		fileLinksResource.setFiles(files);
		return fileLinksResource;
    }

}
