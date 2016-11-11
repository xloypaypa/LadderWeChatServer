package spring.logic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by xsu on 16/10/17.
 * it's testing code for bind logic
 */
public class BindLogicTest extends LogicTest {

    @Test
    public void should_jump_to_exception_logic_when_got_invalidated_input() throws Exception {
        BindLogic bindLogic = new BindLogic(sessionManager, ladderConfig);
        WeChatLogic weChatLogic = bindLogic.getReplyFromUser("id", "type", "3");
        assertEquals(StartLogic.class, weChatLogic.getClass());
        assertEquals("invalidate input", weChatLogic.getReplyFromServer());
    }

    @Test
    public void should_jump_to_ask_username_logic_when_got_validated_input() throws Exception {
        BindLogic bindLogic = new BindLogic(sessionManager, ladderConfig);
        WeChatLogic weChatLogic = bindLogic.getReplyFromUser("id", "type", "1");
        assertEquals(AskUserNameLogic.class, weChatLogic.getClass());
    }
}