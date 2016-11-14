package spring.logic;

import spring.config.LadderConfig;
import spring.service.cache.UserStatus;
import spring.service.session.SessionManager;

class TestLogic extends WeChatLogic {
    private String message;

    TestLogic(SessionManager sessionManager, LadderConfig ladderConfig, String message) {
        super(sessionManager, ladderConfig);
        this.message = message;
    }

    @Override
    public String getReplyFromServer() {
        return "debug message: " + message;
    }

    @Override
    protected void solveLadderLogic(UserStatus userStatus, String weChatId, String messageType, String message) throws Exception {
        userStatus.addNewLogic(new StartLogic(sessionManager, ladderConfig));
    }

}
