package org.java.rest.controller.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@Setter
public class Amount {

    @NotNull
    @Positive
    private BigDecimal amount;
}
