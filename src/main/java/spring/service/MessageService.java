package spring.service;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.config.ServerConfig;
import spring.service.session.SessionManager;
import tools.aes.AesException;
import tools.aes.WXBizMsgCrypt;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private SessionManager sessionManager;

    public String handleMessage(HttpServletRequest httpServletRequest, String body) {
        WXBizMsgCrypt wxBizMsgCrypt;
        try {
            wxBizMsgCrypt = serverConfig.getWxBizMsgCrypt();
        } catch (AesException e) {
            e.printStackTrace();
            return "init wx crypt error";
        }

        try {
            Element requestRootElement = decode(body, wxBizMsgCrypt, httpServletRequest);
            String from = requestRootElement.element("FromUserName").getText();
            String to = requestRootElement.element("ToUserName").getText();
            String messageType = requestRootElement.element("MsgType").getText();
            String message = requestRootElement.element("Content").getText();
            logger.debug("from: " + from + " to: " + to + " message: " + message + " messageType: " + messageType);

            sessionManager.createSession(from);

            sessionManager.getSessionMessage(from).addMessage("/testCommand#{}".getBytes());

            byte[] bytes;
            while (true) {
                bytes = sessionManager.getSessionMessage(from).getMessages();
                if (bytes != null) {
                    break;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Document reply = buildReply(from, messageType, new String(bytes));
            return encrypt(reply.asXML(), wxBizMsgCrypt, new Date(), random.nextInt());
        } catch (DocumentException | AesException | IOException e) {
            e.printStackTrace();
            return "solve message error";
        }
    }

    private Document buildReply(String from, String messageType, String s) {
        Document reply = DocumentHelper.createDocument();
        Element replyRootElement = reply.addElement("xml");
        replyRootElement.addElement("ToUserName").setText(from);
        replyRootElement.addElement("FromUserName").setText(serverConfig.getOriginalId());
        replyRootElement.addElement("CreateTime").setText(new Date().getTime() + "");
        replyRootElement.addElement("MsgType").setText(messageType);
        replyRootElement.addElement("Content").setText(s);
        logger.debug("reply: " + reply.asXML());
        return reply;
    }

    private Element decode(String body, WXBizMsgCrypt wxBizMsgCrypt, HttpServletRequest httpServletRequest) throws AesException, DocumentException {
        String msgSignature = httpServletRequest.getParameter("msg_signature");
        String timeStamp = httpServletRequest.getParameter("timestamp");
        String nonce = httpServletRequest.getParameter("nonce");

        return DocumentHelper.parseText(wxBizMsgCrypt.decryptMsg(msgSignature, timeStamp, nonce, body)).getRootElement();
    }

    private String encrypt(String message, WXBizMsgCrypt wxBizMsgCrypt, Date timeStamp, int nonce) throws AesException {
        return wxBizMsgCrypt.encryptMsg(message, timeStamp.getTime() + "", nonce + "");
    }

}
