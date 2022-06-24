package org.example;


import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@RedisHash("Posts")
public class Posts implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Indexed
    private String message;

    private boolean isVisible;

    private Integer likes;

    private Integer comments;


    public String getId() {
        return id;
    }



    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Posts{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", isVisible=" + isVisible +
                ", likes=" + likes +
                ", comments=" + comments +
                '}';
    }
}
