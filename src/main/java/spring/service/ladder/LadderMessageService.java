package spring.service.ladder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.config.LadderConfig;
import spring.logic.WeChatLogic;
import spring.service.cache.CacheService;
import spring.service.cache.UserStatus;
import spring.service.session.SessionManager;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * Created by xsu on 16/10/11.
 * it's the service of message from ladder gateway
 */
@Service
public class LadderMessageService {

    @Autowired
    private CacheService cacheService;

    public String handleMessage(String weChatId, String messageType, String message) {
        UserStatus userStatus = cacheService.getUserStatus(weChatId);
        WeChatLogic nextLogic = userStatus.getCurrentLogic().getReplyFromUser(weChatId, messageType, message);
        userStatus.updateCurrentLogic(nextLogic);
        cacheService.updateUserStatus(weChatId, userStatus);

        return nextLogic.getReplyFromServer();
    }



}
