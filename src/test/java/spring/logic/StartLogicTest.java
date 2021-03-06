package spring.logic;

import org.junit.Test;
import spring.service.ladder.MockLadderServerSolver;
import tools.ProtocolBuilder;
import tools.StartLogicMatcher;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

/**
 * Created by xsu on 16/10/17.
 * it's the testing code for start logic.
 */
public class StartLogicTest extends LogicTest {

    @Test
    public void testTimeOut() throws Exception {
        sessionManager.createSession("id");

        StartLogic startLogic = new StartLogic(sessionManager, ladderConfig);
        startLogic.getReplyFromUser(userStatus, "id", "message", "type");
        verify(userStatus).addNewLogic(argThat(new StartLogicMatcher("time out")));
    }

    @Test
    public void can_send_custom_message() throws Exception {
        StartLogic startLogic = new StartLogic(sessionManager, ladderConfig, "test message");
        assertEquals("test message\nPlease input any thing to restart.", startLogic.getReplyFromServer());
    }

    @Test
    public void should_reply_server_error_when_change_connection_user_failed() throws Exception {
        sessionManager.createSession("id");
        MockLadderServerSolver mockLadderServerSolver = (MockLadderServerSolver) sessionManager.getSessionMessage("id")
                .getLadderServerSolver();

        mockLadderServerSolver.addReply(ProtocolBuilder.key(ladderConfig.getPublicKey()), "key#{\"result\":\"ok\"}".getBytes(), 100);
        mockLadderServerSolver.addReply(ProtocolBuilder.getSessionId(), "/getSessionID#{\"result\":\"1234\"}".getBytes(), 100);
        mockLadderServerSolver.addReply(ProtocolBuilder.login("username", "password", "1234"), "/login#{\"result\":\"ok\"}".getBytes(), 100);
        mockLadderServerSolver.addReply(ProtocolBuilder.changeConnectionUserByWeChat("id"), "changeConnectionUserByWeChat#{\"result\":\"fail\"}".getBytes(), 100);

        StartLogic startLogic = new StartLogic(sessionManager, ladderConfig);
        startLogic.getReplyFromUser(userStatus, "id", "message", "type");
        verify(userStatus).addNewLogic(argThat(new StartLogicMatcher("server error")));
    }

    @Test
    public void should_jump_to_bind_logic_when_not_bind_account() throws Exception {
        sessionManager.createSession("id");
        MockLadderServerSolver mockLadderServerSolver = (MockLadderServerSolver) sessionManager.getSessionMessage("id")
                .getLadderServerSolver();

        mockLadderServerSolver.addReply(ProtocolBuilder.key(ladderConfig.getPublicKey()), "key#{\"result\":\"ok\"}".getBytes(), 100);
        mockLadderServerSolver.addReply(ProtocolBuilder.getSessionId(), "/getSessionID#{\"result\":\"1234\"}".getBytes(), 100);
        mockLadderServerSolver.addReply(ProtocolBuilder.login("username", "password", "1234"), "/login#{\"result\":\"ok\"}".getBytes(), 100);
        mockLadderServerSolver.addReply(ProtocolBuilder.changeConnectionUserByWeChat("id"), "changeConnectionUserByWeChat#{\"result\":\"unbind account\"}".getBytes(), 100);

        StartLogic startLogic = new StartLogic(sessionManager, ladderConfig);
        startLogic.getReplyFromUser(userStatus, "id", "message", "type");
        verify(userStatus).addNewLogic(any(BindLogic.class));
    }

    @Test
    public void should_jump_to_app_or_unbind_logic_when_have_bind_account() throws Exception {
        sessionManager.createSession("id");
        MockLadderServerSolver mockLadderServerSolver = (MockLadderServerSolver) sessionManager.getSessionMessage("id")
                .getLadderServerSolver();

        mockLadderServerSolver.addReply(ProtocolBuilder.key(ladderConfig.getPublicKey()), "key#{\"result\":\"ok\"}".getBytes(), 100);
        mockLadderServerSolver.addReply(ProtocolBuilder.getSessionId(), "/getSessionID#{\"result\":\"1234\"}".getBytes(), 100);
        mockLadderServerSolver.addReply(ProtocolBuilder.login("username", "password", "1234"), "/login#{\"result\":\"ok\"}".getBytes(), 100);
        mockLadderServerSolver.addReply(ProtocolBuilder.changeConnectionUserByWeChat("id"), "changeConnectionUserByWeChat#{\"result\":\"ok\"}".getBytes(), 100);

        StartLogic startLogic = new StartLogic(sessionManager, ladderConfig);
        startLogic.getReplyFromUser(userStatus, "id", "message", "type");
        verify(userStatus).addNewLogic(any(AppOrUnbindLogic.class));
    }
}