package spring.logic.wallet;

import net.sf.json.JSONObject;
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
    private String reply;
    private WalletMainMenuLogic walletMainMenuLogic = new WalletMainMenuLogic(sessionManager, ladderConfig);

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
            case "end":
                return "create " + reply + "\n\n" + walletMainMenuLogic.getReplyFromServer();
            default:
                return "server error";
        }
    }

    @Override
    protected void solveLadderLogic(UserStatus userStatus, String weChatId, String messageType, String message) throws Exception {
        switch (param) {
            case "3":
            case "4":
                this.typename = message;
                this.param = "value";
                userStatus.addNewLogic(this);
                break;
            case "value":
                loginAsUser(weChatId);
                askLadderServer(weChatId, ProtocolBuilder.useApp("wallet"));
                waitForReply(weChatId);
                LadderReply ladderReply;
                if (entityType.equals("money")) {
                    ladderReply = askLadderServer(weChatId, ProtocolBuilder.addMoney(typename, Double.parseDouble(message)));
                } else {
                    ladderReply = askLadderServer(weChatId, ProtocolBuilder.addBudget(typename, Double.parseDouble(message)));
                }
                this.param = "end";
                reply = JSONObject.fromObject(new String(ladderReply.getBody())).getString("result");
                userStatus.addNewLogic(this);
                break;
            case "end":
                walletMainMenuLogic.solveLadderLogic(userStatus, weChatId, messageType, message);
                break;
        }
    }
}
