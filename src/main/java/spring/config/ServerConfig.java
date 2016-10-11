package spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.aes.AesException;
import tools.aes.WXBizMsgCrypt;

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

    private WXBizMsgCrypt wxBizMsgCrypt = null;

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

    public WXBizMsgCrypt getWxBizMsgCrypt() throws AesException {
        if (this.wxBizMsgCrypt == null) {
            synchronized (ServerConfig.class) {
                if (this.wxBizMsgCrypt == null) {
                    wxBizMsgCrypt = new WXBizMsgCrypt(this.getToken(), this.getEncodingAESKey(), this.getAppId());
                }
            }
        }
        return wxBizMsgCrypt;
    }
}