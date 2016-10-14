package spring.logic;

import spring.config.LadderConfig;
import spring.service.session.SessionManager;

class TestLogic extends WeChatLogic {
    private String sessionId;

    TestLogic(SessionManager sessionManager, LadderConfig ladderConfig, String sessionId) {
        super(sessionManager, ladderConfig);
        this.sessionId = sessionId;
    }

    @Override
    public String getReplyFromServer() {
        return "debug message: " + sessionId;
    }

    @Override
    WeChatLogic solveLadderLogic(String weChatId, String messageType, String message) throws Exception {
        return new TestLogic(sessionManager, ladderConfig, "use cache");
    }

}
