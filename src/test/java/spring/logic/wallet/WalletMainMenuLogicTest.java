package spring.logic.wallet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Test;
import spring.logic.LogicTest;
import spring.logic.StartLogic;
import spring.service.ladder.MockLadderServerSolver;
import tools.ProtocolBuilder;
import tools.StartLogicMatcher;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

/**
 * Created by xsu on 2016/10/19.
 * it's the testing code for wallet main menu
 */
public class WalletMainMenuLogicTest extends LogicTest {

    @Test
    public void should_show_main_menu() throws Exception {
        WalletMainMenuLogic walletMainMenuLogic = new WalletMainMenuLogic(sessionManager, ladderConfig);
        assertEquals("input 1 to get money list;\n" +
                        "input 2 to get budget list;\n" +
                        "input 3 to create money type;\n" +
                        "input 4 to create budget type;\n" +
                        "input 5 to use money;\n" +
                        "input 9 to roll back last operation;\n" +
                        "input 0 to exit app;",
                walletMainMenuLogic.getReplyFromServer());
    }

    @Test
    public void should_back_to_start_logic_when_input_0() throws Exception {
        WalletMainMenuLogic walletMainMenuLogic = new WalletMainMenuLogic(sessionManager, ladderConfig);
        walletMainMenuLogic.getReplyFromUser(userStatus, "id", "type", "0");
        verify(userStatus).addNewLogic(any(StartLogic.class));
    }

    @Test
    public void should_go_to_show_money_list_logic_when_input_1() throws Exception {
        MockLadderServerSolver mockLadderServerSolver = mockLoginAsWeChatProtocol("id");
        JSONArray jsonArray = new JSONArray();
        JSONObject typeOne = new JSONObject();
        typeOne.put("typename", "a");
        typeOne.put("value", 1);
        JSONObject typeTwo = new JSONObject();
        typeTwo.put("typename", "b");
        typeTwo.put("value", 2);
        jsonArray.add(typeOne);
        jsonArray.add(typeTwo);

        mockLadderServerSolver.addReply(ProtocolBuilder.useApp("wallet"),
                "/useApp#{\"result\":\"ok\"}".getBytes(), 100);
        mockLadderServerSolver.addReply(ProtocolBuilder.useApp("wallet"),
                "/loginApp#{\"result\":\"ok\"}".getBytes(), 100);
        mockLadderServerSolver.addReply(ProtocolBuilder.getMoney(),
                ("getMoney#" + jsonArray.toString()).getBytes(), 100);

        WalletMainMenuLogic walletMainMenuLogic = new WalletMainMenuLogic(sessionManager, ladderConfig);
        walletMainMenuLogic.getReplyFromUser(userStatus, "id", "type", "1");
        verify(userStatus).addNewLogic(any(WalletGetMoneyListLogic.class));
    }

    @Test
    public void should_go_to_show_budget_list_logic_when_input_2() throws Exception {
        MockLadderServerSolver mockLadderServerSolver = mockLoginAsWeChatProtocol("id");
        JSONArray jsonArray = new JSONArray();
        JSONObject typeOne = new JSONObject();
        typeOne.put("typename", "a");
        typeOne.put("value", 1);
        JSONObject typeTwo = new JSONObject();
        typeTwo.put("typename", "b");
        typeTwo.put("value", 2);
        jsonArray.add(typeOne);
        jsonArray.add(typeTwo);

        mockLadderServerSolver.addReply(ProtocolBuilder.useApp("wallet"),
                "/useApp#{\"result\":\"ok\"}".getBytes(), 100);
        mockLadderServerSolver.addReply(ProtocolBuilder.useApp("wallet"),
                "/loginApp#{\"result\":\"ok\"}".getBytes(), 100);
        mockLadderServerSolver.addReply(ProtocolBuilder.getBudget(),
                ("getBudget#" + jsonArray.toString()).getBytes(), 100);

        WalletMainMenuLogic walletMainMenuLogic = new WalletMainMenuLogic(sessionManager, ladderConfig);
        walletMainMenuLogic.getReplyFromUser(userStatus, "id", "type", "2");
        verify(userStatus).addNewLogic(any(WalletGetBudgetListLogic.class));
    }

    @Test
    public void should_go_to_ask_money_type_logic_when_input_3() throws Exception {
        MockLadderServerSolver mockLadderServerSolver = mockLoginAsWeChatProtocol("id");
        JSONArray jsonArray = new JSONArray();
        JSONObject typeOne = new JSONObject();
        typeOne.put("typename", "a");
        typeOne.put("value", 1);
        JSONObject typeTwo = new JSONObject();
        typeTwo.put("typename", "b");
        typeTwo.put("value", 2);
        jsonArray.add(typeOne);
        jsonArray.add(typeTwo);

        mockLadderServerSolver.addReply(ProtocolBuilder.useApp("wallet"),
                "/useApp#{\"result\":\"ok\"}".getBytes(), 100);
        mockLadderServerSolver.addReply(ProtocolBuilder.useApp("wallet"),
                "/loginApp#{\"result\":\"ok\"}".getBytes(), 100);
        mockLadderServerSolver.addReply(ProtocolBuilder.getMoney(),
                ("getMoney#" + jsonArray.toString()).getBytes(), 100);

        WalletMainMenuLogic walletMainMenuLogic = new WalletMainMenuLogic(sessionManager, ladderConfig);
        walletMainMenuLogic.getReplyFromUser(userStatus, "id", "type", "3");
        verify(userStatus).addNewLogic(any(WalletAskMoneyTypeLogic.class));
    }

    @Test
    public void should_go_to_roll_back_logic_when_input_9() throws Exception {
        MockLadderServerSolver mockLadderServerSolver = mockLoginAsWeChatProtocol("id");

        mockLadderServerSolver.addReply(ProtocolBuilder.useApp("wallet"),
                "/useApp#{\"result\":\"ok\"}".getBytes(), 100);
        mockLadderServerSolver.addReply(ProtocolBuilder.useApp("wallet"),
                "/loginApp#{\"result\":\"ok\"}".getBytes(), 100);
        mockLadderServerSolver.addReply(ProtocolBuilder.rollBack(),
                "rollBack#{\"result\":\"ok\"}".getBytes(), 100);

        WalletMainMenuLogic walletMainMenuLogic = new WalletMainMenuLogic(sessionManager, ladderConfig);
        walletMainMenuLogic.getReplyFromUser(userStatus, "id", "type", "9");
        verify(userStatus).addNewLogic(any(WalletRollbackLogic.class));
    }

    @Test
    public void should_go_to_exception_logic_when_invalidate_message() throws Exception {
        WalletMainMenuLogic walletMainMenuLogic = new WalletMainMenuLogic(sessionManager, ladderConfig);
        walletMainMenuLogic.getReplyFromUser(userStatus, "id", "type", "-1");
        verify(userStatus).addNewLogic(argThat(new StartLogicMatcher("invalid command")));
    }
}