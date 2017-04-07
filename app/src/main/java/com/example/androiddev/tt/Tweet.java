package com.example.androiddev.tt;

/**
 * Created by androiddev on 4/5/17.
 */

public class Tweet {
    private long id;
    private String message;
    private String created;
    private Account account;

    public Tweet() {
        super();
        account = new Account();
    }

    public Account getAccount() {
        return account;
    }

    public class Account {
        private long id;
        private String name;

        Account() {
            name = new String();
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
