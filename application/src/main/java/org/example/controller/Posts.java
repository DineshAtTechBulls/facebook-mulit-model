package org.example.controller;

import org.example.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Posts {

    @Autowired
    PostService postService;

    @GetMapping("/")
    public List<org.example.Posts> getPost(){
        return postService.getPosts();
    }

    @GetMapping("/post/{id}")
    public org.example.Posts getPostById(@PathVariable("id") Long id){
        return postService.getPostById(id).get();
    }


    @PostMapping("/post")
    public org.example.Posts createPost(@RequestBody org.example.Posts post){
        return postService.createPost(post);
    }

}
