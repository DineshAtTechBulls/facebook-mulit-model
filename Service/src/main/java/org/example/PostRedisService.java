package org.example;

import org.example.redis.PostRedisRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hibernate.tool.schema.SchemaToolingLogging.LOGGER;

@Repository
public class PostRedisService {

    @Autowired
    PostRedisRepo postRedisRepo;
    private static final String HASH_KEY = "Posts";

    @Autowired
    private RedisTemplate redisTemplate;


    public Posts savePosts(Posts post){
        System.out.println("id ----> "+post.getId());
        redisTemplate.opsForHash().put(HASH_KEY, post.getId(),post);
        return post;
    }

    public Posts findPostbyId(Long id){
        return (Posts) redisTemplate.opsForHash().get(HASH_KEY, id);
    }

    public List<Posts> findAll(){
        return redisTemplate.opsForHash().values(HASH_KEY);
    }

    public List<Posts> findAll(final int pageNum, final int pageSize) {
        int tmpIndex = 0;
        int tmpEndIndex = 0;
        final List<Posts> entities = new ArrayList<>();
        try (Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(HASH_KEY,
                ScanOptions.scanOptions().match("*").build())) {
            while (cursor.hasNext()) {
                if (tmpIndex >= pageNum*pageSize && tmpEndIndex < pageSize) {
                    final Map.Entry<Object, Object> entry = cursor.next();
                    final Posts entity = (Posts) entry.getValue();
                    entities.add(entity);
                    tmpIndex++;
                    tmpEndIndex++;
                    continue;
                }
                if (tmpEndIndex >= pageSize) {
                    break;
                }
                tmpIndex++;
                cursor.next();
            }
        } catch (Exception ex) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Exception while fetching data from redis cache : " + ex);
            }
        }
        return entities;
    }

    public Page<Posts> getAllPosts(int pageNo, int pageSize){
        return postRedisRepo.findAll(PageRequest.of(pageNo,pageSize,Sort.by("message").descending()));
    }

    public Posts findPostByMessage(String message){
        return postRedisRepo.findByMessage(message);
    }

}
