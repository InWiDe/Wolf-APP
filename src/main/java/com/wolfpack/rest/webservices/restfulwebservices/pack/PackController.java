package com.wolfpack.rest.webservices.restfulwebservices.pack;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.wolfpack.rest.webservices.restfulwebservices.wolf.Wolf;
import com.wolfpack.rest.webservices.restfulwebservices.wolf.WolfNotFoundException;
import com.wolfpack.rest.webservices.restfulwebservices.wolf.WolfRepository;


@RestController
@Validated
public class PackController {
	
	@Autowired
	private final PackRepository packrepository;
	@Autowired
	private final WolfRepository wolfrepository;

	public PackController(PackRepository packrepository, WolfRepository wolfrepository) {
		super();
		this.packrepository = packrepository;
		this.wolfrepository = wolfrepository;
		
	}
	
	 /**
	 * Use this GET request to retrieve all packs.
	 *
	 * @return GET request response with 200 status code.
	 */
	@GetMapping("/wolfPacks")
	public List<Pack> retrieveAllPacks()
	{
		return packrepository.findAll();
	}
		
	
	 /**
	 * Use this GET request to retrieve all wolves from pack.
	 *
	 * @param id is the id of the pack.
	 * @return GET request response with 200 status code.
	 * @throws PackNotFoundException with 404 status code if pack is not present in database.
	 */	
	@GetMapping("/wolfPacks/{id}/wolves")
	public List<Wolf> RetrieveAllWolvesForPack(@PathVariable int id)
	{
		Optional <Pack> packOptional =  packrepository.findById(id);
		if(!packOptional.isPresent())
		{
			throw new PackNotFoundException("Wof Wof, wolf with id-"+id+" was not found");
		}
		
		return packOptional.get().getWolves();
	}
		
	
	/**
	 * Use this POST request to create new pack.
	 *
	 * @param pack is body parameter of Pack object. Validation is done regarding to this object.
	 * @return POST request response with 201 status code.
	 * @throws handleMethodArgumentNotValid with 400 status code is body parameter does not meet all validation requirements.
	 */	
	@PostMapping("/wolfPacks")
	public ResponseEntity<Object> createPack(@Valid @RequestBody Pack pack)
	{
		Pack savedPack = packrepository.save(pack);
		
		URI location = ServletUriComponentsBuilder
		.fromCurrentRequest()
		.path("/{id}")
		.buildAndExpand(savedPack.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	
	/**
	 * Use this PUT request to update pack.
	 *
	 * @param newPack is body parameter of Pack object. Validation is done regarding to this object.
	 * @param id is the id of the pack.
	 * @return PUT request response with 200 status code.
	 * @throws handleMethodArgumentNotValid with 400 status code is body parameter does not meet all validation requirements.
	 */	
	@PutMapping("/wolfPacks/{id}")
	public Pack updateWolfPack(@RequestBody Pack newPack, @PathVariable int id) 
	{	
		return packrepository.findById(id).map(pack -> {
			   pack.setName(newPack.getName());
			   return packrepository.save(pack);
			    })
			    .orElseGet(() -> {
			    newPack.setId(id);
			    return packrepository.save(newPack);
			 });
	}	
		
	
	/**
	 * Use this POST request to add new wolf to the pack.
	 *
	 * @param packId is the id of the pack. Minimum value of id is 1.
	 * @param wolfId is the id of the wolf. Minimum value of id is 1.
	 * @return POST request response with 200 status code.
	 * @throws PackNotFoundException with 404 status code if pack is not present in database. 
	 * @throws WolfNotFoundException with 404 status code if wolf is not present in database.
	 */	
	@PostMapping("/wolfPacks/{packId}/wolves/{wolfId}")
	public ResponseEntity<Object> AddWolfToThePack(@PathVariable @Min(1) int packId, @PathVariable @Min(1) int wolfId)
	{
		Optional<Wolf> wolfOptional =  wolfrepository.findById(wolfId);
		if(!wolfOptional.isPresent())
		{
			throw new WolfNotFoundException("Wof Wof, Wolf was not found: wolfId-"+wolfId);
		}
			
		Optional <Pack> packOptional = packrepository.findById(packId);
		if(!packOptional.isPresent())
		{
			throw new PackNotFoundException("Wof Wof, Pack was not found:packId-"+packId);
		}
				
		Wolf wolf = wolfOptional.get();
		Pack pack = packOptional.get();
			
		wolf.setPack(pack);
			
		wolfrepository.save(wolf);
					
		return ResponseEntity.ok().build();
	}
	
	
	/**
	 * Use this DELETE request to delete wolf from the pack.
	 *
	 * @param wolfId is the id of the specific wolf. Minimum value of id is 1.
     * @return DELETE request response with 200 status code.
     * @throws WolfNotFoundException with 404 status code if wolf was not found in database.
	 */
	@DeleteMapping("/wolfPacks/wolves/{wolfId}")
	public ResponseEntity<Object> DeleteWolfFromThePack(@PathVariable @Min(1) int wolfId)
	{
		Optional<Wolf> wolfOptional =  wolfrepository.findById(wolfId);
		
		if(!wolfOptional.isPresent())
		{
			throw new WolfNotFoundException("Wof Wof, Wolf was not found: wolfId-"+wolfId);
		}
		
		Wolf wolf = wolfOptional.get();
			
		wolf.setPack(null);
			
		wolfrepository.save(wolf);
					
		return ResponseEntity.ok().build();
	}
		
}
