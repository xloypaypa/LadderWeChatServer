package spring.logic.wallet;

import org.junit.Before;
import org.junit.Test;
import spring.logic.LogicTest;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Created by xsu on 2016/10/20.
 * it's the testing code for wallet show result of using money
 */
public class WalletUseMoneyResultLogicTest extends LogicTest {

    private WalletMainMenuLogic walletMainMenuLogic;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        walletMainMenuLogic = spy(new WalletMainMenuLogic(sessionManager, ladderConfig));
    }

    @Test
    public void should_show_result_and_main_menu() throws Exception {
        WalletUseMoneyResultLogic walletUseMoneyResultLogic =
                new WalletUseMoneyResultLogic(sessionManager, ladderConfig, walletMainMenuLogic, "ok");
        assertTrue(walletUseMoneyResultLogic.getReplyFromServer().startsWith("use money ok\n\n"));
    }

    @Test
    public void should_use_main_menu_logic_when_get_message() throws Exception {
        WalletUseMoneyResultLogic walletUseMoneyResultLogic =
                new WalletUseMoneyResultLogic(sessionManager, ladderConfig, walletMainMenuLogic, "ok");

        walletUseMoneyResultLogic.solveLadderLogic(userStatus, "id", "type", "message");

        verify(walletMainMenuLogic).solveLadderLogic(userStatus, "id", "type", "message");
    }

}