package spring.logic.wallet;

import spring.config.LadderConfig;
import spring.logic.WeChatLogic;
import spring.service.session.SessionManager;

/**
 * Created by xsu on 2016/10/20.
 * it's the logic about use money result
 */
class WalletUseMoneyResultLogic extends WeChatLogic {

    private WalletMainMenuLogic walletMainMenuLogic;
    private String result;

    WalletUseMoneyResultLogic(SessionManager sessionManager, LadderConfig ladderConfig, WalletMainMenuLogic walletMainMenuLogic, String result) {
        super(sessionManager, ladderConfig);
        this.walletMainMenuLogic = walletMainMenuLogic;
        this.result = result;
    }

    @Override
    public String getReplyFromServer() {
        return "use money " + result + "\n\n" + this.walletMainMenuLogic.getReplyFromServer();
    }

    @Override
    protected WeChatLogic solveLadderLogic(String weChatId, String messageType, String message) throws Exception {
        return walletMainMenuLogic.solveLadderLogic(weChatId, messageType, message);
    }
}
