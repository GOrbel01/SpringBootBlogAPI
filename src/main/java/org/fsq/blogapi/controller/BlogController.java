package org.fsq.blogapi.controller;
import org.bson.types.ObjectId;
import org.fsq.blogapi.model.Blog;
import org.fsq.blogapi.model.Response;
import org.fsq.blogapi.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class BlogController {

    private final BlogRepository blogRepository;

    @Value("${spring.profiles.active}")
    private String profile;

    @Autowired
    public BlogController(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @GetMapping("/blogs")
    public List<Blog> getBlogs() {
        return blogRepository.findAll();
    }

    @GetMapping("/blogs/{id}")
    public ResponseEntity<Blog> getBlog(@PathVariable ObjectId id) {
        Optional<Blog> result = blogRepository.findById(id);
        return result.map(blog -> ResponseEntity.status(HttpStatus.OK).body(blog)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/blogs")
    public ResponseEntity<Response> addBlog(@RequestBody Blog blog) {
        System.out.println(profile);
        blogRepository.save(blog);
        Response response = new Response(HttpStatus.CREATED,"Blog successfully added.");
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
    }

    @DeleteMapping("/blogs/{id}")
    public ResponseEntity<Response> deleteBlog(@PathVariable ObjectId id) {
        blogRepository.deleteById(id);
        Response response = new Response(HttpStatus.OK,"Deleted Blog successfully.");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<Response> getTestResponse() {
        Response response = new Response(HttpStatus.OK,"Test Message.");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}