package spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServerConfig {

    @Value("#{'${appId}'}")
    private String appId;

    @Value("#{'${originalId}'}")
    private String originalId;

    @Value("#{'${token}'}")
    private String token;

    @Value("#{'${EncodingAESKey}'}")
    private String encodingAESKey;

    public String getAppId() {
        return appId;
    }

    public String getOriginalId() {
        return originalId;
    }

    public String getToken() {
        return token;
    }

    public String getEncodingAESKey() {
        return encodingAESKey;
    }
}