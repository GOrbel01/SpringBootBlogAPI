package org.fsq.blogapi.controller;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.fsq.blogapi.model.Blog;
import org.fsq.blogapi.model.Response;
import org.fsq.blogapi.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/blogs",produces = MediaType.APPLICATION_JSON_VALUE)
public class BlogController {

    private final BlogRepository blogRepository;

    @Autowired
    public BlogController(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @GetMapping
    public List<Blog> getBlogs() {
        List<Blog> blogs = blogRepository.findAll();
        blogs.forEach(blog -> {
            if (blog.getUser() != null) {
                blog.getUser().setBlogs(null);
            }
        });
        return blogs;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Blog> getBlog(@PathVariable ObjectId id) {
        Optional<Blog> result = blogRepository.findById(id);
        return result.map(blog -> ResponseEntity.status(HttpStatus.OK).body(blog)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Response> addBlog(@Valid @RequestBody Blog blog) {
        if (blog.getLikes() == 0) {
            blog.setLikes(0);
        }

        blogRepository.save(blog);
        Response response = new Response(HttpStatus.CREATED,"Blog successfully added.");
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteBlog(@PathVariable ObjectId id, Authentication authentication) {
        System.out.println("CALLING  DEL REQ");
        Blog blog = blogRepository.findById(id).get();
        if (blog.getUser().getUsername().equalsIgnoreCase(authentication.getName())) {
            blogRepository.deleteById(id);
            Response response = new Response(HttpStatus.OK,"Deleted Blog successfully.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            Response response = new Response(HttpStatus.UNAUTHORIZED,"User not Authrorized for this Operation");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<Response> getTestResponse() {
        Response response = new Response(HttpStatus.OK,"Test Message.");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}