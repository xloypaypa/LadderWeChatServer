package spring.logic.wallet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import spring.config.LadderConfig;
import spring.logic.StartLogic;
import spring.logic.WeChatLogic;
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
                "input 3 to use money;\n" +
                "input 9 to roll back last operation;\n" +
                "input 0 to exit app;";
    }

    @Override
    protected WeChatLogic solveLadderLogic(String weChatId, String messageType, String message) throws Exception {
        switch (message) {
            case "1":
                loginAsUser(weChatId);
                askLadderServer(weChatId, ProtocolBuilder.useApp("wallet"));
                waitForReply(weChatId);
                LadderReply moneyList = askLadderServer(weChatId, ProtocolBuilder.getMoney());
                JSONArray moneyArray = JSONArray.fromObject(new String(moneyList.getBody()));
                return new WalletGetMoneyListLogic(sessionManager, ladderConfig, moneyArray, this);
            case "2":
                loginAsUser(weChatId);
                askLadderServer(weChatId, ProtocolBuilder.useApp("wallet"));
                waitForReply(weChatId);
                LadderReply budgetList = askLadderServer(weChatId, ProtocolBuilder.getBudget());
                JSONArray budgetArray = JSONArray.fromObject(new String(budgetList.getBody()));
                return new WalletGetBudgetListLogic(sessionManager, ladderConfig, budgetArray, this);
            case "3":
                loginAsUser(weChatId);
                askLadderServer(weChatId, ProtocolBuilder.useApp("wallet"));
                waitForReply(weChatId);
                LadderReply moneyListForUse = askLadderServer(weChatId, ProtocolBuilder.getMoney());
                JSONArray moneyArrayForUse = JSONArray.fromObject(new String(moneyListForUse.getBody()));
                return new WalletAskMoneyTypeLogic(sessionManager, ladderConfig, moneyArrayForUse);
            case "9":
                loginAsUser(weChatId);
                askLadderServer(weChatId, ProtocolBuilder.useApp("wallet"));
                waitForReply(weChatId);
                LadderReply rollBackReply = askLadderServer(weChatId, ProtocolBuilder.rollBack());
                JSONObject rollBackMessage = JSONObject.fromObject(new String(rollBackReply.getBody()));
                return new WalletRollbackLogic(sessionManager, ladderConfig, rollBackMessage.getString("result"), this);
            case "0":
                return new StartLogic(sessionManager, ladderConfig);
            default:
                return new StartLogic(sessionManager, ladderConfig, "invalid command");
        }
    }
}
