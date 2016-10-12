package spring.logic;

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

    WeChatLogic(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public abstract String getReplyFromServer();

    public abstract WeChatLogic getReplyFromUser(String weChatId, String messageType, String message);

    void createSession(String weChatId) throws IOException {
        sessionManager.createSession(weChatId);
    }

    void closeSession(String weChatId) {
        sessionManager.closeSession(weChatId);
    }

    LadderReply askLadderServer(String weChatId, byte[] message, long timeOout) {
        sessionManager.getSessionMessage(weChatId).getLadderServerSolver().addMessage(message);

        return waitForReply(weChatId, timeOout);
    }

    private LadderReply waitForReply(String weChatId, long timeOut) {
        FutureTask<byte[]> futureTask = new FutureTask<>(() -> {
            byte[] bytes;
            while (true) {
                bytes = sessionManager.getSessionMessage(weChatId).getMessages();
                if (bytes != null) {
                    break;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    break;
                }
            }
            return bytes;
        });
        Thread thread = new Thread(futureTask);
        thread.start();

        try {
            return new LadderReply(futureTask.get(timeOut, TimeUnit.SECONDS));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            futureTask.cancel(true);
            return null;
        }
    }

    class LadderReply {
        private String command;
        private byte[] body;

        LadderReply(byte[] message) {
            System.out.println("ladder: " + new String(message));
            int index = 0;
            while (message[index] != '#') {
                index++;
            }
            command = new String(message, 0, index);
            body = new byte[message.length - index - 1];
            for (int i = index + 1; i < message.length; i++) {
                body[i - index - 1] = message[i];
            }

            System.out.println(command);
            System.out.println(new String(body));
            System.out.println();
        }

        String getCommand() {
            return command;
        }

        byte[] getBody() {
            return body;
        }
    }

}
