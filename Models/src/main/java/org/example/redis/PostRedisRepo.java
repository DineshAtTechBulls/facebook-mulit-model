package org.example.redis;

import org.example.Posts;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PostRedisRepo extends PagingAndSortingRepository<Posts,Long> {
    Posts findByMessage(String message);

}
