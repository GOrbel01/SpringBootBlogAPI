package org.fsq.blogapi.repository;

import org.bson.types.ObjectId;
import org.fsq.blogapi.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {

}
