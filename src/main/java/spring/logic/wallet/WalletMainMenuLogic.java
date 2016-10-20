package spring.logic.wallet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import spring.config.LadderConfig;
import spring.logic.ExceptionLogic;
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
        return "input 1 to get money list; input 2 to get budget list; input 9 to roll back last operation; input 0 to exit app; ";
    }

    @Override
    protected WeChatLogic solveLadderLogic(String weChatId, String messageType, String message) throws Exception {
        switch (message) {
            case "1":
                loginAsUser(weChatId);
                askLadderServer(weChatId, ProtocolBuilder.useApp("wallet"), 1000);
                waitForReply(weChatId, 2000);
                LadderReply moneyList = askLadderServer(weChatId, ProtocolBuilder.getMoney(), 1000);
                JSONArray moneyArray = JSONArray.fromObject(new String(moneyList.getBody()));
                return new WalletGetMoneyListLogic(sessionManager, ladderConfig, moneyArray, this);
            case "2":
                loginAsUser(weChatId);
                askLadderServer(weChatId, ProtocolBuilder.useApp("wallet"), 1000);
                waitForReply(weChatId, 2000);
                LadderReply budgetList = askLadderServer(weChatId, ProtocolBuilder.getBudget(), 1000);
                JSONArray budgetArray = JSONArray.fromObject(new String(budgetList.getBody()));
                return new WalletGetBudgetListLogic(sessionManager, ladderConfig, budgetArray, this);
            case "9":
                loginAsUser(weChatId);
                askLadderServer(weChatId, ProtocolBuilder.useApp("wallet"), 1000);
                waitForReply(weChatId, 2000);
                LadderReply rollBackReply = askLadderServer(weChatId, ProtocolBuilder.rollBack(), 1000);
                JSONObject rollBackMessage = JSONObject.fromObject(new String(rollBackReply.getBody()));
                return new WalletRollbackLogic(sessionManager, ladderConfig, rollBackMessage.getString("result"), this);
            case "0":
                return new StartLogic(sessionManager, ladderConfig);
            default:
                return new ExceptionLogic(sessionManager, ladderConfig, "invalid command");
        }
    }
}
