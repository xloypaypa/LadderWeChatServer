package spring.logic.wallet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import spring.config.LadderConfig;
import spring.logic.ExceptionLogic;
import spring.logic.WeChatLogic;
import spring.service.session.SessionManager;
import tools.ProtocolBuilder;

/**
 * Created by xsu on 2016/10/20.
 * it's the logic to ask money type
 */
class WalletAskMoneyTypeLogic extends WeChatLogic {

    private JSONArray moneyTypes;

    WalletAskMoneyTypeLogic(SessionManager sessionManager, LadderConfig ladderConfig, JSONArray moneyTypes) {
        super(sessionManager, ladderConfig);
        this.moneyTypes = moneyTypes;
    }

    @Override
    public String getReplyFromServer() {
        String result = "please reply one of money type:\n";
        for (int i = 0; i < moneyTypes.size(); i++) {
            JSONObject now = moneyTypes.getJSONObject(i);
            result += (i + 1) + ". " + now.getString("typename") + "\n";
        }
        return result;
    }

    @Override
    protected WeChatLogic solveLadderLogic(String weChatId, String messageType, String message) throws Exception {
        try {
            int index = Integer.parseInt(message) - 1;
            if (index < 0 || index >= moneyTypes.size()) {
                return new ExceptionLogic(sessionManager, ladderConfig, "invalid type");
            } else {
                loginAsUser(weChatId);
                askLadderServer(weChatId, ProtocolBuilder.useApp("wallet"));
                waitForReply(weChatId);
                LadderReply budgetList = askLadderServer(weChatId, ProtocolBuilder.getBudget());
                JSONArray budgetArray = JSONArray.fromObject(new String(budgetList.getBody()));

                return new WalletAskBudgetTypeLogic(sessionManager, ladderConfig, budgetArray, moneyTypes.getJSONObject(index).getString("typename"));
            }
        } catch (NumberFormatException e) {
            return new ExceptionLogic(sessionManager, ladderConfig, "invalid type");
        }
    }
}
