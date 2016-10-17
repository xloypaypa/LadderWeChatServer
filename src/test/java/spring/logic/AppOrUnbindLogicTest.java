package spring.logic;

import org.junit.Test;
import spring.service.ladder.MockLadderServerSolver;
import tools.ProtocolBuilder;

import static org.junit.Assert.*;

/**
 * Created by xsu on 16/10/17.
 * it's the testing code for app or unbind logic
 */
public class AppOrUnbindLogicTest extends LogicTest {

    @Test
    public void should_jump_to_exception_logic_when_got_invalidated_input() throws Exception {
        mockLoginAsWeChatProtocol("id");
        AppOrUnbindLogic appOrUnbindLogic = new AppOrUnbindLogic(sessionManager, ladderConfig);
        WeChatLogic weChatLogic = appOrUnbindLogic.getReplyFromUser("id", "type", "3");
        assertEquals(ExceptionLogic.class, weChatLogic.getClass());
        assertEquals("invalidate input", weChatLogic.getReplyFromServer());
    }

    @Test
    public void should_jump_to_test_logic_when_use_app() throws Exception {
        mockLoginAsWeChatProtocol("id");
        AppOrUnbindLogic appOrUnbindLogic = new AppOrUnbindLogic(sessionManager, ladderConfig);
        WeChatLogic weChatLogic = appOrUnbindLogic.getReplyFromUser("id", "type", "1");
        assertEquals(TestLogic.class, weChatLogic.getClass());
        assertEquals("debug message: still in progress", weChatLogic.getReplyFromServer());
    }

    @Test
    public void should_jump_to_start_logic_when_unbind_account() throws Exception {
        MockLadderServerSolver mockLadderServerSolver = mockLoginAsWeChatProtocol("id");
        mockLadderServerSolver.putReply(ProtocolBuilder.unbindUserAndWeChat("id"),
                "unbindUserAndWeChat#{\"result\":\"ok\"}".getBytes(), 50);
        AppOrUnbindLogic appOrUnbindLogic = new AppOrUnbindLogic(sessionManager, ladderConfig);
        WeChatLogic weChatLogic = appOrUnbindLogic.getReplyFromUser("id", "type", "2");
        assertEquals(StartLogic.class, weChatLogic.getClass());
    }
}