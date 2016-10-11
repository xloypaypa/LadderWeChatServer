package spring.service.ladder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.config.LadderConfig;
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
    private SessionManager sessionManager;

    @Autowired
    private LadderConfig ladderConfig;

    public String handleMessage(String weChatId, String messageType, String message) {
        try {
            sessionManager.createSession(weChatId);
        } catch (IOException e) {
            e.printStackTrace();
            return "can't create connection to ladder";
        }

        sessionManager.getSessionMessage(weChatId).addMessage("/testCommand#{}".getBytes());

        byte[] result = waitForReply(weChatId);

        sessionManager.closeSession(weChatId);

        return new String(result);
    }

    private byte[] waitForReply(String weChatId) {
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
            return futureTask.get(ladderConfig.getTimeOut(), TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            futureTask.cancel(true);
            return "time out".getBytes();
        }
    }

}
