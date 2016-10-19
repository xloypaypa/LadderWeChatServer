package spring.service.ladder;

import spring.config.LadderConfig;
import spring.service.session.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xsu on 16/10/16.
 * it's the mock ladder server solver
 */
public class MockLadderServerSolver extends LadderServerSolver {

    private Map<String, List<MessageReply>> replyMap;

    public MockLadderServerSolver(String weChatId, SessionManager sessionManager, LadderConfig ladderConfig) {
        super(weChatId, sessionManager, ladderConfig);
        this.replyMap = new HashMap<>();
    }

    @Override
    public void addMessage(byte[] message) {
        List<MessageReply> messageReplies = replyMap.get(new String(message));
        if (messageReplies != null) {
            messageReplies.forEach(MessageReply::reply);
        }
    }

    public void addReply(byte[] message, byte[] reply, long waitTime) {
        String key = new String(message);
        if (!this.replyMap.containsKey(key)) {
            this.replyMap.put(key, new ArrayList<>());
        }
        this.replyMap.get(key).add(new MessageReply(reply, waitTime));
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