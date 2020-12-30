package com.revature.Account;

import java.util.Objects;

public class AccountAction {
    private boolean wasSuccessful;
    private String message;

    /**+
     *
     * @param wasSuccessful A value of {@code true} if the AccountAction was successful
     * @param message A Message with information about the AccountAction
     */
    public AccountAction(boolean wasSuccessful, String message) {
        this.wasSuccessful = wasSuccessful;
        this.message = message;
    }

    /**+
     *
     * @return {@code true} if the AccountAction was successful
     */
    public boolean wasSuccessful() {
        return wasSuccessful;
    }

    /**+
     *
     * @return A Message with information about the AccountAction
     */
    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountAction that = (AccountAction) o;
        return wasSuccessful == that.wasSuccessful &&
                message.equals(that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wasSuccessful, message);
    }
}
