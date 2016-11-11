package spring.service.ladder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.service.cache.CacheService;
import spring.service.cache.UserStatus;

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
        userStatus.runLogic(weChatId, messageType, message);
        cacheService.updateUserStatus(weChatId, userStatus);

        return userStatus.getNextLogic().getReplyFromServer();
    }

}
