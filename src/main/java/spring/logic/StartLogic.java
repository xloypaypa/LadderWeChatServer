package spring.logic;

import net.sf.json.JSONObject;
import spring.config.LadderConfig;
import spring.service.session.SessionManager;
import spring.tools.RSA;
import tools.ProtocolBuilder;

import java.io.IOException;
import java.security.KeyPair;

/**
 * Created by xsu on 16/10/12.
 * it's the logic when start connections
 */
public class StartLogic extends WeChatLogic {

    public StartLogic(SessionManager sessionManager, LadderConfig ladderConfig) {
        super(sessionManager, ladderConfig);
    }

    @Override
    public String getReplyFromServer() {
        return null;
    }

    @Override
    public WeChatLogic getReplyFromUser(String weChatId, String messageType, String message) {
        try {
            createSession(weChatId);
        } catch (IOException e) {
            return new ExceptionLogic(this.sessionManager, ladderConfig, "can't connect");
        }

        try {
            KeyPair keyPair = RSA.buildKeyPair();
            askLadderServer(weChatId, ProtocolBuilder.key(keyPair.getPublic()), 1000);
            sessionManager.getSessionMessage(weChatId).getLadderServerSolver().setPrivateKey(keyPair.getPrivate());
            sessionManager.getSessionMessage(weChatId).getLadderServerSolver().setEncrypt(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LadderReply sessionReply = askLadderServer(weChatId, ProtocolBuilder.getSessionId(), 1000);
        String sessionId = JSONObject.fromObject(new String(sessionReply.getBody())).getString("result");

        closeSession(weChatId);

        return new TestLogic(sessionManager, ladderConfig, sessionId);
    }
}
