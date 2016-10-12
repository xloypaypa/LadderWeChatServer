package spring.logic;

import net.sf.json.JSONObject;
import spring.config.LadderConfig;
import spring.service.session.SessionManager;
import tools.ProtocolBuilder;

import java.io.IOException;

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
            askLadderServer(weChatId, ProtocolBuilder.key(ladderConfig.getPublicKey()), 1000);
            sessionManager.getSessionMessage(weChatId).getLadderServerSolver().setEncrypt(true);

            LadderReply sessionReply = askLadderServer(weChatId, ProtocolBuilder.getSessionId(), 1000);
            String sessionId = JSONObject.fromObject(new String(sessionReply.getBody())).getString("result");

            LadderReply loginReply = askLadderServer(weChatId,
                    ProtocolBuilder.login(ladderConfig.getUsername(), ladderConfig.getPassword(), sessionId), 100);

            closeSession(weChatId);
            return new TestLogic(sessionManager, ladderConfig, new String(loginReply.getBody()));
        } catch (Exception e) {
            closeSession(weChatId);
            return new ExceptionLogic(this.sessionManager, ladderConfig, "time out");
        }
    }
}
