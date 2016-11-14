package spring.logic;

import spring.config.LadderConfig;
import spring.service.cache.UserStatus;
import spring.service.session.SessionManager;

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
    protected void solveLadderLogic(UserStatus userStatus, String weChatId, String messageType, String message) throws Exception {
        switch (message) {
            case "1":
                userStatus.addNewLogic(new AskUserNameLogic(this.sessionManager, this.ladderConfig, true));
                break;
            case "2":
                userStatus.addNewLogic(new AskUserNameLogic(this.sessionManager, this.ladderConfig, false));
                break;
            default:
                userStatus.addNewLogic(new StartLogic(this.sessionManager, this.ladderConfig, "invalidate input"));
                break;
        }
    }

}
