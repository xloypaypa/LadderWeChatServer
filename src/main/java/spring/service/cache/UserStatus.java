package spring.service.cache;

import spring.config.LadderConfig;
import spring.logic.StartLogic;
import spring.logic.WeChatLogic;
import spring.service.session.SessionManager;

/**
 * Created by xsu on 16/10/12.
 * it's the user status
 */
public class UserStatus {

    private WeChatLogic currentLogic;

    UserStatus(SessionManager sessionManager, LadderConfig ladderConfig) {
        this.currentLogic = new StartLogic(sessionManager, ladderConfig);
    }

    UserStatus(UserStatus userStatus) {
        this.currentLogic = userStatus.getCurrentLogic();
    }

    public WeChatLogic getCurrentLogic() {
        return currentLogic;
    }

    public void updateCurrentLogic(WeChatLogic weChatLogic) {
        this.currentLogic = weChatLogic;
    }
}
