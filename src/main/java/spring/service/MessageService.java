package spring.service;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.config.ServerConfig;
import tools.aes.AesException;
import tools.aes.WXBizMsgCrypt;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Random;

/**
 * Created by xsu on 16/10/9.
 * it's the message service
 */
@Service
public class MessageService {

    private final static Logger logger = Logger.getLogger(TokenService.class);
    private final static Random random = new Random();
    private final static String format = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><Encrypt><![CDATA[%1$s]]></Encrypt></xml>";

    @Autowired
    private ServerConfig serverConfig;

    public String handleMessage(HttpServletRequest httpServletRequest, String body) {
        System.out.println("body: " + body);

        WXBizMsgCrypt wxBizMsgCrypt;
        try {
            wxBizMsgCrypt = new WXBizMsgCrypt(serverConfig.getToken(), serverConfig.getEncodingAESKey(), serverConfig.getAppId());
        } catch (AesException e) {
            e.printStackTrace();
            return "init wx crypt error";
        }

        try {
            Document request = DocumentHelper.parseText(body);
            Element requestRootElement = decode(request.getRootElement(), wxBizMsgCrypt, httpServletRequest);
            String from = requestRootElement.element("FromUserName").getText();
            String to = requestRootElement.element("ToUserName").getText();
            String messageType = requestRootElement.element("MsgType").getText();
            String message = requestRootElement.element("Content").getText();
            logger.debug("from: " + from + " to: " + to + " message: " + message);

            Document reply = buildReply(to, messageType);
            return encrypt(reply.asXML(), wxBizMsgCrypt, new Date(), random.nextInt());
        } catch (DocumentException | AesException e) {
            e.printStackTrace();
            return "solve message error";
        }
    }

    private Document buildReply(String to, String messageType) {
        Document reply = DocumentHelper.createDocument();
        Element replyRootElement = reply.addElement("xml");
        replyRootElement.addElement("ToUserName").setText(serverConfig.getOriginalId());
        replyRootElement.addElement("FromUserName").setText(to);
        replyRootElement.addElement("CreateTime").setText(new Date().getTime() + "");
        replyRootElement.addElement("MsgType").setText(messageType);
        replyRootElement.addElement("Content").setText("I have received your message");
        logger.debug("reply: " + reply.asXML());
        return reply;
    }

    private Element decode(Element message, WXBizMsgCrypt wxBizMsgCrypt, HttpServletRequest httpServletRequest) throws AesException, DocumentException {
        String msgSignature = httpServletRequest.getParameter("signature");
        String timeStamp = httpServletRequest.getParameter("timestamp");
        String nonce = httpServletRequest.getParameter("nonce");
        String encrypt = message.elementText("Encrypt");
        String fromXML = String.format(format, encrypt);

        if (msgSignature == null) {
            System.out.println("msgSignature");
        }
        if (timeStamp == null) {
            System.out.println("timeStamp");
        }
        if (nonce == null) {
            System.out.println("nonce");
        }
        if (encrypt == null) {
            System.out.println("encrypt");
        }

        return DocumentHelper.parseText(wxBizMsgCrypt.decryptMsg(msgSignature, timeStamp, nonce, fromXML)).getRootElement();
    }

    private String encrypt(String message, WXBizMsgCrypt wxBizMsgCrypt, Date timeStamp, int nonce) throws AesException {
        return wxBizMsgCrypt.encryptMsg(message, timeStamp.getTime() + "", nonce + "");
    }

}
