package com.wolfpack.rest.webservices.restfulwebservices.pack;

import java.net.URI;
import java.util.List;
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

import com.wolfpack.rest.webservices.restfulwebservices.wolf.Wolf;
import com.wolfpack.rest.webservices.restfulwebservices.wolf.WolfNotFoundException;
import com.wolfpack.rest.webservices.restfulwebservices.wolf.WolfRepository;


@RestController
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
	
		//Retrieve Packs
		@GetMapping("/wolfPacks")
		public List<Pack> retrieveAllPacks()
		{
			return packrepository.findAll();
		}
		
		
		//Retrieve all wolves from specific pack
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
		
		//Create new pack
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
		
		//Update pack
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
		
		//Add new wolf to the pack
		@PostMapping("/wolfPacks/{packId}/wolves/{wolfId}")
		public ResponseEntity<Object> AddWolfToThePack(@PathVariable int packId, @PathVariable int wolfId)
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
		
		
		//Deletes wolf from the pack
		@DeleteMapping("/wolfPacks/wolves/{wolfId}")
		public ResponseEntity<Object> DeleteWolfFromThePack(@PathVariable int wolfId)
		{
			Optional<Wolf> wolfOptional =  wolfrepository.findById(wolfId);
			
			if(!wolfOptional.isPresent())
			{
				throw new WolfNotFoundException("Wof Wof, Wolf was not found: wolfId-"+wolfId);
			}
			
			Wolf wolf = wolfOptional.get();
			
			if(wolf.getPack() == null)
			{
				throw new WolfNotFoundException("Wof wof, this wolf is currently lonely, you can add him to the pack using this URI:/wolfPacks/{packId}/wolves/{wolfId}");
			}
				
			wolf.setPack(null);
				
			wolfrepository.save(wolf);
						
			return ResponseEntity.ok().build();
		}
		

		
}
