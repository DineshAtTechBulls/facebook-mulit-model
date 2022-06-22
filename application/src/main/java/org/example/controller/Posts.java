package org.example.controller;

import org.example.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Posts {

    @Autowired
    PostService postService;

    @GetMapping("/")
    @Cacheable(value = "Posts",key="#id")
    public List<org.example.Posts> getPost(){
        return postService.getPosts();
    }

    @GetMapping("/post/{id}")
    @Cacheable(value = "Posts",key="#id")
    public org.example.Posts getPostById(@PathVariable("id") Long id){
        return postService.getPostById(id).get();
    }


    @PostMapping("/post")
    @CachePut(cacheNames="Posts")
    public org.example.Posts createPost(@RequestBody org.example.Posts post){
        return postService.createPost(post);
    }

    @DeleteMapping("/post/{id}")
    @CacheEvict(value = "Posts", allEntries=true)
    public boolean deletePost(@PathVariable Long id){
        return postService.deletePost(id);
    }

}
