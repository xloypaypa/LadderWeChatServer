package spring.service;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.springframework.stereotype.Service;

/**
 * Created by xsu on 16/10/9.
 * it's the message service
 */
@Service
public class MessageService {

    private final static Logger logger = Logger.getLogger(TokenService.class);

    public String handleMessage(String body) {
        try {
            Document document = DocumentHelper.parseText(body);
            String from = document.getRootElement().element("FromUserName").getText();
            String to = document.getRootElement().element("ToUserName").getText();
            String message = document.getRootElement().element("Content").getText();
            logger.debug("from: " + from + " to: " + to + " message: " + message);
            return "I have received your message.";
        } catch (DocumentException e) {
            logger.error("request body not xml.");
            e.printStackTrace();
            return "error";
        }
    }

}
