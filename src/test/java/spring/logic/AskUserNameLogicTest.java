package spring.logic;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by xsu on 16/10/17.
 * it's the testing code for ask username logic
 */
public class AskUserNameLogicTest extends LogicTest {

    @Test
    public void should_jump_to_ask_password_logic() throws Exception {
        AskUserNameLogic askUserNameLogic = new AskUserNameLogic(sessionManager, ladderConfig, true);
        WeChatLogic weChatLogic = askUserNameLogic.getReplyFromUser("id", "type", "3");
        assertEquals(AskPasswordLogic.class, weChatLogic.getClass());
    }
}