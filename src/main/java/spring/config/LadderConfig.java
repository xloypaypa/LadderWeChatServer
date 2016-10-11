package spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by xsu on 16/10/11.
 * it's the config for ladder gateway
 */
@Component
public class LadderConfig {

    @Value("#{'${ip}'}")
    private String ip;

    @Value("#{'${port}'}")
    private int port;

    @Value("#{'${serverKey}'}")
    private String serverKey;

    @Value("#{'${publicKey}'}")
    private String publicKey;

    @Value("#{'${privateKey}'}")
    private String privateKey;

    @Value("#{'${maxConnectionNumber}'}")
    private int maxConnectionNumber;

    @Value("#{'${timeOut}'}")
    private long timeOut;

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getServerKey() {
        return serverKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public int getMaxConnectionNumber() {
        return maxConnectionNumber;
    }

    public long getTimeOut() {
        return timeOut;
    }
}
