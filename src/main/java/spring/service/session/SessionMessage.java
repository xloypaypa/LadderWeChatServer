package spring.service.session;

import spring.service.ladder.LadderServerSolver;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by xlo on 16/3/1.
 * it's the session message
 */
public class SessionMessage {

    private LadderServerSolver ladderServerSolver;
    private List<byte[]> messages;

    public SessionMessage(LadderServerSolver ladderServerSolver) {
        this.ladderServerSolver = ladderServerSolver;
        this.messages = new LinkedList<>();
    }

    public LadderServerSolver getLadderServerSolver() {
        return ladderServerSolver;
    }

    public void setLadderServerSolver(LadderServerSolver ladderServerSolver) {
        this.ladderServerSolver = ladderServerSolver;
    }

    public synchronized byte[] getMessages() {
        if (!messages.isEmpty()) {
            byte[] bytes = messages.get(0);
            messages.remove(0);
            return bytes;
        } else {
            return null;
        }
    }

    public synchronized void addMessage(byte[] message) {
        this.messages.add(message);
    }
}
