package com.eecs4413final.demo.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "administrators")
public class Administrator {

    @Id
    private Long adminID;

    @OneToOne
    @MapsId
    @JoinColumn(name = "adminID")
    private User user;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    public Administrator() {
    }

    public Administrator(User user, String firstName, String lastName) {
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getAdminID() {
        return adminID;
    }

    public void setAdminID(Long adminID) {
        this.adminID = adminID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    @Override
    public String toString() {
        return "Administrator{" +
                "adminID=" + adminID +
                ", user=" + user.getUsername() +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Administrator that = (Administrator) o;

        return Objects.equals(adminID, that.adminID);
    }

    @Override
    public int hashCode() {
        return adminID != null ? adminID.hashCode() : 0;
    }
}