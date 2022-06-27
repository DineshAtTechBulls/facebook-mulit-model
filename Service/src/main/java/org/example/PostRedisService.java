package org.example;

import org.example.redis.PostRedisRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.query.SortQueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.*;

import static org.hibernate.tool.schema.SchemaToolingLogging.LOGGER;

@Repository
public class PostRedisService {

    @Autowired
    PostRedisRepo postRedisRepo;
    private static final String HASH_KEY = "Posts";

    @Autowired
    private RedisTemplate redisTemplate;
    private Object d;


    public Posts savePosts(Posts post){
        System.out.println("id ----> "+post.getId());
        redisTemplate.boundSetOps("sortKey").add(post.getId());
        BoundHashOperations x = redisTemplate.boundHashOps("hash" + post.getId());
        x.put("id",post.getId());
         x.put("message", post.getMessage());
         x.put("likes",post.getLikes());
         x.put("comments",post.getComments());

        return post;
    }

    public Object findPostbyId(String id){
        System.out.println("Inside find by id");
        return redisTemplate.boundHashOps("hash"+id).entries();
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
        return postRedisRepo.findAll(PageRequest.of(pageNo,pageSize, Sort.by("likes").descending()));
    }

    public Posts findPostByMessage(String message){
        return postRedisRepo.findByMessage(message);
    }

    public Object findAllPosts(int start, int end){
        SortParameters.Order order = SortParameters.Order.DESC;
        List list = redisTemplate.sort(SortQueryBuilder.sort("sortKey")
                .by("hash*->message")
                        .order(order)
                        .limit(start*end,end)
                .build());
        List x = new ArrayList();
        list.forEach(d->{
            x.add(redisTemplate.boundHashOps("hash"+d).entries());
        });

        Map<String, Object> mapper = new HashMap<>();
        mapper.put("content", x);
        mapper.put("sorted", true);
        mapper.put("total_elements", x.size());

        return mapper;
    }

}
