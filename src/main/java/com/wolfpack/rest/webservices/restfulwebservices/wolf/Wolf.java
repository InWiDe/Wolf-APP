package com.wolfpack.rest.webservices.restfulwebservices.wolf;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wolfpack.rest.webservices.restfulwebservices.pack.Pack;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="All details about wolves")
@Entity
@Table(name="wolf")
public class Wolf {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Size(min=2, message="Name should have at least 2 characters")
	@ApiModelProperty(notes="Name should have at least 2 characters")
	private String name;
	
	private String gender; // Make enum
	
	@Past
	@ApiModelProperty(notes="Birthday should be in the past")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date birthDate;
	
	private Double longtitude;
	
	private Double latitude;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="pack_id")
	private Pack pack;
	
	protected Wolf() {
		
	}
	
	public Wolf(Integer id, String name, String gender, Date birthDate, Double longtitude, Double latitude) {
		super();
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.birthDate = birthDate;
		this.longtitude = longtitude;
		this.latitude = latitude;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Double getLongtitude() {
		return longtitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLongtitude(Double longtitude) {
		this.longtitude = longtitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Pack getPack() {
		return pack;
	}

	public void setPack(Pack pack) {
		this.pack = pack;
	}

	
}
