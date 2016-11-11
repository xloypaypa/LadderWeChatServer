package spring.logic;

import org.junit.Test;
import spring.config.MockLadderConfig;
import spring.service.session.MockSessionManager;
import spring.service.session.SessionManager;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Created by xsu on 16/10/16.
 * it's the testing code for test logic
 */
public class TestLogicTest extends LogicTest {

    @Test
    public void testTestLogicMessageAndReply() throws Exception {
        SessionManager sessionManager = new MockSessionManager(new MockLadderConfig());

        TestLogic testLogic = new TestLogic(sessionManager, null, "message");

        testLogic.getReplyFromUser(userStatus, "id", "es", "es");

        verify(userStatus).addNewLogic(any(StartLogic.class));
        assertEquals("debug message: message", testLogic.getReplyFromServer());
    }
}