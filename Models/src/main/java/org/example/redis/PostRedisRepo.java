package org.example.redis;

import org.example.Posts;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PostRedisRepo extends PagingAndSortingRepository<Posts,Long> {
}
