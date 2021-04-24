package com.smh.bs.server.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "bundles")
public class Bundle {

    @Id
    private String id;

    private int count;

    private int bytes;

    @CreatedDate
    private long createdTime;

    private long expirationTime;

}
