package com.smh.bs.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BundleInformation {

    private String name;
    private long size;
    private long expireTime;
    private List<ResourceInformation> resources;

}
