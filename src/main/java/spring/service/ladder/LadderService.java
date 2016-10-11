package spring.service.ladder;

import net.server.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.config.LadderConfig;

import java.io.IOException;

/**
 * Created by xlo on 16/3/3.
 * it's the ladder service
 */
@Service
public class LadderService {

    private LadderConfig ladderConfig;

    @Autowired
    public LadderService(LadderConfig ladderConfig) {
        this.ladderConfig = ladderConfig;
        assert this.ladderConfig != null;
        Client.getInstance("weChat", this.ladderConfig.getMaxConnectionNumber());
    }

    public void connect(LadderServerSolver connectionSolver) throws IOException {
        Client.getNewClient("weChat").connect(ladderConfig.getIp(), ladderConfig.getPort(), connectionSolver);
    }

}
