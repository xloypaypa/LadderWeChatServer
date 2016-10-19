package spring.config;

import spring.tools.RSA;

import java.security.PublicKey;

/**
 * Created by xsu on 16/10/17.
 * it's the mock ladder config
 */
public class MockLadderConfig extends LadderConfig {

    private PublicKey publicKey;

    public MockLadderConfig() {
        try {
            this.publicKey = RSA.buildKeyPair().getPublic();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getUsername() {
        return "username";
    }

    @Override
    public String getPassword() {
        return "password";
    }

    @Override
    public PublicKey getPublicKey() {
        try {
            return publicKey;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String[] getAvailableApp() {
        return new String[]{"wallet"};
    }
}