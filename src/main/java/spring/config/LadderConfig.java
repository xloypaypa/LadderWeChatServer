package spring.config;

import model.config.ConfigResourceManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import spring.tools.RSA;

import java.security.PrivateKey;
import java.security.PublicKey;

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

    @Value("#{'${username}'}")
    private String username;

    @Value("#{'${password}'}")
    private String password;

    private PublicKey serverPublicKey = null, localPublicKey = null;
    private PrivateKey localPrivateKey = null;

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public PublicKey getServerKey() {
        initKey();
        return serverPublicKey;
    }

    public PublicKey getPublicKey() {
        initKey();
        return localPublicKey;
    }

    public PrivateKey getPrivateKey() {
        initKey();
        return localPrivateKey;
    }

    public int getMaxConnectionNumber() {
        return maxConnectionNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    private void initKey() {
        if (serverPublicKey == null) {
            synchronized (LadderConfig.class) {
                if (serverPublicKey == null) {
                    ConfigResourceManager configResourceManager = ConfigResourceManager.getConfigResourceManager();
                    serverPublicKey = RSA.bytes2PublicKey(configResourceManager.getResource(serverKey));
                    localPublicKey = RSA.bytes2PublicKey(configResourceManager.getResource(publicKey));
                    localPrivateKey = RSA.bytes2PrivateKey(configResourceManager.getResource(privateKey));
                }
            }
        }
    }
}
