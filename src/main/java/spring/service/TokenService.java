package spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.config.TokenConfig;
import tools.SHA1;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by xsu on 16/10/8.
 * it's the token service
 */
@Service
public class TokenService {

    @Autowired
    private TokenConfig tokenConfig;

    public boolean validateToken(HttpServletRequest request) {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        if (signature != null && timestamp != null && nonce != null ) {
            String[] strSet = new String[] { tokenConfig.getToken(), timestamp, nonce };
            java.util.Arrays.sort(strSet);
            String key = "";
            for (String string : strSet) {
                key = key + string;
            }
            String pwd = SHA1.encode(key);
            return pwd.equals(signature);
        }else {
            return false;
        }
    }

}
