package spring.logic.wallet;

import org.junit.Test;
import spring.logic.LogicTest;
import spring.logic.WeChatLogic;
import spring.service.ladder.MockLadderServerSolver;
import tools.ProtocolBuilder;

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
        mockLadderServerSolver.addReply(ProtocolBuilder.addMoney("message", 100),
                "/addMoney#{\"result\":\"ok\"}".getBytes(), 100);

        WalletCreateMoneyOrBudgetLogic walletCreateMoneyOrBudgetLogic = new WalletCreateMoneyOrBudgetLogic(sessionManager, ladderConfig, "3");
        walletCreateMoneyOrBudgetLogic.getReplyFromServer();
        walletCreateMoneyOrBudgetLogic.solveLadderLogic(userStatus, "id", "type", "message");

        walletCreateMoneyOrBudgetLogic.getReplyFromServer();
        walletCreateMoneyOrBudgetLogic.solveLadderLogic(userStatus, "id", "type", "100");

        verify(userStatus, times(2)).addNewLogic(any(WeChatLogic.class));
    }

}