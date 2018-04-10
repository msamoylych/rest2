package org.java.rest.controller;

import org.java.rest.controller.dto.Amount;
import org.java.rest.controller.dto.Transfer;
import org.java.rest.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/account/{id}")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(path = "/replenish")
    ResponseEntity<?> replenish(@PathVariable Long id, @RequestBody @Valid Amount amount) throws Exception {
        accountService.replenish(id, amount.getAmount());
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/withdraw")
    ResponseEntity<?> withdraw(@PathVariable Long id, @RequestBody @Valid Amount amount) throws Exception {
        accountService.withdraw(id, amount.getAmount());
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/transfer")
    ResponseEntity<?> transfer(@PathVariable Long id, @RequestBody @Valid Transfer transfer) throws Exception {
        accountService.transfer(id, transfer.getAccount(), transfer.getAmount());
        return ResponseEntity.ok().build();
    }
}
