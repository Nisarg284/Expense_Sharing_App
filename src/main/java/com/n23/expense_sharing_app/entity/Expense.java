package com.n23.expense_sharing_app.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class) // <--- ENSURE THIS IS HERE
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    private String description;
    private Double amount;

    @CreatedDate
    @Column(nullable = false,updatable = false)
    private Date createdAt;


    @ManyToOne
    private User paidBy;

    @ManyToOne
    private Group group;

    @OneToMany(mappedBy = "expense", fetch = FetchType.LAZY)
    private List<ExpenseSplit> expenseSplits;



}
