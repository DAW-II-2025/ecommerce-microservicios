package pe.edu.cibertec.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dwytuu7v6",
                "api_key", "317819869571194",
                "api_secret", "1jiyy3wx2Jd8uvtnsbdWGUPk_BM",
                "secure", true
        ));
    }
}
