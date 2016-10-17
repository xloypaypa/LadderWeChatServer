package spring.service.ladder;

import spring.config.LadderConfig;
import spring.service.session.SessionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xsu on 16/10/16.
 * it's the mock ladder server solver
 */
public class MockLadderServerSolver extends LadderServerSolver {

    private Map<String, MessageReply> replyMap;

    public MockLadderServerSolver(String weChatId, SessionManager sessionManager, LadderConfig ladderConfig) {
        super(weChatId, sessionManager, ladderConfig);
        this.replyMap = new HashMap<>();
    }

    @Override
    public void addMessage(byte[] message) {
        MessageReply messageReply = replyMap.get(new String(message));
        if (messageReply != null) {
            messageReply.reply();
        }
    }

    public void putReply(byte[] message, byte[] reply, long waitTime) {
        this.replyMap.put(new String(message), new MessageReply(reply, waitTime));
    }

    private class MessageReply {
        private byte[] reply;
        private long waitTime;

        private MessageReply(byte[] reply, long waitTime) {
            this.reply = reply;
            this.waitTime = waitTime;
        }

        private void reply() {
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sessionManager.getSessionMessage(weChatId).addMessage(reply);
                }
            }.start();
        }
    }
}