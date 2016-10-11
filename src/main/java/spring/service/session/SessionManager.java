package spring.service.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.service.ladder.LadderServerSolver;
import spring.service.ladder.LadderService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by xlo on 16/2/29.
 * it's the session manager
 */
@Service
public class SessionManager {

    @Autowired
    private LadderService ladderService;

    private Map<String, SessionMessage> sessionMessageMap;

    private SessionManager() {
        sessionMessageMap = new HashMap<>();
    }

    public synchronized void createSession(String weChatId) throws IOException {
        LadderServerSolver connectionSolver = new LadderServerSolver(weChatId, this);
        ladderService.connect(connectionSolver);
        sessionMessageMap.put(weChatId, new SessionMessage(connectionSolver));
    }

    public synchronized void closeSession(String weChatId) {
        this.sessionMessageMap.remove(weChatId).getLadderServerSolver().whenClosing();
    }

    public synchronized SessionMessage getSessionMessage(String weChatId) {
        if (sessionMessageMap.containsKey(weChatId)) {
            return sessionMessageMap.get(weChatId);
        } else {
            return null;
        }
    }
}
