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
        return "session id: " + sessionId;
    }

    @Override
    public WeChatLogic getReplyFromUser(String weChatId, String messageType, String message) {
        return new TestLogic(sessionManager, ladderConfig, "use cache");
    }
}
