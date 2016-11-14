package spring.logic.wallet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import spring.config.LadderConfig;
import spring.logic.StartLogic;
import spring.logic.WeChatLogic;
import spring.service.cache.UserStatus;
import spring.service.session.SessionManager;

/**
 * Created by xsu on 2016/10/20.
 * it's the logic to ask budget type
 */
class WalletAskBudgetTypeLogic extends WeChatLogic {

    private JSONArray budgetTypes;
    private String moneyType;

    WalletAskBudgetTypeLogic(SessionManager sessionManager, LadderConfig ladderConfig, JSONArray budgetTypes, String moneyType) {
        super(sessionManager, ladderConfig);
        this.budgetTypes = budgetTypes;
        this.moneyType = moneyType;
    }

    @Override
    public String getReplyFromServer() {
        String result = "please reply one of budget type:\n";
        for (int i = 0; i < budgetTypes.size(); i++) {
            JSONObject now = budgetTypes.getJSONObject(i);
            result += (i + 1) + ". " + now.getString("typename") + "\n";
        }
        return result;
    }

    @Override
    protected void solveLadderLogic(UserStatus userStatus, String weChatId, String messageType, String message) throws Exception {
        try {
            int index = Integer.parseInt(message) - 1;
            if (index < 0 || index >= budgetTypes.size()) {
                userStatus.addNewLogic(new StartLogic(sessionManager, ladderConfig, "invalid type"));
            } else {
                userStatus.addNewLogic(new WalletAskValueLogic(sessionManager, ladderConfig, moneyType, budgetTypes.getJSONObject(index).getString("typename")));
            }
        } catch (NumberFormatException e) {
            userStatus.addNewLogic(new StartLogic(sessionManager, ladderConfig, "invalid type"));
        }
    }

}
