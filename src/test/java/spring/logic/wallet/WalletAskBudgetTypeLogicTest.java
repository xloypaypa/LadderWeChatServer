package spring.logic.wallet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Test;
import spring.logic.LogicTest;
import spring.logic.StartLogic;
import spring.logic.WeChatLogic;

import static org.junit.Assert.*;

/**
 * Created by xsu on 2016/10/20.
 * it's the testing code for wallet asking budget type
 */
public class WalletAskBudgetTypeLogicTest extends LogicTest {

    @Test
    public void should_show_budget_type_list() throws Exception {
        JSONArray jsonArray = new JSONArray();
        JSONObject typeOne = new JSONObject();
        typeOne.put("typename", "a");
        typeOne.put("value", 1);
        JSONObject typeTwo = new JSONObject();
        typeTwo.put("typename", "b");
        typeTwo.put("value", 1.99998);
        jsonArray.add(typeOne);
        jsonArray.add(typeTwo);

        WalletAskBudgetTypeLogic walletAskBudgetTypeLogic = new WalletAskBudgetTypeLogic(sessionManager, ladderConfig, jsonArray, "");
        assertTrue(walletAskBudgetTypeLogic.getReplyFromServer().startsWith("please reply one of budget type:\n" +
                "1. a\n" +
                "2. b\n"));
    }

    @Test
    public void should_jump_to_ask_value_logic_if_selected_a_budget_type() throws Exception {
        JSONArray jsonArray = new JSONArray();
        JSONObject typeOne = new JSONObject();
        typeOne.put("typename", "a");
        typeOne.put("value", 1);
        JSONObject typeTwo = new JSONObject();
        typeTwo.put("typename", "b");
        typeTwo.put("value", 1.99998);
        jsonArray.add(typeOne);
        jsonArray.add(typeTwo);

        WalletAskBudgetTypeLogic walletAskBudgetTypeLogic = new WalletAskBudgetTypeLogic(sessionManager, ladderConfig, jsonArray, "");
        WeChatLogic weChatLogic = walletAskBudgetTypeLogic.getReplyFromUser("id", "type", "1");
        assertEquals(WalletAskValueLogic.class, weChatLogic.getClass());
    }

    @Test
    public void should_jump_to_exception_logic_if_selected_a_invalidate_budget_type() throws Exception {
        JSONArray jsonArray = new JSONArray();
        JSONObject typeOne = new JSONObject();
        typeOne.put("typename", "a");
        typeOne.put("value", 1);
        JSONObject typeTwo = new JSONObject();
        typeTwo.put("typename", "b");
        typeTwo.put("value", 1.99998);
        jsonArray.add(typeOne);
        jsonArray.add(typeTwo);

        WalletAskBudgetTypeLogic walletAskBudgetTypeLogic = new WalletAskBudgetTypeLogic(sessionManager, ladderConfig, jsonArray, "");
        WeChatLogic weChatLogic = walletAskBudgetTypeLogic.getReplyFromUser("id", "type", "3");
        assertEquals(StartLogic.class, weChatLogic.getClass());
        assertEquals("invalid type", weChatLogic.getReplyFromServer());
    }

}