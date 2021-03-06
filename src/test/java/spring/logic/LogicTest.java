package spring.logic;

import org.junit.Before;
import spring.config.LadderConfig;
import spring.config.MockLadderConfig;
import spring.service.cache.UserStatus;
import spring.service.ladder.MockLadderServerSolver;
import spring.service.session.MockSessionManager;
import spring.service.session.SessionManager;
import tools.ProtocolBuilder;

import static org.mockito.Mockito.mock;

/**
 * Created by xsu on 16/10/17.
 * it's the logic testing
 */
public class LogicTest {

    protected LadderConfig ladderConfig;

    protected SessionManager sessionManager;

    protected UserStatus userStatus;

    String sessionId = "1234";

    @Before
    public void setUp() throws Exception {
        ladderConfig = new MockLadderConfig();
        sessionManager = new MockSessionManager((MockLadderConfig) ladderConfig);
        this.userStatus = mock(UserStatus.class);
    }

    protected MockLadderServerSolver mockLoginAsWeChatProtocol(String id) throws Exception {
        sessionManager.createSession(id);
        MockLadderServerSolver mockLadderServerSolver = (MockLadderServerSolver) sessionManager.getSessionMessage("id")
                .getLadderServerSolver();
        mockLadderServerSolver.addReply(ProtocolBuilder.key(ladderConfig.getPublicKey()), "key#{\"result\":\"ok\"}".getBytes(), 100);
        mockLadderServerSolver.addReply(ProtocolBuilder.getSessionId(), ("/getSessionID#{\"result\":\"" + sessionId + "\"}").getBytes(), 100);
        mockLadderServerSolver.addReply(ProtocolBuilder.login("username", "password", "1234"), "/login#{\"result\":\"ok\"}".getBytes(), 100);
        mockLadderServerSolver.addReply(ProtocolBuilder.changeConnectionUserByWeChat("id"), "changeConnectionUserByWeChat#{\"result\":\"ok\"}".getBytes(), 100);
        return mockLadderServerSolver;
    }
}
