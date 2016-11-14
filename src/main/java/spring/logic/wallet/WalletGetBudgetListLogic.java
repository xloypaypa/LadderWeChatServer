package spring.logic.wallet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import spring.config.LadderConfig;
import spring.logic.WeChatLogic;
import spring.service.cache.UserStatus;
import spring.service.session.SessionManager;

import java.text.DecimalFormat;

/**
 * Created by xsu on 2016/10/19.
 * it's the logic for get money list in wallet
 */
class WalletGetBudgetListLogic extends WeChatLogic {

    private JSONArray jsonArray;
    private WalletMainMenuLogic walletMainMenuLogic;

    WalletGetBudgetListLogic(SessionManager sessionManager, LadderConfig ladderConfig, JSONArray jsonArray, WalletMainMenuLogic walletMainMenuLogic) {
        super(sessionManager, ladderConfig);
        this.jsonArray = jsonArray;
        this.walletMainMenuLogic = walletMainMenuLogic;
    }

    @Override
    public String getReplyFromServer() {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String result = "your budget list is:\n";
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject now = jsonArray.getJSONObject(i);
            result += now.getString("typename") + " => " + decimalFormat.format(now.getDouble("value")) + "\n";
        }
        result += "\n";
        result += this.walletMainMenuLogic.getReplyFromServer();
        return result;
    }

    @Override
    protected void solveLadderLogic(UserStatus userStatus, String weChatId, String messageType, String message) throws Exception {
        walletMainMenuLogic.solveLadderLogic(userStatus, weChatId, messageType, message);
    }
}
