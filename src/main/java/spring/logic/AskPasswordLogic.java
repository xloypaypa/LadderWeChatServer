package spring.logic;

import net.sf.json.JSONObject;
import spring.config.LadderConfig;
import spring.service.cache.UserStatus;
import spring.service.session.SessionManager;
import tools.ProtocolBuilder;

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
    public void solveLadderLogic(UserStatus userStatus, String weChatId, String messageType, String password) throws Exception {
        askLadderServer(weChatId, ProtocolBuilder.key(ladderConfig.getPublicKey()));
        sessionManager.getSessionMessage(weChatId).getLadderServerSolver().setEncrypt(true);

        LadderReply sessionReply = askLadderServer(weChatId, ProtocolBuilder.getSessionId());
        String sessionId = JSONObject.fromObject(new String(sessionReply.getBody())).getString("result");

        askLadderServer(weChatId,
                ProtocolBuilder.login(ladderConfig.getUsername(), ladderConfig.getPassword(), sessionId));

        if (forBind) {
            askLadderServer(weChatId,
                    ProtocolBuilder.bindUserAndWeChat(username, password, sessionId, weChatId));
            userStatus.addNewLogic(new AppOrUnbindLogic(this.sessionManager, this.ladderConfig));
        } else {
            askLadderServer(weChatId,
                    ProtocolBuilder.register(username, password));
            askLadderServer(weChatId,
                    ProtocolBuilder.bindUserAndWeChat(username, password, sessionId, weChatId));
            userStatus.addNewLogic(new StartLogic(this.sessionManager, this.ladderConfig));
        }
    }
}
