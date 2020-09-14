package com.wolfpack.rest.webservices.restfulwebservices.pack;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;

import com.wolfpack.rest.webservices.restfulwebservices.wolf.Wolf;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Insides of the pack")
@Entity
@Table(name="pack")
public class Pack {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull(message="Wof wof, pack cannot survive without it's name!")
	@ApiModelProperty(notes="Wof wof, pack cannot survive without it's name!")
	private String name;
	
	@OneToMany(targetEntity=Wolf.class,cascade = CascadeType.ALL , fetch = FetchType.LAZY, mappedBy = "pack")
	private List<Wolf> wolves;

    protected Pack()
    {
    	
    }

	public Pack(Integer id, String name, List<Wolf> wolves) {
		super();
		this.id = id;
		this.name = name;
		this.wolves = wolves;
	}

	public List<Wolf> getWolves() {
		return wolves;
	}

	public void setWolves(List<Wolf> wolves) {
		this.wolves = wolves;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
