package org.fsq.blogapi.controller;

import org.fsq.blogapi.model.Blog;
import org.fsq.blogapi.repository.BlogRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BlogControllerTests {
    @Autowired
    private BlogController blogController;

    @Autowired
    private BlogRepository blogRepository;

    @Test
    void contextLoads() throws Exception {
        assertThat(blogController).isNotNull();
    }

    @BeforeEach
    public void setup() {
        Blog blog = new Blog();
        blog.setAuthor("Barry A");
        blog.setUrl("www.omg.com");
        blog.setTitle("How to catch fish");
        blog.setLikes(0);

        blogRepository.save(blog);
    }

    @Test
    void blogContrllerFindsItems() {
        assertThat(blogController.getBlogs()).size().isGreaterThan(0);
        blogController.getBlogs().forEach(blg -> System.out.println(blg.getAuthor()));
    }

    @AfterAll
    public void tearDown() {

    }
}
