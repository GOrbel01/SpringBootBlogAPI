package org.fsq.blogapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document("blogs")
public class Blog {
    @Id
    @Field("id")
    @JsonIgnore
    private ObjectId id;
    private String author;
    private String title;
    private String url;
    @DBRef
    private User user;
    private int likes;
}
