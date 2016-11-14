package spring.logic;

import net.sf.json.JSONObject;
import spring.config.LadderConfig;
import spring.service.cache.UserStatus;
import spring.service.session.SessionManager;

/**
 * Created by xsu on 16/10/12.
 * it's the logic when start connections
 */
public class StartLogic extends WeChatLogic {

    private String message;

    public StartLogic(SessionManager sessionManager, LadderConfig ladderConfig) {
        super(sessionManager, ladderConfig);
        this.message = null;
    }

    public StartLogic(SessionManager sessionManager, LadderConfig ladderConfig, String message) {
        super(sessionManager, ladderConfig);
        this.message = message;
    }

    @Override
    public String getReplyFromServer() {
        if (this.message != null) {
            return this.message;
        } else {
            return "back to start logic";
        }
    }

    @Override
    protected void solveLadderLogic(UserStatus userStatus, String weChatId, String messageType, String message) throws Exception {
        LadderReply changeReply = loginAsUser(weChatId);
        String result = JSONObject.fromObject(new String(changeReply.getBody())).getString("result");

        switch (result) {
            case "fail":
                userStatus.addNewLogic(new StartLogic(this.sessionManager, ladderConfig, "server error"));
                break;
            case "unbind account":
                userStatus.addNewLogic(new BindLogic(this.sessionManager, this.ladderConfig));
                break;
            case "ok":
                userStatus.addNewLogic(new AppOrUnbindLogic(this.sessionManager, this.ladderConfig));
                break;
            default:
                userStatus.addNewLogic(new TestLogic(sessionManager, ladderConfig, new String(changeReply.getBody())));
                break;
        }
    }
}
