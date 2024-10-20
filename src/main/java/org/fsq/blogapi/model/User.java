package org.fsq.blogapi.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("users")
public class User {
    private String username;
    private String name;
    private String passwordHash;
    @DBRef
    private List<Blog> blogs;
}
