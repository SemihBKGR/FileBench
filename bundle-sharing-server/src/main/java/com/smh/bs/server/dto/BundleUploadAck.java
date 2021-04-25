package com.smh.bs.server.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BundleUploadAck {

    private long timestamp;
    private long uploadedSize;

}
