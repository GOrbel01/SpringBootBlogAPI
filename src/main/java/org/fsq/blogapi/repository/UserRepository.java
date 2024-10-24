package org.fsq.blogapi.repository;

import org.bson.types.ObjectId;
import org.fsq.blogapi.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User,ObjectId>,PagingAndSortingRepository<User, ObjectId>, CrudRepository<User,ObjectId> {
    User findUserByUsername(String id);
}
