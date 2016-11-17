package spring.logic.wallet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import spring.config.LadderConfig;
import spring.logic.StartLogic;
import spring.logic.WeChatLogic;
import spring.service.cache.UserStatus;
import spring.service.session.SessionManager;
import tools.ProtocolBuilder;

/**
 * Created by xsu on 2016/10/19.
 * it's the we chat main menu logic
 */
public class WalletMainMenuLogic extends WeChatLogic {
    public WalletMainMenuLogic(SessionManager sessionManager, LadderConfig ladderConfig) {
        super(sessionManager, ladderConfig);
    }

    @Override
    public String getReplyFromServer() {
        return "input 1 to get money list;\n" +
                "input 2 to get budget list;\n" +
                "input 3 to create money type;\n" +
                "input 4 to create budget type;\n" +
                "input 5 to use money;\n" +
                "input 9 to roll back last operation;\n" +
                "input 0 to exit app;";
    }

    @Override
    protected void solveLadderLogic(UserStatus userStatus, String weChatId, String messageType, String message) throws Exception {
        switch (message) {
            case "1":
                loginAsUser(weChatId);
                askLadderServer(weChatId, ProtocolBuilder.useApp("wallet"));
                waitForReply(weChatId);
                LadderReply moneyList = askLadderServer(weChatId, ProtocolBuilder.getMoney());
                JSONArray moneyArray = JSONArray.fromObject(new String(moneyList.getBody()));
                userStatus.addNewLogic(new WalletGetMoneyListLogic(sessionManager, ladderConfig, moneyArray, this));
                break;
            case "2":
                loginAsUser(weChatId);
                askLadderServer(weChatId, ProtocolBuilder.useApp("wallet"));
                waitForReply(weChatId);
                LadderReply budgetList = askLadderServer(weChatId, ProtocolBuilder.getBudget());
                JSONArray budgetArray = JSONArray.fromObject(new String(budgetList.getBody()));
                userStatus.addNewLogic(new WalletGetBudgetListLogic(sessionManager, ladderConfig, budgetArray, this));
                break;
            case "3":
            case "4":
                userStatus.addNewLogic(new WalletCreateMoneyOrBudgetLogic(sessionManager, ladderConfig, message));
                break;
            case "5":
                loginAsUser(weChatId);
                askLadderServer(weChatId, ProtocolBuilder.useApp("wallet"));
                waitForReply(weChatId);
                LadderReply moneyListForUse = askLadderServer(weChatId, ProtocolBuilder.getMoney());
                JSONArray moneyArrayForUse = JSONArray.fromObject(new String(moneyListForUse.getBody()));
                userStatus.addNewLogic(new WalletAskMoneyTypeLogic(sessionManager, ladderConfig, moneyArrayForUse));
                break;
            case "9":
                loginAsUser(weChatId);
                askLadderServer(weChatId, ProtocolBuilder.useApp("wallet"));
                waitForReply(weChatId);
                LadderReply rollBackReply = askLadderServer(weChatId, ProtocolBuilder.rollBack());
                JSONObject rollBackMessage = JSONObject.fromObject(new String(rollBackReply.getBody()));
                userStatus.addNewLogic(new WalletRollbackLogic(sessionManager, ladderConfig, rollBackMessage.getString("result"), this));
                break;
            case "0":
                userStatus.addNewLogic(new StartLogic(sessionManager, ladderConfig));
                break;
            default:
                userStatus.addNewLogic(new StartLogic(sessionManager, ladderConfig, "invalid command"));
                break;
        }
    }
}
