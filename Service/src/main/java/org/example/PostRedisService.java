/*
package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostRedisService {

    private static final String HASH_KEY = "Posts";

    @Autowired
    private RedisTemplate redisTemplate;

    public Posts savePosts(Posts post){
        redisTemplate.opsForHash().put(HASH_KEY, post.getId(),post);
        return post;
    }

    public Posts findPostbyId(Long id){
        return (Posts) redisTemplate.opsForHash().get(HASH_KEY, id);
    }

    public List<Posts> findAll(){
        return redisTemplate.opsForHash().values(HASH_KEY);
    }

}
*/
