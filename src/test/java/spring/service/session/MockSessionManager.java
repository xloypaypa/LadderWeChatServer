package spring.service.session;

import spring.config.MockLadderConfig;
import spring.service.ladder.MockLadderServerSolver;

import java.io.IOException;

/**
 * Created by xsu on 16/10/16.
 * it's the mock session manager use mock session message
 */
public class MockSessionManager extends SessionManager {

    public MockSessionManager(MockLadderConfig mockLadderConfig) {
        this.ladderConfig = mockLadderConfig;
    }

    public synchronized void createSession(String weChatId) throws IOException {
        if (!sessionMessageMap.containsKey(weChatId)) {
            MockLadderServerSolver ladderServerSolver = new MockLadderServerSolver(weChatId, this, ladderConfig);
            sessionMessageMap.put(weChatId, new SessionMessage(ladderServerSolver));
        }
    }

    public synchronized void closeSession(String weChatId) {
        this.sessionMessageMap.remove(weChatId);
    }

    public synchronized SessionMessage getSessionMessage(String weChatId) {
        if (sessionMessageMap.containsKey(weChatId)) {
            return sessionMessageMap.get(weChatId);
        } else {
            return null;
        }
    }

}