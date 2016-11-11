package spring.logic;

import net.sf.json.JSONObject;
import spring.config.LadderConfig;
import spring.service.cache.UserStatus;
import spring.service.session.SessionManager;
import tools.ProtocolBuilder;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by xsu on 16/10/12.
 * it's the we chat logic
 */
public abstract class WeChatLogic {

    protected SessionManager sessionManager;
    protected LadderConfig ladderConfig;

    protected WeChatLogic(SessionManager sessionManager, LadderConfig ladderConfig) {
        this.sessionManager = sessionManager;
        this.ladderConfig = ladderConfig;
    }

    public abstract String getReplyFromServer();
    
    protected abstract WeChatLogic solveLadderLogic(String weChatId, String messageType, String message) throws Exception;

    public void getReplyFromUser(UserStatus userStatus, String weChatId, String messageType, String message) {
        FutureTask<WeChatLogic> futureTask = new FutureTask<>(() -> {
            try {
                createSession(weChatId);
            } catch (IOException e) {
                return new StartLogic(this.sessionManager, ladderConfig, "can't connect");
            }

            WeChatLogic result;
            try {
                result = solveLadderLogic(weChatId, messageType, message);
            } catch (Exception e) {
                result = new StartLogic(this.sessionManager, ladderConfig, "exception: " + e.getMessage());
            }
            closeSession(weChatId);
            return result;
        });
        Thread thread = new Thread(futureTask);
        thread.start();
        try {
            userStatus.addNewLogic(futureTask.get(4500, TimeUnit.MILLISECONDS));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            futureTask.cancel(true);
            userStatus.addNewLogic(new StartLogic(this.sessionManager, ladderConfig, "time out"));
        }
    }

    private void createSession(String weChatId) throws IOException {
        sessionManager.createSession(weChatId);
    }

    private void closeSession(String weChatId) {
        sessionManager.closeSession(weChatId);
    }

    protected LadderReply loginAsUser(String weChatId) throws Exception {
        askLadderServer(weChatId, ProtocolBuilder.key(ladderConfig.getPublicKey()));
        sessionManager.getSessionMessage(weChatId).getLadderServerSolver().setEncrypt(true);

        LadderReply sessionReply = askLadderServer(weChatId, ProtocolBuilder.getSessionId());
        String sessionId = JSONObject.fromObject(new String(sessionReply.getBody())).getString("result");

        askLadderServer(weChatId,
                ProtocolBuilder.login(ladderConfig.getUsername(), ladderConfig.getPassword(), sessionId));

        return askLadderServer(weChatId,
                ProtocolBuilder.changeConnectionUserByWeChat(weChatId));
    }

    protected LadderReply askLadderServer(String weChatId, byte[] message) throws Exception {
        sessionManager.getSessionMessage(weChatId).getLadderServerSolver().addMessage(message);

        return waitForReply(weChatId);
    }

    protected LadderReply waitForReply(String weChatId) throws Exception {
        byte[] bytes;
        while (true) {
            bytes = sessionManager.getSessionMessage(weChatId).getMessages();
            if (bytes != null) {
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
        return new LadderReply(bytes);
    }

    protected class LadderReply {
        private String command;
        private byte[] body;

        LadderReply(byte[] message) {
            int index = 0;
            while (message[index] != '#' && index < message.length) {
                index++;
            }
            command = new String(message, 0, index);
            body = new byte[message.length - index - 1];
            for (int i = index + 1; i < message.length; i++) {
                body[i - index - 1] = message[i];
            }
        }

        public String getCommand() {
            return command;
        }

        public byte[] getBody() {
            return body;
        }
    }

}
