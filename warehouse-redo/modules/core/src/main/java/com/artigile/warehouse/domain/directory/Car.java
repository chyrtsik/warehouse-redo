package com.artigile.warehouse.domain.directory;

import javax.persistence.*;

/**
 * @author Valery Barysok, 2013-01-23
 */
@Entity
@Table(name = "car")
public class Car {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "state_number", nullable = false)
    private String stateNumber;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "owner")
    private String owner;

    @Column(name = "trailer")
    private String trailer;

    @Column(name = "description")
    private String description;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    public Car() {
    }

    public Car(long id) {
        this.id = id;
    }

    public Car(String brand, String stateNumber, String fullName) {
        this.brand = brand;
        this.stateNumber = stateNumber;
        this.fullName = fullName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getStateNumber() {
        return stateNumber;
    }

    public void setStateNumber(String stateNumber) {
        this.stateNumber = stateNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getVersion() {
        return version;
    }
}
