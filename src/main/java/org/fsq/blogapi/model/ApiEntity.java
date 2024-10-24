package org.fsq.blogapi.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class ApiEntity {
    @Id
    @Field("_id")
    @JsonSerialize(using= ToStringSerializer.class)
    private ObjectId id;
}
