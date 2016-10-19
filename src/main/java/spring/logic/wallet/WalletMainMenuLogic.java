package spring.logic.wallet;

import net.sf.json.JSONArray;
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
        return "input 1 to get money list; input 0 to exit app; ";
    }

    @Override
    protected WeChatLogic solveLadderLogic(String weChatId, String messageType, String message) throws Exception {
        switch (message) {
            case "1":
                loginAsUser(weChatId);
                askLadderServer(weChatId, ProtocolBuilder.useApp("wallet"), 500);
                LadderReply moneyList = askLadderServer(weChatId, ProtocolBuilder.getMoney(), 2000);
                JSONArray jsonArray = JSONArray.fromObject(new String(moneyList.getBody()));
                return new WalletGetMoneyListLogic(sessionManager, ladderConfig, jsonArray, this);
            case "0":
                return new StartLogic(sessionManager, ladderConfig);
            default:
                return new ExceptionLogic(sessionManager, ladderConfig, "invalid command");
        }
    }
}
