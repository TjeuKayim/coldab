package com.github.coldab.shared.edit;

import com.github.coldab.shared.account.Account;

import java.time.LocalDateTime;

/**
 * A deletion is the removal of letters between a start (inclusive) and end (exclusive) position
 */
public class Deletion extends Edit {
    private final Letter end;

    public Deletion(Account account, LocalDateTime creationDate, Letter start, Letter end) {
        super(account, creationDate, start);
        this.end = end;
    }
}
