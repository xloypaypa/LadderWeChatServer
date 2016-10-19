package spring.logic.wallet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import spring.logic.LogicTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Created by xsu on 2016/10/19.
 * it's the testing code for show wallet money list
 */
public class WalletGetMoneyListLogicTest extends LogicTest {

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

        WalletGetMoneyListLogic walletGetMoneyListLogic = new WalletGetMoneyListLogic(sessionManager, ladderConfig, jsonArray, walletMainMenuLogic);
        assertEquals("your money list is:\n" +
                "a => 1.00\n" +
                "b => 2.00\n" +
                "\n" +
                "input 1 to get money list; input 0 to exit app; ", walletGetMoneyListLogic.getReplyFromServer());
    }

    @Test
    public void should_use_main_menu_logic_when_get_message() throws Exception {
        WalletGetMoneyListLogic walletGetMoneyListLogic = new WalletGetMoneyListLogic(sessionManager, ladderConfig, null, walletMainMenuLogic);

        walletGetMoneyListLogic.solveLadderLogic("id", "type", "message");

        verify(walletMainMenuLogic).solveLadderLogic("id", "type", "message");
    }
}