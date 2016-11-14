package spring.logic.wallet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import spring.logic.LogicTest;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Created by xsu on 2016/10/20.
 * it's the testing code for show wallet money list
 */
public class WalletGetBudgetListLogicTest extends LogicTest {

    private WalletMainMenuLogic walletMainMenuLogic;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        walletMainMenuLogic = spy(new WalletMainMenuLogic(sessionManager, ladderConfig));
    }

    @Test
    public void should_show_list_and_main_menu() throws Exception {
        JSONArray jsonArray = new JSONArray();
        JSONObject typeOne = new JSONObject();
        typeOne.put("typename", "a");
        typeOne.put("value", 1);
        JSONObject typeTwo = new JSONObject();
        typeTwo.put("typename", "b");
        typeTwo.put("value", 1.99998);
        jsonArray.add(typeOne);
        jsonArray.add(typeTwo);

        WalletGetBudgetListLogic walletGetBudgetListLogic = new WalletGetBudgetListLogic(sessionManager, ladderConfig, jsonArray, walletMainMenuLogic);
        assertTrue(walletGetBudgetListLogic.getReplyFromServer().startsWith("your budget list is:\n" +
                "a => 1.00\n" +
                "b => 2.00\n" +
                "\n"));
    }

    @Test
    public void should_use_main_menu_logic_when_get_message() throws Exception {
        WalletGetBudgetListLogic walletGetBudgetListLogic = new WalletGetBudgetListLogic(sessionManager, ladderConfig, null, walletMainMenuLogic);

        walletGetBudgetListLogic.solveLadderLogic(userStatus, "id", "type", "message");

        verify(walletMainMenuLogic).solveLadderLogic(userStatus, "id", "type", "message");
    }

}