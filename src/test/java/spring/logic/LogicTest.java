package spring.logic;

import org.junit.Before;
import spring.config.LadderConfig;
import spring.config.MockLadderConfig;
import spring.service.ladder.MockLadderServerSolver;
import spring.service.session.MockSessionManager;
import spring.service.session.SessionManager;
import tools.ProtocolBuilder;

/**
 * Created by xsu on 16/10/17.
 * it's the logic testing
 */
public class LogicTest {

    protected LadderConfig ladderConfig;

    protected SessionManager sessionManager;

    String sessionId = "1234";

    @Before
    public void setUp() throws Exception {
        ladderConfig = new MockLadderConfig();
        sessionManager = new MockSessionManager((MockLadderConfig) ladderConfig);
    }

    protected MockLadderServerSolver mockLoginAsWeChatProtocol(String id) throws Exception {
        sessionManager.createSession(id);
        MockLadderServerSolver mockLadderServerSolver = (MockLadderServerSolver) sessionManager.getSessionMessage("id")
                .getLadderServerSolver();
        mockLadderServerSolver.putReply(ProtocolBuilder.key(ladderConfig.getPublicKey()), "key#{\"result\":\"ok\"}".getBytes(), 100);
        mockLadderServerSolver.putReply(ProtocolBuilder.getSessionId(), ("/getSessionID#{\"result\":\"" + sessionId + "\"}").getBytes(), 100);
        mockLadderServerSolver.putReply(ProtocolBuilder.login("username", "password", "1234"), "/login#{\"result\":\"ok\"}".getBytes(), 100);
        return mockLadderServerSolver;
    }
}
