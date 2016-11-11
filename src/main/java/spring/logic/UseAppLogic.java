package spring.logic;

import spring.config.LadderConfig;
import spring.logic.wallet.WalletMainMenuLogic;
import spring.service.session.SessionManager;

/**
 * Created by xsu on 2016/10/19.
 * it's the logic to ask user choose a app
 */
class UseAppLogic extends WeChatLogic {

    UseAppLogic(SessionManager sessionManager, LadderConfig ladderConfig) {
        super(sessionManager, ladderConfig);
    }

    @Override
    public String getReplyFromServer() {
        String[] availableApps = ladderConfig.getAvailableApp();
        String reply = "";
        for (int index = 0; index < availableApps.length; index++) {
            reply += "input " + (index + 1) + " to use " + availableApps[index] + "; ";
        }
        return reply;
    }

    @Override
    protected WeChatLogic solveLadderLogic(String weChatId, String messageType, String message) throws Exception {
        try {
            int index = Integer.parseInt(message) - 1;
            if (index < 0 || index >= ladderConfig.getAvailableApp().length) {
                return new StartLogic(sessionManager, ladderConfig, "no such application");
            } else {
                return new WalletMainMenuLogic(sessionManager, ladderConfig);
            }
        } catch (NumberFormatException e) {
            return new StartLogic(sessionManager, ladderConfig, "no such application");
        }
    }
}
