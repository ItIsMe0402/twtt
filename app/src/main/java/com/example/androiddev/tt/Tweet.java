package com.example.androiddev.tt;

import java.util.Date;

/**
 * Created by androiddev on 4/5/17.
 */

public class Tweet {
    private long id;
    private String message;
    private Date createdAt;
    private User user;

    public Tweet() {
        super();
        createdAt = new Date(0);
        user = new User();
    }

    public User getUser() {
        return user;
    }

    public class User {
        private long id;
        private String name;
        private String imgUrl;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        User() {
            name = new String();
            imgUrl = new String();
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
