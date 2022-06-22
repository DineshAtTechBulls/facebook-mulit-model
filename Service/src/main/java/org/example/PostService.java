package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    PostRepo postRepo;

   public Page<Posts> getPosts(){
       Pageable page = PageRequest.of(0, 5);
       return postRepo.findAll(page);
   }

   public Optional<Posts> getPostById(Long id){
       return Optional.of(postRepo.findById(id)).get();
   }

   public Posts createPost(Posts post){
       return postRepo.save(post);
   }

   public boolean deletePost(Long id){
       try {
           postRepo.deleteById(id);
           return true;
       }catch (Exception e){
           return false;
       }
   }
}
