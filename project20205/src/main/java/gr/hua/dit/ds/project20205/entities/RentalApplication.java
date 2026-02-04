package gr.hua.dit.ds.project20205.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "rental_application")
public class RentalApplication {

    // Πρωτεύον κλειδί για την ταυτοποίηση της αίτησης.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Το όνομα του ενδιαφερόμενου ενοικιαστή.
    @Column(name = "first_name", nullable = false)
    private String firstName;

    // Το επώνυμο του ενδιαφερόμενου ενοικιαστή.
    @Column(name = "last_name", nullable = false)
    private String lastName;

    // Το email του ενδιαφερόμενου ενοικιαστή.
    @Column(name = "email", nullable = false)
    private String email;

    // Ο αριθμός τηλεφώνου του ενδιαφερόμενου ενοικιαστή.
    @Column(name = "phone", nullable = false)
    private String phone;

    // Συσχέτιση της αίτησης με ένα ακίνητο.
    // Το πεδίο αυτό αντιπροσωπεύει το ακίνητο για το οποίο υποβλήθηκε η αίτηση.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", referencedColumnName = "id", nullable = true)
    private Property property;


    // Getters και Setters για κάθε πεδίο.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

@Override
public String toString() {
    return "RentalApplication{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", phone='" + phone + '\'' +
            ", property=" + (property != null ? property.getId() : "N/A") +
            '}';
}
}
