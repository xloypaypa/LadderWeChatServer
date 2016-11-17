package spring.logic.wallet;

import spring.config.LadderConfig;
import spring.logic.WeChatLogic;
import spring.service.cache.UserStatus;
import spring.service.session.SessionManager;
import tools.ProtocolBuilder;

/**
 * Created by xsu on 2016/11/17.
 * it's the create money or budget type logic
 */
class WalletCreateMoneyOrBudgetLogic extends WeChatLogic {

    private String param;
    private String entityType;
    private String typename;

    WalletCreateMoneyOrBudgetLogic(SessionManager sessionManager, LadderConfig ladderConfig, String param) {
        super(sessionManager, ladderConfig);
        this.param = param;
    }

    @Override
    public String getReplyFromServer() {
        switch (param) {
            case "3":
                this.entityType = "money";
                return "please input money type name";
            case "4":
                this.entityType = "budget";
                return "please input budget type name";
            case "value":
                return "please input default value";
            default:
                return "server error";
        }
    }

    @Override
    protected void solveLadderLogic(UserStatus userStatus, String weChatId, String messageType, String message) throws Exception {
        if (param.equals("3") || param.equals("4")) {
            this.typename = message;
            this.param = "value";
            userStatus.addNewLogic(this);
        } else if (param.equals("value")) {
            loginAsUser(weChatId);
            askLadderServer(weChatId, ProtocolBuilder.useApp("wallet"));
            waitForReply(weChatId);
            if (entityType.equals("money")) {
                askLadderServer(weChatId, ProtocolBuilder.addMoney(typename, Double.parseDouble(message)));
            } else {
                askLadderServer(weChatId, ProtocolBuilder.addBudget(typename, Double.parseDouble(message)));
            }
            userStatus.addNewLogic(new WalletMainMenuLogic(sessionManager, ladderConfig));
        }
    }
}
