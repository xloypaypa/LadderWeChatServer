package spring.logic;

import net.sf.json.JSONObject;
import spring.config.LadderConfig;
import spring.service.session.SessionManager;
import tools.ProtocolBuilder;

/**
 * Created by xsu on 16/10/13.
 * it's the logic ask user use app or unbind
 * it's maybe have more logic
 */
class AppOrUnbindLogic extends WeChatLogic {

    AppOrUnbindLogic(SessionManager sessionManager, LadderConfig ladderConfig) {
        super(sessionManager, ladderConfig);
    }

    @Override
    public String getReplyFromServer() {
        return "Please reply 1 to Use application, 2 to unbind your account";
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
                return new UseAppLogic(this.sessionManager, this.ladderConfig);
            case "2":
                askLadderServer(weChatId,
                        ProtocolBuilder.unbindUserAndWeChat(weChatId), 500);
                return new StartLogic(this.sessionManager, this.ladderConfig);
            default:
                return new ExceptionLogic(this.sessionManager, ladderConfig, "invalidate input");
        }
    }

}
