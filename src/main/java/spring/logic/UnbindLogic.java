package spring.logic;

import net.sf.json.JSONObject;
import spring.config.LadderConfig;
import spring.service.session.SessionManager;
import tools.ProtocolBuilder;

import java.io.IOException;

/**
 * Created by xsu on 16/10/12.
 * it's the logic to solve unbind user
 */
class UnbindLogic extends WeChatLogic {

    UnbindLogic(SessionManager sessionManager, LadderConfig ladderConfig) {
        super(sessionManager, ladderConfig);
    }

    @Override
    public String getReplyFromServer() {
        return "you not bind account. reply 1 to bind; 2 to register";
    }

    @Override
    public WeChatLogic getReplyFromUser(String weChatId, String messageType, String message) {
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

            closeSession(weChatId);

            if (message.equals("1")) {
                return new AskUserNameLogic(this.sessionManager, this.ladderConfig, true);
            } else if (message.equals("2")) {
                return new AskUserNameLogic(this.sessionManager, this.ladderConfig, false);
            } else {
                return new ExceptionLogic(this.sessionManager, this.ladderConfig, "invalidate input");
            }
        } catch (Exception e) {
            closeSession(weChatId);
            return new ExceptionLogic(this.sessionManager, ladderConfig, "time out");
        }
    }
}
