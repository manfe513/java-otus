package ru.otus.crm.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "addresses")
@NoArgsConstructor
@Getter
@Setter
public class Address implements Cloneable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "street")
    private String street;

    public Address(Long id, String street) {
        this.id = id;
        this.street = street;
    }

    public Address clone() {
        return new Address(id, street);
    }

    @Override
    public String toString() {
        return "Address{" + "id=" + id + ", street='" + street + '\'' + '}';
    }
}
