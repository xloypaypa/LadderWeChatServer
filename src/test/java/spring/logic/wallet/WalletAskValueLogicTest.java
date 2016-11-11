package spring.logic.wallet;

import org.junit.Before;
import org.junit.Test;
import spring.logic.LogicTest;
import spring.service.ladder.MockLadderServerSolver;
import tools.ProtocolBuilder;
import tools.StartLogicMatcher;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

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
        walletAskValueLogic.getReplyFromUser(userStatus, "id", "type", "12.3");
        verify(userStatus).addNewLogic(any(WalletMainMenuLogic.class));
    }

    @Test
    public void should_jump_to_exception_logic_if_input_not_double() throws Exception {
        WalletAskValueLogic walletAskValueLogic = new WalletAskValueLogic(sessionManager, ladderConfig, "", "");
        walletAskValueLogic.getReplyFromUser(userStatus, "id", "type", "12.a");
        verify(userStatus).addNewLogic(argThat(new StartLogicMatcher("Value should be a number.")));
    }

}