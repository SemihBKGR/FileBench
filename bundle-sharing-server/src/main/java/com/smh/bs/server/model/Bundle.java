package com.smh.bs.server.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "bundles")
public class Bundle {

    @Id
    private String id;

    private String name;

    private List<Resource> resources;

    private long size;

    @CreatedDate
    private long createdTime;

    private long expireTime;

}
