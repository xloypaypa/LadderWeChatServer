package spring.logic.wallet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import spring.logic.LogicTest;
import spring.service.ladder.MockLadderServerSolver;
import tools.ProtocolBuilder;
import tools.StartLogicMatcher;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

/**
 * Created by xsu on 2016/10/20.
 * it's the testing code for wallet asking money type
 */
public class WalletAskMoneyTypeLogicTest extends LogicTest {

    private JSONArray jsonArray;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        jsonArray = new JSONArray();
        JSONObject typeOne = new JSONObject();
        typeOne.put("typename", "a");
        typeOne.put("value", 1);
        JSONObject typeTwo = new JSONObject();
        typeTwo.put("typename", "b");
        typeTwo.put("value", 1.99998);
        jsonArray.add(typeOne);
        jsonArray.add(typeTwo);

        MockLadderServerSolver mockLadderServerSolver = mockLoginAsWeChatProtocol("id");
        mockLadderServerSolver.addReply(ProtocolBuilder.useApp("wallet"),
                "/useApp#{\"result\":\"ok\"}".getBytes(), 100);
        mockLadderServerSolver.addReply(ProtocolBuilder.useApp("wallet"),
                "/loginApp#{\"result\":\"ok\"}".getBytes(), 100);
        mockLadderServerSolver.addReply(ProtocolBuilder.getBudget(),
                ("getBudget#" + jsonArray.toString()).getBytes(), 100);
    }

    @Test
    public void should_show_money_type_list() throws Exception {
        WalletAskMoneyTypeLogic walletAskMoneyTypeLogic = new WalletAskMoneyTypeLogic(sessionManager, ladderConfig, jsonArray);
        assertTrue(walletAskMoneyTypeLogic.getReplyFromServer().startsWith("please reply one of money type:\n" +
                "1. a\n" +
                "2. b\n"));
    }

    @Test
    public void should_jump_to_ask_budget_logic_if_selected_a_money_type() throws Exception {
        WalletAskMoneyTypeLogic walletAskMoneyTypeLogic = new WalletAskMoneyTypeLogic(sessionManager, ladderConfig, jsonArray);
        walletAskMoneyTypeLogic.getReplyFromUser(userStatus, "id", "type", "1");
        verify(userStatus).addNewLogic(any(WalletAskBudgetTypeLogic.class));
    }

    @Test
    public void should_jump_to_exception_logic_if_selected_a_invalidate_money_type() throws Exception {
        WalletAskMoneyTypeLogic walletAskMoneyTypeLogic = new WalletAskMoneyTypeLogic(sessionManager, ladderConfig, jsonArray);
        walletAskMoneyTypeLogic.getReplyFromUser(userStatus, "id", "type", "3");
        verify(userStatus).addNewLogic(argThat(new StartLogicMatcher("invalid type")));
    }

}