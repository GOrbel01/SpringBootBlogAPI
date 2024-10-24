package org.fsq.blogapi.controller;

import org.fsq.blogapi.model.*;
import org.fsq.blogapi.repository.BlogRepository;
import org.fsq.blogapi.repository.UserRepository;
import org.fsq.blogapi.service.JwtService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
/* Blog Controller Integration Tests */
public class BlogControllerTests {
    @Autowired
    private BlogController blogController;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Value("${spring.profiles.active}")
    private String profile;

    @Test
    void contextLoads() {
        assertThat(blogController).isNotNull();
    }

    @BeforeAll
    public void globalSetup() {
        assertEquals("test",profile);
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testpw");
        user.setPasswordHash(passwordEncoder.encode(user.getPassword()));
        user.setName("Test User");
        userRepository.save(user);
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
    void blogControllerFindsItems() {
        ParameterizedTypeReference<List<Blog>> ref = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<Blog>> res = this.testRestTemplate.exchange("/api/blogs", HttpMethod.GET,null, ref);
        assertNotNull(res);
        assertNotNull(res.getBody());
        assertEquals(200,res.getStatusCode().value());
        assertThat(res.getBody()).size().isGreaterThan(0);
        assertEquals("Barry A",res.getBody().get(0).getAuthor());
    }

    @Test
    void canPostNewBlog() {
        Blog blog = getSimpleBlog();

        ResponseEntity<Response> res = this.testRestTemplate.postForEntity("/api/blogs",blog,Response.class);
        assertEquals(HttpStatus.CREATED,res.getStatusCode());
        ResponseEntity<List<Blog>> blogs = getBlogs();
        assertEquals(2,blogs.getBody().size());
    }

    @Test
    void blogPostTitleCannotBeNullOrBlank() {
        Blog blog = getSimpleBlog();
        blog.setTitle("");
        ResponseEntity<Response> res = this.testRestTemplate.postForEntity("/api/blogs",blog,Response.class);
        assertEquals(HttpStatus.BAD_REQUEST,res.getStatusCode());
        assertNotNull(res.getBody());
        assertTrue(res.getBody().getMessage().contains("title") && res.getBody().getMessage().contains("must not be blank"));
        ResponseEntity<List<Blog>> blogs = getBlogs();
        assertEquals(1,blogs.getBody().size());
    }

    private ResponseEntity<List<Blog>> getBlogs() {
        ParameterizedTypeReference<List<Blog>> ref = new ParameterizedTypeReference<>() {};
        return this.testRestTemplate.exchange("/api/blogs", HttpMethod.GET,null, ref);
    }

    @Test
    void canDeleteBlog() {
        Blog blog = getSimpleBlog();
        blog.setUser(userRepository.findUserByUsername("testUser"));
        blogController.addBlog(blog);

        List<Blog> blogs = blogController.getBlogs();
        Blog toDelete = blogs.stream().filter(b -> b.getTitle().equals(blog.getTitle())).findFirst().get();

        ResponseEntity<Response> res = null;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer " + jwtService.generateToken("testUser"));
        try {
            res = this.testRestTemplate.exchange("/api/blogs/" + toDelete.getId().toString(), HttpMethod.DELETE, new HttpEntity<>(httpHeaders), Response.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        assertEquals(HttpStatus.OK,res.getStatusCode());
    }

    public Blog getSimpleBlog() {
        Blog blog = new Blog();
        blog.setAuthor("James B");
        blog.setUrl("www.zomg.com");
        blog.setTitle("How to jump really high");
        blog.setLikes(4);
        return blog;
    }

    @AfterEach
    public void cleanup() {
        blogRepository.deleteAll();
    }

    @AfterAll
    public void tearDown() {
        User user = userRepository.findUserByUsername("testUser");
        userRepository.delete(user);
    }


    @TestConfiguration(proxyBeanMethods = false)
    static class RestTemplateBuilderConfiguration {

        @Bean
        RestTemplateBuilder restTemplateBuilder() {
            return new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(1))
                    .setReadTimeout(Duration.ofSeconds(1));
        }

    }
}
