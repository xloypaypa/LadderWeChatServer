package spring.logic;

import net.sf.json.JSONObject;
import spring.config.LadderConfig;
import spring.service.session.SessionManager;
import tools.ProtocolBuilder;

import java.io.IOException;

/**
 * Created by xsu on 16/10/13.
 * it's the logic for ask password
 */
class AskPasswordLogic extends WeChatLogic {

    private boolean forBind;
    private String username;

    AskPasswordLogic(SessionManager sessionManager, LadderConfig ladderConfig, boolean forBind, String username) {
        super(sessionManager, ladderConfig);
        this.forBind = forBind;
        this.username = username;
    }

    @Override
    public String getReplyFromServer() {
        return "Please input your password";
    }

    @Override
    public WeChatLogic getReplyFromUser(String weChatId, String messageType, String password) {
        try {
            createSession(weChatId);
        } catch (IOException e) {
            return new ExceptionLogic(this.sessionManager, ladderConfig, "can't connect");
        }

        try {
            askLadderServer(weChatId, ProtocolBuilder.key(ladderConfig.getPublicKey()), 500);
            sessionManager.getSessionMessage(weChatId).getLadderServerSolver().setEncrypt(true);

            LadderReply sessionReply = askLadderServer(weChatId, ProtocolBuilder.getSessionId(), 500);
            String sessionId = JSONObject.fromObject(new String(sessionReply.getBody())).getString("result");

            askLadderServer(weChatId,
                    ProtocolBuilder.login(ladderConfig.getUsername(), ladderConfig.getPassword(), sessionId), 500);

            LadderReply ladderReply;
            if (forBind) {
                ladderReply = askLadderServer(weChatId,
                        ProtocolBuilder.bindUserAndWeChat(username, password, sessionId, weChatId), 500);
            } else {
                ladderReply = askLadderServer(weChatId,
                        ProtocolBuilder.register(username, password), 500);
            }

            closeSession(weChatId);

            return new TestLogic(this.sessionManager, this.ladderConfig, new String(ladderReply.getBody()));
        } catch (Exception e) {
            closeSession(weChatId);
            return new ExceptionLogic(this.sessionManager, ladderConfig, "time out");
        }
    }
}
