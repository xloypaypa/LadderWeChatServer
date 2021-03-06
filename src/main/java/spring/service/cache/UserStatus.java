package spring.service.cache;

import spring.config.LadderConfig;
import spring.logic.StartLogic;
import spring.logic.WeChatLogic;
import spring.service.session.SessionManager;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by xsu on 16/10/12.
 * it's the user status
 */
public class UserStatus {

    private Queue<WeChatLogic> currentLogic;

    UserStatus(SessionManager sessionManager, LadderConfig ladderConfig) {
        this.currentLogic = new LinkedList<>();
        this.currentLogic.add(new StartLogic(sessionManager, ladderConfig));
    }

    UserStatus(UserStatus userStatus) {
        this.currentLogic = new LinkedList<>(userStatus.currentLogic);
    }

    public WeChatLogic getNextLogic() {
        return currentLogic.peek();
    }

    public void clear() {
        this.currentLogic.clear();
    }

    public void addNewLogic(WeChatLogic weChatLogic) {
        this.currentLogic.add(weChatLogic);
    }

    public void runLogic(String weChatId, String messageType, String message) {
        currentLogic.poll().getReplyFromUser(this, weChatId, messageType, message);
    }
}
