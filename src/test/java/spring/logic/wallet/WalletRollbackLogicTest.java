package spring.logic.wallet;

import org.junit.Before;
import org.junit.Test;
import spring.logic.LogicTest;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Created by xsu on 2016/10/20.
 * it's the testing logic for roll back
 */
public class WalletRollbackLogicTest extends LogicTest {

    private WalletMainMenuLogic walletMainMenuLogic;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        walletMainMenuLogic = spy(new WalletMainMenuLogic(sessionManager, ladderConfig));
    }

    @Test
    public void should_show_result() throws Exception {
        WalletRollbackLogic walletRollbackLogic = new WalletRollbackLogic(sessionManager, ladderConfig, "ok", walletMainMenuLogic);
        assertTrue(walletRollbackLogic.getReplyFromServer().startsWith("roll back ok\n" +
                "\n"));
    }

    @Test
    public void should_use_main_menu_logic_when_get_message() throws Exception {
        WalletGetBudgetListLogic walletGetBudgetListLogic = new WalletGetBudgetListLogic(sessionManager, ladderConfig, null, walletMainMenuLogic);

        walletGetBudgetListLogic.solveLadderLogic(userStatus, "id", "type", "message");

        verify(walletMainMenuLogic).solveLadderLogic(userStatus, "id", "type", "message");
    }

}