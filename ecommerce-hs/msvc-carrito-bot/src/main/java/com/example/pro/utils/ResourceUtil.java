package com.example.pro.utils;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

public class ResourceUtil {

    public static Resource byteArrayToResource(byte[] data, String filename) {
        return new ByteArrayResource(data) {
            @Override
            public String getFilename() {
                return filename; // muy importante
            }
        };
    }
}
