package tools;

import net.sf.json.JSONObject;
import spring.tools.PasswordEncoder;
import spring.tools.RSA;

import java.security.PublicKey;

/**
 * Created by xlo on 16/2/23.
 * it's the protocol builder
 */
public class ProtocolBuilder {

    public static byte[] getSessionId() {
        return "/getSessionID#{}".getBytes();
    }

    public static byte[] key(PublicKey publicKey) {
        byte[] key = RSA.publicKey2Bytes(publicKey);
        byte[] url = "/key#".getBytes();
        byte[] all = new byte[url.length + key.length];
        System.arraycopy(url, 0, all, 0, url.length);
        System.arraycopy(key, 0, all, url.length, key.length);
        return all;
    }

    public static byte[] login(String username, String password, String sessionID) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", PasswordEncoder.encode(password + sessionID));
        String body = jsonObject.toString();
        return ("/login#" + body).getBytes();
    }

    public static byte[] changeConnectionUserByWeChat(String weChatId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("weChatId", weChatId);
        String body = jsonObject.toString();
        return ("changeConnectionUserByWeChat#" + body).getBytes();
    }

    public static byte[] bindUserAndWeChat(String username, String password, String sessionID, String weChatId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", PasswordEncoder.encode(password + sessionID));
        jsonObject.put("weChatId", weChatId);
        String body = jsonObject.toString();
        return ("bindUserAndWeChat#" + body).getBytes();
    }

    public static byte[] unbindUserAndWeChat(String weChatId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("weChatId", weChatId);
        String body = jsonObject.toString();
        return ("unbindUserAndWeChat#" + body).getBytes();
    }

    public static byte[] register(String username, String password) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", password);
        String body = jsonObject.toString();
        return ("/register#" + body).getBytes();
    }

    public static byte[] useApp(String appName) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appName", appName);
        String body = jsonObject.toString();
        return ("/useApp#" + body).getBytes();
    }

    //wallet part
    public static byte[] getMoney() {
        return "getMoney#{}".getBytes();
    }

    public static byte[] getBudget() {
        return "getBudget#{}".getBytes();
    }

    public static byte[] rollBack() {
        return "rollBack#{}".getBytes();
    }

    public static byte[] useMoney(String typename, String budgetType, double value) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("typename", typename);
        jsonObject.put("value", value);
        jsonObject.put("budgetType", budgetType);
        String body = jsonObject.toString();
        return ("useMoney#" + body).getBytes();
    }

}
