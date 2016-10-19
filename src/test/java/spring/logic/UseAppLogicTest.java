package spring.logic;

import org.junit.Test;
import spring.logic.wallet.WalletMainMenuLogic;

import static org.junit.Assert.*;

/**
 * Created by xsu on 2016/10/19.
 * it's the testing code for use app logic
 */
public class UseAppLogicTest extends LogicTest {

    @Test
    public void should_show_all_available_application() throws Exception {
        UseAppLogic useAppLogic = new UseAppLogic(sessionManager, ladderConfig);
        assertEquals("input 1 to use wallet; ", useAppLogic.getReplyFromServer());
    }

    @Test
    public void should_go_to_wallet_main_menu_logic_when_use_wallet() throws Exception {
        UseAppLogic useAppLogic = new UseAppLogic(sessionManager, ladderConfig);
        WeChatLogic weChatLogic = useAppLogic.getReplyFromUser("id", "type", "1");
        assertEquals(WalletMainMenuLogic.class, weChatLogic.getClass());
    }

    @Test
    public void should_go_to_exception_logic_when_input_invalidate_message() throws Exception {
        UseAppLogic useAppLogic = new UseAppLogic(sessionManager, ladderConfig);
        WeChatLogic weChatLogic = useAppLogic.getReplyFromUser("id", "type", "2");
        assertEquals(ExceptionLogic.class, weChatLogic.getClass());
        assertEquals("no such application", weChatLogic.getReplyFromServer());
    }
}