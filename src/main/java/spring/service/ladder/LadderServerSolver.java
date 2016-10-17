package spring.service.ladder;

import net.server.AbstractServer;
import net.tool.connectionManager.ConnectionManager;
import net.tool.connectionSolver.ConnectionMessageImpl;
import net.tool.connectionSolver.ConnectionStatus;
import net.tool.packageSolver.PackageStatus;
import net.tool.packageSolver.packageReader.LadderProtocolReader;
import net.tool.packageSolver.packageReader.PackageReader;
import net.tool.packageSolver.packageWriter.LadderProtocolWriter;
import net.tool.packageSolver.packageWriter.PackageWriter;
import net.tool.packageSolver.packageWriter.packageWriterFactory.LadderProtocolFactory;
import spring.config.LadderConfig;
import spring.service.session.SessionManager;
import spring.tools.RSA;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Created by xlo on 16/2/26.
 * it's the solver to connect ladder gateway
 */
public class LadderServerSolver extends AbstractServer {

    private volatile PackageReader packageReader;
    private volatile PackageWriter packageWriter;

    private volatile boolean isEncrypt = false;

    final String weChatId;
    final SessionManager sessionManager;
    private final LadderConfig ladderConfig;

    public LadderServerSolver(String weChatId, SessionManager sessionManager, LadderConfig ladderConfig) {
        super(new ConnectionMessageImpl());
        this.weChatId = weChatId;
        this.sessionManager = sessionManager;
        this.ladderConfig = ladderConfig;
    }

    @Override
    public ConnectionStatus whenWaiting() {
        if (this.packageWriter.getPackageQueueSize() == 0) {
            return ConnectionStatus.READING;
        } else {
            return ConnectionStatus.WRITING;
        }
    }

    @Override
    public ConnectionStatus whenReading() {
        try {
            PackageStatus packageStatus = packageReader.read();
            if (packageStatus.equals(PackageStatus.END)) {
                byte[] body = this.packageReader.getBody();
                body = decrypt(body);
                sessionManager.getSessionMessage(weChatId).addMessage(body);
                return ConnectionStatus.READING;
            } else if (packageStatus.equals(PackageStatus.WAITING)) {
                return getNextStatusWhenPackageEnd();
            } else if (packageStatus.equals(PackageStatus.ERROR)) {
                return ConnectionStatus.ERROR;
            } else if (packageStatus.equals(PackageStatus.CLOSED)) {
                return ConnectionStatus.CLOSE;
            } else {
                return ConnectionStatus.READING;
            }
        } catch (Exception e) {
            return ConnectionStatus.ERROR;
        }
    }

    @Override
    public ConnectionStatus whenWriting() {
        try {
            PackageStatus packageStatus = this.packageWriter.write();
            if (packageStatus.equals(PackageStatus.END)) {
                return getNextStatusWhenPackageEnd();
            } else if (packageStatus.equals(PackageStatus.WAITING)) {
                return ConnectionStatus.WAITING;
            } else if (packageStatus.equals(PackageStatus.ERROR)) {
                return ConnectionStatus.ERROR;
            } else if (packageStatus.equals(PackageStatus.CLOSED)) {
                return ConnectionStatus.CLOSE;
            } else {
                return ConnectionStatus.READING;
            }
        } catch (IOException e) {
            return ConnectionStatus.ERROR;
        }
    }

    private ConnectionStatus getNextStatusWhenPackageEnd() {
        if (this.packageWriter.getPackageQueueSize() == 0) {
            this.getConnectionMessage().getSelectionKey().interestOps(SelectionKey.OP_READ);
        } else {
            this.getConnectionMessage().getSelectionKey().interestOps(SelectionKey.OP_WRITE);
        }
        return ConnectionStatus.WAITING;
    }

    @Override
    public ConnectionStatus whenError() {
        return ConnectionStatus.CLOSE;
    }

    public void addMessage(byte[] message) {
        byte[] encrypt = message;
        try {
            encrypt = encrypt(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] commandPackage = LadderProtocolFactory.getLadderProtocolFactory().setMessage(encrypt).getLadderProtocolByte();
        this.packageWriter.addPackage(commandPackage, 0);
        this.getConnectionMessage().getSelectionKey().interestOps(SelectionKey.OP_WRITE);
        this.getConnectionMessage().getSelectionKey().selector().wakeup();
    }

    @Override
    public ConnectionStatus whenInit() {
        this.packageReader = new LadderProtocolReader(this.getConnectionMessage().getSocket());
        this.packageWriter = new LadderProtocolWriter(this.getConnectionMessage().getSocket());
        return ConnectionStatus.CONNECTING;
    }

    @Override
    public ConnectionStatus whenConnecting() {
        if (this.getConnectionMessage().getSelectionKey().isConnectable()) {
            SocketChannel socket = this.getConnectionMessage().getSocket();
            try {
                if (socket.finishConnect()) {
                    return getNextStatusWhenPackageEnd();
                } else {
                    return ConnectionStatus.CONNECTING;
                }
            } catch (IOException e) {
                return ConnectionStatus.ERROR;
            }
        } else {
            return ConnectionStatus.CONNECTING;
        }
    }

    protected ConnectionStatus whenError(Exception e) {
        e.printStackTrace();
        return ConnectionStatus.WAITING;
    }

    @Override
    public ConnectionStatus whenClosing() {
        if (this.getConnectionMessage() != null) {
            ConnectionManager.getSolverManager().removeConnection(this.getConnectionMessage().getSocket().socket());
            this.getConnectionMessage().closeSocket();
        }
        return null;
    }

    private byte[] encrypt(byte[] message) throws Exception {
        if (isEncrypt) {
            return RSA.encrypt(ladderConfig.getServerKey(), message);
        } else {
            return message;
        }
    }

    private byte[] decrypt(byte[] message) throws Exception {
        if (isEncrypt) {
            return RSA.decrypt(ladderConfig.getPrivateKey(), message);
        } else {
            return message;
        }
    }

    public void setEncrypt(boolean encrypt) {
        isEncrypt = encrypt;
    }
}
