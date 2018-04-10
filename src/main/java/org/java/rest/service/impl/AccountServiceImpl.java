package org.java.rest.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.java.rest.repository.AccountRepository;
import org.java.rest.repository.dao.Account;
import org.java.rest.service.AccountService;
import org.java.rest.service.exception.AccountException;
import org.java.rest.service.exception.AccountNotFoundException;
import org.java.rest.service.exception.NotEnoughMoneyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void replenish(Long accountId, BigDecimal amount) throws AccountException {
        Account account = getAccount(accountId);
        updateAmount(account, amount);
    }

    public void withdraw(Long accountId, BigDecimal amount) throws AccountException {
        Account account = getAccount(accountId);
        checkEnoughMoney(account, amount);
        updateAmount(account, amount.negate());
    }

    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) throws AccountException {
        Account fromAccount, toAccount;
        if (fromAccountId < toAccountId) {
            fromAccount = getAccount(fromAccountId);
            checkEnoughMoney(fromAccount, amount);
            toAccount = getAccount(toAccountId);
        } else {
            toAccount = getAccount(toAccountId);
            fromAccount = getAccount(fromAccountId);
            checkEnoughMoney(fromAccount, amount);
        }
        updateAmount(fromAccount, amount.negate());
        updateAmount(toAccount, amount);
    }

    private Account getAccount(Long id) throws AccountNotFoundException {
        return accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
    }

    private void checkEnoughMoney(Account account, BigDecimal amount) throws NotEnoughMoneyException {
        if (account.getAmount().compareTo(amount) < 0) {
            throw new NotEnoughMoneyException();
        }
    }

    private void updateAmount(Account account, BigDecimal amount) {
        BigDecimal oldAmount = account.getAmount();
        BigDecimal newAmount = oldAmount.add(amount);
        account.setAmount(newAmount);
        accountRepository.save(account);
        log.info("Account <{}>: {} -> {}", account.getId(), oldAmount.toPlainString(), newAmount.toPlainString());
    }
}
