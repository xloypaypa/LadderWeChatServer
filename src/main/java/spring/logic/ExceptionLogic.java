package spring.logic;

import spring.config.LadderConfig;
import spring.service.session.SessionManager;

/**
 * Created by xsu on 16/10/12.
 * it's the logic solve time out
 */
class ExceptionLogic extends WeChatLogic {

    private String message;

    ExceptionLogic(SessionManager sessionManager, LadderConfig ladderConfig, String message) {
        super(sessionManager, ladderConfig);
        this.message = message;
    }

    @Override
    public String getReplyFromServer() {
        return message;
    }

    @Override
    public WeChatLogic getReplyFromUser(String weChatId, String messageType, String message) {
        return new StartLogic(sessionManager, ladderConfig);
    }
}
