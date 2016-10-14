package spring.logic;

import net.sf.json.JSONObject;
import spring.config.LadderConfig;
import spring.service.session.SessionManager;
import tools.ProtocolBuilder;

/**
 * Created by xsu on 16/10/12.
 * it's the logic to solve unbind user
 */
class BindLogic extends WeChatLogic {

    BindLogic(SessionManager sessionManager, LadderConfig ladderConfig) {
        super(sessionManager, ladderConfig);
    }

    @Override
    public String getReplyFromServer() {
        return "you not bind account. reply 1 to bind; 2 to register";
    }

    @Override
    WeChatLogic solveLadderLogic(String weChatId, String messageType, String message) throws Exception {
        askLadderServer(weChatId, ProtocolBuilder.key(ladderConfig.getPublicKey()), 500);
        sessionManager.getSessionMessage(weChatId).getLadderServerSolver().setEncrypt(true);

        LadderReply sessionReply = askLadderServer(weChatId, ProtocolBuilder.getSessionId(), 500);
        String sessionId = JSONObject.fromObject(new String(sessionReply.getBody())).getString("result");

        askLadderServer(weChatId,
                ProtocolBuilder.login(ladderConfig.getUsername(), ladderConfig.getPassword(), sessionId), 500);

        switch (message) {
            case "1":
                return new AskUserNameLogic(this.sessionManager, this.ladderConfig, true);
            case "2":
                return new AskUserNameLogic(this.sessionManager, this.ladderConfig, false);
            default:
                return new ExceptionLogic(this.sessionManager, this.ladderConfig, "invalidate input");
        }
    }

}
