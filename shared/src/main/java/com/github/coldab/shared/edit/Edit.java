package com.github.coldab.shared.edit;

import com.github.coldab.shared.account.Account;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * An Edit is a change in a {@link com.github.coldab.shared.project.TextFile}
 */
public abstract class Edit {
    private LocalDateTime creationDate;
    private boolean applied;
    /**
     * The start position where the edit is applied
     */
    protected Letter start;
    private Account account;

    /**
     * Apply this edit
     * @param letters the letters to apply changes on
     *
     */
    public void apply(List<Letter> letters) {
        if (applied) {
            throw new IllegalStateException();
        }
        applied = true;
    }

    /**
     * Undo this edit
     * @param letters the letters to undo changes on
     */
    public void undo(List<Letter> letters) {
        if (!applied) {
            throw new IllegalStateException();
        }
        applied = false;
    }
}
