package com.cherniva.springsecurityserver.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="pin", uniqueConstraints = {@UniqueConstraint(columnNames = {"value", "password"})})
public class Pin
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(nullable=false, unique=true)
//    private String value; // individual username
//
//    @Column(nullable=false)
//    private String password;

    @Column(nullable=true)
    private String value; // individual username

    @Column(nullable=false)
    private String password;

    @Column//(unique = true)
    private String uid;

    @ManyToMany(mappedBy="pins")
    private List<User> users;

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name="pin_timestamp",
            joinColumns={@JoinColumn(name="PIN_ID", referencedColumnName="ID")},
            inverseJoinColumns={@JoinColumn(name="TIMESTAMP_ID", referencedColumnName="ID")})
    private List<Timestamp> timestamps = new ArrayList<>();

}
