package spring.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.config.ServerConfig;
import tools.SHA1;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by xsu on 16/10/8.
 * it's the token service
 */
@Service
public class TokenService {

    @Autowired
    private ServerConfig serverConfig;

    private final static Logger logger = Logger.getLogger(TokenService.class);

    public boolean validateToken(HttpServletRequest request) {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        if (signature != null && timestamp != null && nonce != null ) {
            String[] strSet = new String[] { serverConfig.getToken(), timestamp, nonce };
            java.util.Arrays.sort(strSet);
            String key = "";
            for (String string : strSet) {
                key = key + string;
            }
            String pwd = SHA1.encode(key);
            logger.warn("validate token success.");
            return signature.equals(pwd);
        }else {
            logger.warn("validate token failed.");
            return false;
        }
    }

}
