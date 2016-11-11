package spring.logic;

import org.junit.Test;
import spring.service.ladder.MockLadderServerSolver;
import tools.ProtocolBuilder;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Created by xsu on 16/10/17.
 * it's the testing code for ask password
 */
public class AskPasswordLogicTest extends LogicTest {

    @Test
    public void should_jump_to_app_or_unbind_logic_when_ask_password_for_bind() throws Exception {
        MockLadderServerSolver mockLadderServerSolver = mockLoginAsWeChatProtocol("id");
        mockLadderServerSolver.addReply(ProtocolBuilder.bindUserAndWeChat("wu", "wp", sessionId, "id"),
                "bindUserAndWeChat#{\"result\":\"ok\"}".getBytes(), 100);

        AskPasswordLogic askPasswordLogic = new AskPasswordLogic(sessionManager, ladderConfig, true, "wu");
        askPasswordLogic.getReplyFromUser(userStatus, "id", "type", "wp");
        verify(userStatus).addNewLogic(any(AppOrUnbindLogic.class));
    }

    @Test
    public void should_jump_to_start_logic_when_ask_password_not_for_bind() throws Exception {
        MockLadderServerSolver mockLadderServerSolver = mockLoginAsWeChatProtocol("id");
        mockLadderServerSolver.addReply(ProtocolBuilder.register("wu", "wp"),
                "bindUserAndWeChat#{\"result\":\"ok\"}".getBytes(), 100);
        mockLadderServerSolver.addReply(ProtocolBuilder.bindUserAndWeChat("wu", "wp", sessionId, "id"),
                "bindUserAndWeChat#{\"result\":\"ok\"}".getBytes(), 100);

        AskPasswordLogic askPasswordLogic = new AskPasswordLogic(sessionManager, ladderConfig, false, "wu");
        askPasswordLogic.getReplyFromUser(userStatus, "id", "type", "wp");
        verify(userStatus).addNewLogic(any(StartLogic.class));
    }
}