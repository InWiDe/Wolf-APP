package com.wolfpack.rest.webservices.restfulwebservices.wolf;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
 
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

@RestController
public class WolfController {
	
	@Autowired
	private final WolfRepository wolfrepository;
	
	 public WolfController(WolfRepository wolfrepository) {
	        this.wolfrepository = wolfrepository;
	    }
	
	//Retrieve all Wolves
	@GetMapping("/wolves")
	public Iterable<Wolf> retrieveAllWolves()
	{
		return wolfrepository.findAll();
	}
	
	//Retrieve Wolf
	@GetMapping("/wolves/{id}")
	public EntityModel<Wolf> retrieveWolf(@PathVariable int id)
	{
		Optional<Wolf> wolf = wolfrepository.findById(id);
		if(!wolf.isPresent())
		{
			throw new WolfNotFoundException("id:"+id);
		}
				
		//HATEOAS
		EntityModel<Wolf> resource = EntityModel.of(wolf.get());
		
		WebMvcLinkBuilder linkTo = 
				linkTo(methodOn(this.getClass()).retrieveAllWolves());
		
		resource.add(linkTo.withRel("all-wolves"));
		
		return resource;		
	}
	
	//Add Wolf
	@PostMapping("/wolves")
	public ResponseEntity<Object> createWolf(@Valid @RequestBody Wolf wolf)
	{
		Wolf savedWolf = wolfrepository.save(wolf);
		
		URI location = ServletUriComponentsBuilder
		.fromCurrentRequest()
		.path("/{id}")
		.buildAndExpand(savedWolf.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	//Delete Wolf (Deletes also from the pack)
	@DeleteMapping("/wolves/{id}")
	public void deleteWolf(@PathVariable int id)
	{
		Optional<Wolf> wolf = wolfrepository.findById(id);
		if(!wolf.isPresent())
		{
			throw new WolfNotFoundException("Wof Wof, Wolf with this id was not found:"+id);
		}
		wolfrepository.deleteById(id);
		
	}
	
	//Update Wolf
	@PutMapping("/wolves/{id}")
	 public ResponseEntity<Object> updateWolf(@RequestBody Wolf newWolf, @PathVariable int id) {

		Optional<Wolf> wolfOptional = wolfrepository.findById(id);

		if (!wolfOptional.isPresent())
			throw new WolfNotFoundException("Wof Wof, Wolf with this id was not found:"+id);

		newWolf.setId(id);
		
		wolfrepository.save(newWolf);

		return ResponseEntity.noContent().build();
	  }

}
