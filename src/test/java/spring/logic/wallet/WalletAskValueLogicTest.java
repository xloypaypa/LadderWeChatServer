package spring.logic.wallet;

import org.junit.Before;
import org.junit.Test;
import spring.logic.LogicTest;
import spring.logic.StartLogic;
import spring.logic.WeChatLogic;
import spring.service.ladder.MockLadderServerSolver;
import tools.ProtocolBuilder;

import static org.junit.Assert.assertEquals;

/**
 * Created by xsu on 2016/10/20.
 * it's the testing code for wallet asking value
 */
public class WalletAskValueLogicTest extends LogicTest {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockLadderServerSolver mockLadderServerSolver = mockLoginAsWeChatProtocol("id");
        mockLadderServerSolver.addReply(ProtocolBuilder.useApp("wallet"),
                "/useApp#{\"result\":\"ok\"}".getBytes(), 100);
        mockLadderServerSolver.addReply(ProtocolBuilder.useApp("wallet"),
                "/loginApp#{\"result\":\"ok\"}".getBytes(), 100);
        mockLadderServerSolver.addReply(ProtocolBuilder.useMoney("money", "budget", 12.3),
                "useMoney#{\"result\":\"ok\"}".getBytes(), 100);
    }

    @Test
    public void should_show_hint_message() throws Exception {
        WalletAskValueLogic walletAskValueLogic = new WalletAskValueLogic(sessionManager, ladderConfig, "", "");
        assertEquals("please input value.", walletAskValueLogic.getReplyFromServer());
    }

    @Test
    public void should_jump_to_use_money_result_logic_if_input_a_double() throws Exception {
        WalletAskValueLogic walletAskValueLogic = new WalletAskValueLogic(sessionManager, ladderConfig, "money", "budget");
        WeChatLogic weChatLogic = walletAskValueLogic.getReplyFromUser("id", "type", "12.3");
        assertEquals(WalletUseMoneyResultLogic.class, weChatLogic.getClass());
    }

    @Test
    public void should_jump_to_exception_logic_if_input_not_double() throws Exception {
        WalletAskValueLogic walletAskValueLogic = new WalletAskValueLogic(sessionManager, ladderConfig, "", "");
        WeChatLogic weChatLogic = walletAskValueLogic.getReplyFromUser("id", "type", "12.a");
        assertEquals(StartLogic.class, weChatLogic.getClass());
        assertEquals("Value should be a number.", weChatLogic.getReplyFromServer());
    }

}