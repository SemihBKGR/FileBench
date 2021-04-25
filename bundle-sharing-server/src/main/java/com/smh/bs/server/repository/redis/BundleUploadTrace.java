package com.smh.bs.server.repository.redis;

import com.smh.bs.server.model.Bundle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BundleUploadTrace implements Serializable {

    private String bundleId;
    private String bundleName;
    private long uploadedSize;
    private long size;
    private int uploadedResourceCount;
    private int resourceCount;
    private long startTime;

    public static BundleUploadTrace of(Bundle bundle){
        return BundleUploadTrace.builder()
                .bundleId(bundle.getId())
                .bundleName(bundle.getName())
                .uploadedSize(0L)
                .size(bundle.getSize())
                .uploadedResourceCount(0)
                .resourceCount(bundle.getResources().size())
                .startTime(bundle.getCreatedTime())
                .build();
    }

}
