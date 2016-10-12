package spring.logic;

import spring.config.LadderConfig;
import spring.service.session.SessionManager;

/**
 * Created by xsu on 16/10/12.
 * it's the logic solve time out
 */
class ExceptionLogic extends WeChatLogic {

    ExceptionLogic(SessionManager sessionManager, LadderConfig ladderConfig, String s) {
        super(sessionManager, ladderConfig);
    }

    @Override
    public String getReplyFromServer() {
        return null;
    }

    @Override
    public WeChatLogic getReplyFromUser(String weChatId, String messageType, String message) {
        return null;
    }
}
