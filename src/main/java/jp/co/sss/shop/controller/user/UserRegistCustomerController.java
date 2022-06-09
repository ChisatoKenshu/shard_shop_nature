package jp.co.sss.shop.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserRegistCustomerController {
	@RequestMapping(path="/user/regist/input",method = RequestMethod.GET)
	public String registInput(Model model) {
		return "user/regist/user_regist_input";
	}
}
