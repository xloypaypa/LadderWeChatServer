package spring.logic;

import net.sf.json.JSONObject;
import spring.config.LadderConfig;
import spring.service.session.SessionManager;
import tools.ProtocolBuilder;

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
        return "back to start logic";
    }

    @Override
    WeChatLogic solveLadderLogic(String weChatId, String messageType, String message) throws Exception {
        askLadderServer(weChatId, ProtocolBuilder.key(ladderConfig.getPublicKey()), 500);
        sessionManager.getSessionMessage(weChatId).getLadderServerSolver().setEncrypt(true);

        LadderReply sessionReply = askLadderServer(weChatId, ProtocolBuilder.getSessionId(), 500);
        String sessionId = JSONObject.fromObject(new String(sessionReply.getBody())).getString("result");

        askLadderServer(weChatId,
                ProtocolBuilder.login(ladderConfig.getUsername(), ladderConfig.getPassword(), sessionId), 500);

        LadderReply changeReply = askLadderServer(weChatId,
                ProtocolBuilder.changeConnectionUserByWeChat(weChatId), 500);
        String result = JSONObject.fromObject(new String(changeReply.getBody())).getString("result");

        switch (result) {
            case "fail":
                return new ExceptionLogic(this.sessionManager, ladderConfig, "server error");
            case "unbind account":
                return new BindLogic(this.sessionManager, this.ladderConfig);
            case "ok":
                return new AppOrUnbindLogic(this.sessionManager, this.ladderConfig);
            default:
                return new TestLogic(sessionManager, ladderConfig, new String(changeReply.getBody()));
        }
    }
}
