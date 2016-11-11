package spring.logic;

import org.junit.Test;
import spring.service.ladder.MockLadderServerSolver;
import tools.ProtocolBuilder;
import tools.StartLogicMatcher;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

/**
 * Created by xsu on 16/10/17.
 * it's the testing code for app or unbind logic
 */
public class AppOrUnbindLogicTest extends LogicTest {

    @Test
    public void should_jump_to_exception_logic_when_got_invalidated_input() throws Exception {
        mockLoginAsWeChatProtocol("id");
        AppOrUnbindLogic appOrUnbindLogic = new AppOrUnbindLogic(sessionManager, ladderConfig);
        appOrUnbindLogic.getReplyFromUser(userStatus, "id", "type", "3");
        verify(userStatus).addNewLogic(argThat(new StartLogicMatcher("invalidate input")));
    }

    @Test
    public void should_jump_to_use_app_logic_when_use_app() throws Exception {
        mockLoginAsWeChatProtocol("id");
        AppOrUnbindLogic appOrUnbindLogic = new AppOrUnbindLogic(sessionManager, ladderConfig);
        appOrUnbindLogic.getReplyFromUser(userStatus, "id", "type", "1");
        verify(userStatus).addNewLogic(any(UseAppLogic.class));
    }

    @Test
    public void should_jump_to_start_logic_when_unbind_account() throws Exception {
        MockLadderServerSolver mockLadderServerSolver = mockLoginAsWeChatProtocol("id");
        mockLadderServerSolver.addReply(ProtocolBuilder.unbindUserAndWeChat("id"),
                "unbindUserAndWeChat#{\"result\":\"ok\"}".getBytes(), 50);
        AppOrUnbindLogic appOrUnbindLogic = new AppOrUnbindLogic(sessionManager, ladderConfig);
        appOrUnbindLogic.getReplyFromUser(userStatus, "id", "type", "2");
        verify(userStatus).addNewLogic(any(StartLogic.class));
    }
}