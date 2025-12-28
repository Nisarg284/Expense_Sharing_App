package com.n23.expense_sharing_app.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "expense_group")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Optional but safe
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;


    @Column(unique = true)
    private String name;
    private String description;

    // One Group has many members
    @ManyToMany
    @ToString.Exclude
    @JoinTable
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Optional but safe
    private Set<User>groupMembers = new HashSet<>();

}
