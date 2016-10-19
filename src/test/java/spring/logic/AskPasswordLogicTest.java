package spring.logic;

import org.junit.Test;
import spring.service.ladder.MockLadderServerSolver;
import tools.ProtocolBuilder;

import static org.junit.Assert.*;

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
        WeChatLogic weChatLogic = askPasswordLogic.getReplyFromUser("id", "type", "wp");
        assertEquals(AppOrUnbindLogic.class, weChatLogic.getClass());
    }

    @Test
    public void should_jump_to_start_logic_when_ask_password_not_for_bind() throws Exception {
        MockLadderServerSolver mockLadderServerSolver = mockLoginAsWeChatProtocol("id");
        mockLadderServerSolver.addReply(ProtocolBuilder.register("wu", "wp"),
                "bindUserAndWeChat#{\"result\":\"ok\"}".getBytes(), 100);

        AskPasswordLogic askPasswordLogic = new AskPasswordLogic(sessionManager, ladderConfig, false, "wu");
        WeChatLogic weChatLogic = askPasswordLogic.getReplyFromUser("id", "type", "wp");
        assertEquals(StartLogic.class, weChatLogic.getClass());
    }
}