package spring.logic;

import net.sf.json.JSONObject;
import spring.service.session.SessionManager;
import tools.ProtocolBuilder;

import java.io.IOException;

/**
 * Created by xsu on 16/10/12.
 * it's the logic when start connections
 */
public class StartLogic extends WeChatLogic {

    public StartLogic(SessionManager sessionManager) {
        super(sessionManager);
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
            return new ExceptionLogic(this.sessionManager, "can't connect");
        }

        LadderReply sessionReply = askLadderServer(weChatId, ProtocolBuilder.getSessionId(), 1000);
        String sessionId = JSONObject.fromObject(new String(sessionReply.getBody())).getString("result");

        closeSession(weChatId);

        return new TestLogic(sessionManager, sessionId);
    }
}
