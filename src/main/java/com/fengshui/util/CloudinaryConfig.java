package com.fengshui.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class CloudinaryConfig {
    
    private static final String CLOUD_NAME = "r3ftqtcb";
    private static final String API_KEY = "331255455129789";
    private static final String API_SECRET = "X3P6QeVQ7joiMtPRNNquOSaRTSE";

    private static Cloudinary cloudinary;

    // Sử dụng Singleton Pattern để chỉ tạo 1 object Cloudinary duy nhất
    public static Cloudinary getInstance() {
        if (cloudinary == null) {
            cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET,
                "secure", true
            ));
        }
        return cloudinary;
    }
}
