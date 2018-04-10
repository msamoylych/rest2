package org.java.rest.repository.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @PositiveOrZero
    private BigDecimal amount;

    public Account(String amount) {
        this.amount = new BigDecimal(amount);
    }
}
