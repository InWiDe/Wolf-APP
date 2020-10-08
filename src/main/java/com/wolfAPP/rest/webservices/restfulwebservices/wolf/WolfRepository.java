package com.wolfAPP.rest.webservices.restfulwebservices.wolf;

import org.springframework.data.jpa.repository.JpaRepository;

//Interface that persists Wolf object into the database.
public interface WolfRepository extends JpaRepository<Wolf, Integer> {
}
