package spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import spring.service.MessageService;
import spring.service.TokenService;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Created by xsu on 16/10/8.
 * it's the token controller
 */
@Controller
@RequestMapping(path = "/weChat")
public class WeChatController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private MessageService messageService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public String token(HttpServletRequest request) {
        if (tokenService.validateToken(request)) {
            return request.getParameter("echostr");
        } else {
            return "error";
        }
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public String message(HttpServletRequest httpServletRequest, @RequestBody String body) {
        return messageService.handleMessage(httpServletRequest, body);
    }

}
