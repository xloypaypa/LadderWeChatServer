package spring.logic;

import spring.config.LadderConfig;
import spring.service.session.SessionManager;

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

    SessionManager sessionManager;
    LadderConfig ladderConfig;

    WeChatLogic(SessionManager sessionManager, LadderConfig ladderConfig) {
        this.sessionManager = sessionManager;
        this.ladderConfig = ladderConfig;
    }

    public abstract String getReplyFromServer();
    
    abstract WeChatLogic solveLadderLogic(String weChatId, String messageType, String message) throws Exception;

    public WeChatLogic getReplyFromUser(String weChatId, String messageType, String message) {
        try {
            createSession(weChatId);
        } catch (IOException e) {
            return new ExceptionLogic(this.sessionManager, ladderConfig, "can't connect");
        }

        WeChatLogic result;
        try {
            result = solveLadderLogic(weChatId, messageType, message);
        } catch (Exception e) {
            result = new ExceptionLogic(this.sessionManager, ladderConfig, "time out");
        }
        closeSession(weChatId);
        return result;
    }

    private void createSession(String weChatId) throws IOException {
        sessionManager.createSession(weChatId);
    }

    private void closeSession(String weChatId) {
        sessionManager.closeSession(weChatId);
    }

    LadderReply askLadderServer(String weChatId, byte[] message, long timeOut) throws Exception {
        sessionManager.getSessionMessage(weChatId).getLadderServerSolver().addMessage(message);

        return waitForReply(weChatId, timeOut);
    }

    private LadderReply waitForReply(String weChatId, long timeOut) throws Exception {
        FutureTask<byte[]> futureTask = new FutureTask<>(() -> {
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
            return bytes;
        });
        Thread thread = new Thread(futureTask);
        thread.start();

        try {
            return new LadderReply(futureTask.get(timeOut, TimeUnit.MILLISECONDS));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            futureTask.cancel(true);
            throw e;
        }
    }

    class LadderReply {
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

        String getCommand() {
            return command;
        }

        byte[] getBody() {
            return body;
        }
    }

}
