package spring.logic;

import org.junit.Test;
import tools.StartLogicMatcher;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

/**
 * Created by xsu on 16/10/17.
 * it's testing code for bind logic
 */
public class BindLogicTest extends LogicTest {

    @Test
    public void should_jump_to_exception_logic_when_got_invalidated_input() throws Exception {
        BindLogic bindLogic = new BindLogic(sessionManager, ladderConfig);
        bindLogic.getReplyFromUser(userStatus, "id", "type", "3");
        verify(userStatus).addNewLogic(argThat(new StartLogicMatcher("invalidate input")));
    }

    @Test
    public void should_jump_to_ask_username_logic_when_got_validated_input() throws Exception {
        BindLogic bindLogic = new BindLogic(sessionManager, ladderConfig);
        bindLogic.getReplyFromUser(userStatus, "id", "type", "1");
        verify(userStatus).addNewLogic(any(AskUserNameLogic.class));
    }
}