package spring.logic;

import spring.service.session.SessionManager;

class TestLogic extends WeChatLogic {
    private String sessionId;

    TestLogic(SessionManager sessionManager, String sessionId) {
        super(sessionManager);
        this.sessionId = sessionId;
    }

    @Override
    public String getReplyFromServer() {
        return "session id: " + sessionId;
    }

    @Override
    public WeChatLogic getReplyFromUser(String weChatId, String messageType, String message) {
        return new TestLogic(sessionManager, "use cache");
    }
}
