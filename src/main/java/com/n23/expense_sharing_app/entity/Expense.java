package com.n23.expense_sharing_app.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.n23.expense_sharing_app.enums.ExpenseType;
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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Optional but safe
    private User paidBy;

    @ManyToOne
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Optional but safe
    private Group group;

    @OneToMany(mappedBy = "expense", fetch = FetchType.LAZY)
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Optional but safe
    private List<ExpenseSplit> expenseSplits;


    @Enumerated(EnumType.STRING)
    @Column(name = "expense_type")
    private ExpenseType type;


    @Column(nullable = false)
    private boolean deleted = false;


}
