package com.wolfpack.rest.webservices.restfulwebservices.pack;

import org.springframework.data.jpa.repository.JpaRepository;

//Interface that persists Pack object into the database.
public interface PackRepository extends JpaRepository<Pack, Integer>  {

}
