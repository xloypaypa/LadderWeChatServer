package spring.logic;

import org.junit.Test;
import spring.service.session.MockSessionManager;
import spring.service.session.SessionManager;

import static org.junit.Assert.*;

/**
 * Created by xsu on 16/10/16.
 * it's the testing code for test logic
 */
public class TestLogicTest {

    @Test
    public void testTestLogicMessageAndReply() throws Exception {
        SessionManager sessionManager = new MockSessionManager();

        TestLogic testLogic = new TestLogic(sessionManager, null, "message");

        WeChatLogic weChatLogic = testLogic.getReplyFromUser("id", "es", "es");

        assertEquals("debug message: message", testLogic.getReplyFromServer());
        assertEquals(TestLogic.class, weChatLogic.getClass());
        assertEquals("debug message: use cache", weChatLogic.getReplyFromServer());
    }
}