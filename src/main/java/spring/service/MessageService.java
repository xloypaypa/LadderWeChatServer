package spring.service;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by xsu on 16/10/9.
 * it's the message service
 */
@Service
public class MessageService {

    private final static Logger logger = Logger.getLogger(TokenService.class);

    public String handleMessage(String body) {
        try {
            Document request = DocumentHelper.parseText(body);
            Element requestRootElement = request.getRootElement();
            String from = requestRootElement.element("FromUserName").getText();
            String to = requestRootElement.element("ToUserName").getText();
            String messageType = requestRootElement.element("MsgType").getText();
            String message = requestRootElement.element("Content").getText();
            logger.debug("from: " + from + " to: " + to + " message: " + message);

            Document reply = DocumentHelper.createDocument();
            Element replyRootElement = reply.addElement("xml");
            replyRootElement.addElement("ToUserName").setText(from);
            replyRootElement.addElement("FromUserName").setText(to);
            replyRootElement.addElement("CreateTime").setText(new Date().getTime() + "");
            replyRootElement.addElement("MsgType").setText(messageType);
            replyRootElement.addElement("Content").setText("I have received your message");
            logger.debug("reply: " + reply.asXML());
            return reply.asXML();
        } catch (DocumentException e) {
            logger.error("request body not xml.");
            e.printStackTrace();
            return "request body not xml.";
        }
    }

}
