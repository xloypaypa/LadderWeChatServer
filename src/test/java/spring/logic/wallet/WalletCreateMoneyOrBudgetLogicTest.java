package spring.logic.wallet;

import org.junit.Test;
import spring.logic.LogicTest;
import spring.logic.WeChatLogic;
import spring.service.ladder.MockLadderServerSolver;
import tools.ProtocolBuilder;

import static net.sf.ezmorph.test.ArrayAssertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by xsu on 2016/11/17.
 * it's the testing code for create money or budget.
 */
public class WalletCreateMoneyOrBudgetLogicTest extends LogicTest {

    @Test
    public void should_jump_to_itself_after_input_type() throws Exception {
        WalletCreateMoneyOrBudgetLogic walletCreateMoneyOrBudgetLogic = new WalletCreateMoneyOrBudgetLogic(sessionManager, ladderConfig, "3");
        walletCreateMoneyOrBudgetLogic.solveLadderLogic(userStatus, "id", "type", "message");

        verify(userStatus).addNewLogic(walletCreateMoneyOrBudgetLogic);
    }

    @Test
    public void should_ask_app_to_add_entity() throws Exception {
        MockLadderServerSolver mockLadderServerSolver = mockLoginAsWeChatProtocol("id");
        mockLadderServerSolver.addReply(ProtocolBuilder.useApp("wallet"),
                "/useApp#{\"result\":\"ok\"}".getBytes(), 100);
        mockLadderServerSolver.addReply(ProtocolBuilder.useApp("wallet"),
                "/loginApp#{\"result\":\"ok\"}".getBytes(), 100);
        mockLadderServerSolver.addReply(ProtocolBuilder.useMoney("money", "budget", 12.3),
                "useMoney#{\"result\":\"ok\"}".getBytes(), 100);
        mockLadderServerSolver.addReply(ProtocolBuilder.addMoney("message", 100),
                "/addMoney#{\"result\":\"ok\"}".getBytes(), 100);

        WalletCreateMoneyOrBudgetLogic walletCreateMoneyOrBudgetLogic = new WalletCreateMoneyOrBudgetLogic(sessionManager, ladderConfig, "3");
        walletCreateMoneyOrBudgetLogic.getReplyFromServer();
        walletCreateMoneyOrBudgetLogic.solveLadderLogic(userStatus, "id", "type", "message");

        walletCreateMoneyOrBudgetLogic.getReplyFromServer();
        walletCreateMoneyOrBudgetLogic.solveLadderLogic(userStatus, "id", "type", "100");

        verify(userStatus, times(2)).addNewLogic(any(WeChatLogic.class));

        assertEquals("create ok\n" +
                "\n" +
                "input 1 to get money list;\n" +
                "input 2 to get budget list;\n" +
                "input 3 to create money type;\n" +
                "input 4 to create budget type;\n" +
                "input 5 to use money;\n" +
                "input 9 to roll back last operation;\n" +
                "input 0 to exit app;", walletCreateMoneyOrBudgetLogic.getReplyFromServer());
    }

}