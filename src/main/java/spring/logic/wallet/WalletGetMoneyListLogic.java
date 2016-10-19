package spring.logic.wallet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import spring.config.LadderConfig;
import spring.logic.WeChatLogic;
import spring.service.session.SessionManager;

import java.text.DecimalFormat;

/**
 * Created by xsu on 2016/10/19.
 * it's the logic for get money list in wallet
 */
class WalletGetMoneyListLogic extends WeChatLogic {

    private JSONArray jsonArray;
    private WalletMainMenuLogic walletMainMenuLogic;

    WalletGetMoneyListLogic(SessionManager sessionManager, LadderConfig ladderConfig, JSONArray jsonArray, WalletMainMenuLogic walletMainMenuLogic) {
        super(sessionManager, ladderConfig);
        this.jsonArray = jsonArray;
        this.walletMainMenuLogic = walletMainMenuLogic;
    }

    @Override
    public String getReplyFromServer() {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String result = "your money list is:\n";
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject now = jsonArray.getJSONObject(i);
            result += now.getString("typename") + " => " + decimalFormat.format(now.getDouble("value")) + "\n";
        }
        result += "\n";
        result += "input 1 to get money list; input 0 to exit app; ";
        return result;
    }

    @Override
    protected WeChatLogic solveLadderLogic(String weChatId, String messageType, String message) throws Exception {
        return walletMainMenuLogic.solveLadderLogic(weChatId, messageType, message);
    }
}
