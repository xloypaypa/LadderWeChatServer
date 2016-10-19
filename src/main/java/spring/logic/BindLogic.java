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
    protected WeChatLogic solveLadderLogic(String weChatId, String messageType, String message) throws Exception {
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
