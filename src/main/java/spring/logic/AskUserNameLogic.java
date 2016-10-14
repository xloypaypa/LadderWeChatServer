package spring.logic;

import spring.config.LadderConfig;
import spring.service.session.SessionManager;

/**
 * Created by xsu on 16/10/13.
 * it's the logic for ask username
 */
class AskUserNameLogic extends WeChatLogic {

    private boolean forBind;

    AskUserNameLogic(SessionManager sessionManager, LadderConfig ladderConfig, boolean forBind) {
        super(sessionManager, ladderConfig);
        this.forBind = forBind;
    }

    @Override
    public String getReplyFromServer() {
        return "Please input your username";
    }

    @Override
    WeChatLogic solveLadderLogic(String weChatId, String messageType, String message) throws Exception {
        return new AskPasswordLogic(this.sessionManager, this.ladderConfig, forBind, message);
    }

}
