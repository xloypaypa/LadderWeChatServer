package spring.logic.wallet;

import net.sf.json.JSONObject;
import spring.config.LadderConfig;
import spring.logic.StartLogic;
import spring.logic.WeChatLogic;
import spring.service.cache.UserStatus;
import spring.service.session.SessionManager;
import tools.ProtocolBuilder;

/**
 * Created by xsu on 2016/10/20.
 * it's the logic to ask value
 */
class WalletAskValueLogic extends WeChatLogic {

    private String moneyType, budgetType;

    WalletAskValueLogic(SessionManager sessionManager, LadderConfig ladderConfig, String moneyType, String budgetType) {
        super(sessionManager, ladderConfig);
        this.moneyType = moneyType;
        this.budgetType = budgetType;
    }

    @Override
    public String getReplyFromServer() {
        return "please input value.";
    }

    @Override
    protected void solveLadderLogic(UserStatus userStatus, String weChatId, String messageType, String message) throws Exception {
        try {
            double value = Double.parseDouble(message);
            loginAsUser(weChatId);
            askLadderServer(weChatId, ProtocolBuilder.useApp("wallet"));
            waitForReply(weChatId);
            LadderReply useReply = askLadderServer(weChatId, ProtocolBuilder.useMoney(moneyType, budgetType, value));
            JSONObject useMessage = JSONObject.fromObject(new String(useReply.getBody()));
            userStatus.addNewLogic(new WalletUseMoneyResultLogic(sessionManager, ladderConfig,
                    new WalletMainMenuLogic(sessionManager, ladderConfig), useMessage.getString("result")));
        } catch (NumberFormatException e) {
            userStatus.addNewLogic(new StartLogic(sessionManager, ladderConfig, "Value should be a number."));
        }
    }
}
