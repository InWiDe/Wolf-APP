package com.wolfAPP.rest.webservices.restfulwebservices.wolf;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
 
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

@RestController
@Validated
public class WolfController {
	
	@Autowired
	private final WolfRepository wolfrepository;
	

	public WolfController(WolfRepository wolfrepository) {
	        this.wolfrepository = wolfrepository;   
	}
	
	
	 /**
     * Use this GET request to retrieve all wolves from database.
     *
     * @return Get request response with 200 status code.
     */
	@GetMapping("/wolves")
	public Iterable<Wolf> retrieveAllWolves()
	{
		return wolfrepository.findAll();
	}
	
	
	 /**
     * Use this GET request to retrieve wolf from database.
     *
     * @param id is the id of the specific wolf. Minimum value of id is 1.
     * @return GET request response with 200 status code.
     * @throws WolfNotFoundException with 404 status code if wolf is not present in database.
     * 
     * Also, HATEOAS has been included for navigation improvement.
     */
	@GetMapping("/wolves/{id}")
	public EntityModel<Wolf> retrieveWolf(@PathVariable @Min(1) int id)
	{
		Optional<Wolf> wolf = wolfrepository.findById(id);
		if(!wolf.isPresent())
		{
			throw new WolfNotFoundException("id:"+id);
		}
				
		//HATEOAS - Adding the control logic for consumer
		EntityModel<Wolf> resource = EntityModel.of(wolf.get());
		
		WebMvcLinkBuilder linkTo = 
				linkTo(methodOn(this.getClass()).retrieveAllWolves());
		
		resource.add(linkTo.withRel("all-wolves"));
		
		return resource;		
	}
	
	
	/**
     * Use this POST request to create a new wolf.
     *
     * @param wolf is body parameter of Wolf object. Validation is done regarding to this object.
     * @return POST request response with 201 status code.
     * @throws handleMethodArgumentNotValid with 400 status code is body parameter does not meet all validation requirements.
     */
	@PostMapping("/wolves")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Object> createWolf(@Valid @RequestBody Wolf wolf)
	{
		Wolf savedWolf = wolfrepository.save(wolf);
		
		URI location = ServletUriComponentsBuilder
		.fromCurrentRequest()
		.path("/{id}")
		.buildAndExpand(savedWolf.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	
    /**
     * Use this DELETE request to remove wolf from database. This request also deletes wolf from the pack.
     *
     * @param id is the id of the specific wolf. Minimum value of id is 1.
     * @return DELETE request response with 200 status code.
     * @throws WolfNotFoundException with 404 status code if wolf was not found in database.
     */
	@DeleteMapping("/wolves/{id}")
	public void deleteWolf(@PathVariable @Min(1) int id)
	{
		Optional<Wolf> wolf = wolfrepository.findById(id);
		if(!wolf.isPresent())
		{
			throw new WolfNotFoundException("Wof Wof, Wolf with this id was not found:"+id);
		}
		wolfrepository.deleteById(id);
		
	}
	
	
	/**
     * Use this PUT request to update wolf.
     * 
     * @param newWolf is body parameter of Wolf object. Validation is done regarding to this object.
     * @param id is the id of the specific wolf. Minimum value of id is 1.
     * @return PUT request response with 204 status code.
     * @throws WolfNotFoundException with 404 status code if wolf was not found in database.
     * @throws handleMethodArgumentNotValid with 400 status code is body parameter does not meet all validation requirements.
     */
	@PutMapping("/wolves/{id}")
	 public ResponseEntity<Object> updateWolf(@Valid @RequestBody Wolf newWolf, @PathVariable @Min(1) int id) {

		Optional<Wolf> wolfOptional = wolfrepository.findById(id);

		if (!wolfOptional.isPresent())
			throw new WolfNotFoundException("Wof Wof, Wolf with this id was not found:"+id);

		newWolf.setId(id);
		
		wolfrepository.save(newWolf);

		return ResponseEntity.noContent().build();
	  }

}
