package spring.logic;

import spring.config.LadderConfig;
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
    WeChatLogic solveLadderLogic(String weChatId, String messageType, String message) throws Exception {
        return new TestLogic(sessionManager, ladderConfig, "use cache");
    }

}
