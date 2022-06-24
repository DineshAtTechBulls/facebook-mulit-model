package org.example.controller;

import org.example.PostRedisService;
import org.example.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@RestController
@CacheConfig(cacheNames = "Posts")
public class Posts {

    @Autowired
    PostService postService;

    @Autowired
    PostRedisService postRedisService;

    @GetMapping("/posts")
    public Object getPost(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int size){
        return postRedisService.findAllPosts(pageNo, size);
    }

    @GetMapping("/post/message")
    public org.example.Posts getPostByMessage(@RequestParam("msg") String message){
        return postRedisService.findPostByMessage(message);
    }

    @GetMapping("/post/{id}")
    @Cacheable(value = "Posts",key="#id")
    public org.example.Posts getPostById(@PathVariable("id") Long id){
        return postService.getPostById(id).get();
    }


    @PostMapping("/post")
    @CachePut(value="Posts", key="#post.id")
    @CacheEvict(value = "Posts", allEntries=true)
    public org.example.Posts createPost(@RequestBody org.example.Posts post){
        return postService.createPost(post);
    }

    @DeleteMapping("/post/{id}")
    @CacheEvict(value = "Posts", allEntries=true, key = "#id")
    public boolean deletePost(@PathVariable Long id){
        return postService.deletePost(id);
    }

}
