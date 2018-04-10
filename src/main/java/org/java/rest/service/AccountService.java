package org.java.rest.service;

import org.java.rest.service.exception.AccountException;

import java.math.BigDecimal;

public interface AccountService {

    /**
     * Пополнение счёта {@code accountId}
     *
     * @param accountId Id счёта
     * @param amount    Сумма денег
     * @throws AccountException Если счёт {@code accountId} не найден
     */
    void replenish(Long accountId, BigDecimal amount) throws AccountException;

    /**
     * Списать со счёта {@code accountId}
     *
     * @param accountId Id счёта
     * @param amount    Сумма денег
     * @throws AccountException Если счёт {@code accountId} не найден или на счёте недостаточно денег
     */
    void withdraw(Long accountId, BigDecimal amount) throws AccountException;

    /**
     * Перевод денег с {@code fromAccountId} на {@code toAccountId}
     *
     * @param fromAccountId Id счёта списания
     * @param toAccountId   Id счёта пополнения
     * @param amount        Сумма денег
     * @throws AccountException Если счёт {@code fromAccountId} или {@code toAccountId} не найден,
     *                          или на счёте {@code fromAccountId} недостаточно денег
     */
    void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) throws AccountException;
}
