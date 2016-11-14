package spring.logic.wallet;

import spring.config.LadderConfig;
import spring.logic.WeChatLogic;
import spring.service.cache.UserStatus;
import spring.service.session.SessionManager;

/**
 * Created by xsu on 2016/10/19.
 * it's the logic for get money list in wallet
 */
class WalletRollbackLogic extends WeChatLogic {

    private String result;
    private WalletMainMenuLogic walletMainMenuLogic;

    WalletRollbackLogic(SessionManager sessionManager, LadderConfig ladderConfig, String result, WalletMainMenuLogic walletMainMenuLogic) {
        super(sessionManager, ladderConfig);
        this.result = result;
        this.walletMainMenuLogic = walletMainMenuLogic;
    }

    @Override
    public String getReplyFromServer() {
        String result = "roll back " + this.result + "\n";
        result += "\n";
        result += this.walletMainMenuLogic.getReplyFromServer();
        return result;
    }

    @Override
    protected void solveLadderLogic(UserStatus userStatus, String weChatId, String messageType, String message) throws Exception {
        walletMainMenuLogic.solveLadderLogic(userStatus, weChatId, messageType, message);
    }
}
