package spring.logic;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by xsu on 16/10/17.
 * it's the testing for exception logic
 */
public class ExceptionLogicTest extends LogicTest {

    @Test
    public void should_jump_to_start_logic() throws Exception {
        ExceptionLogic exceptionLogic = new ExceptionLogic(sessionManager, ladderConfig, "message");
        WeChatLogic weChatLogic = exceptionLogic.getReplyFromUser("id", "type", "3");
        assertEquals(StartLogic.class, weChatLogic.getClass());
    }
}