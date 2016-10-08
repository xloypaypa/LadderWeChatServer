package spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import spring.service.TokenService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by xsu on 16/10/8.
 * it's the token controller
 */
@Controller
@RequestMapping(method = RequestMethod.GET)
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @ResponseBody
    @RequestMapping(path = "/weChat")
    public String token(HttpServletRequest request) throws IOException {
        if (tokenService.validateToken(request)) {
            return request.getParameter("echostr");
        } else {
            return "error";
        }
    }

}
