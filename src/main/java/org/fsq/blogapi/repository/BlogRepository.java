package org.fsq.blogapi.repository;

import org.bson.types.ObjectId;
import org.fsq.blogapi.model.Blog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends MongoRepository<Blog, ObjectId> {
}
