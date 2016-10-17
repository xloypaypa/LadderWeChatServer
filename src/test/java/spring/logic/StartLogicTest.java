package spring.logic;

import org.junit.Test;
import spring.config.MockLadderConfig;
import spring.service.ladder.MockLadderServerSolver;
import spring.service.session.MockSessionManager;
import spring.service.session.SessionManager;
import tools.ProtocolBuilder;

import static org.junit.Assert.*;

/**
 * Created by xsu on 16/10/17.
 * it's the testing code for start logic.
 */
public class StartLogicTest {

    @Test
    public void testTimeOut() throws Exception {
        SessionManager sessionManager = new MockSessionManager(new MockLadderConfig());

        sessionManager.createSession("id");

        StartLogic startLogic = new StartLogic(sessionManager, new MockLadderConfig());
        WeChatLogic weChatLogic = startLogic.getReplyFromUser("id", "message", "type");
        assertEquals(ExceptionLogic.class, weChatLogic.getClass());
        assertEquals("time out", weChatLogic.getReplyFromServer());
    }

    @Test
    public void should_reply_server_error_when_change_connection_user_failed() throws Exception {
        MockLadderConfig mockLadderConfig = new MockLadderConfig();
        SessionManager sessionManager = new MockSessionManager(mockLadderConfig);

        sessionManager.createSession("id");
        MockLadderServerSolver mockLadderServerSolver = (MockLadderServerSolver) sessionManager.getSessionMessage("id")
                .getLadderServerSolver();

        mockLadderServerSolver.putReply(ProtocolBuilder.key(mockLadderConfig.getPublicKey()), "key#{\"result\":\"ok\"}".getBytes(), 100);
        mockLadderServerSolver.putReply(ProtocolBuilder.getSessionId(), "/getSessionID#{\"result\":\"1234\"}".getBytes(), 100);
        mockLadderServerSolver.putReply(ProtocolBuilder.login("username", "password", "1234"), "/login#{\"result\":\"ok\"}".getBytes(), 100);
        mockLadderServerSolver.putReply(ProtocolBuilder.changeConnectionUserByWeChat("id"), "changeConnectionUserByWeChat#{\"result\":\"fail\"}".getBytes(), 100);

        StartLogic startLogic = new StartLogic(sessionManager, mockLadderConfig);
        WeChatLogic weChatLogic = startLogic.getReplyFromUser("id", "message", "type");
        assertEquals(ExceptionLogic.class, weChatLogic.getClass());
        assertEquals("server error", weChatLogic.getReplyFromServer());
    }
}