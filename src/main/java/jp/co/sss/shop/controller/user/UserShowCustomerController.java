package jp.co.sss.shop.controller.user;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.UserRepository;

@Controller
public class UserShowCustomerController {
	@Autowired
	UserRepository userRepository;
	
	@RequestMapping(path="/user/detail")
	public String showUser( Model model, @ModelAttribute UserForm form, HttpSession session) {
		

		
		UserBean user = (UserBean) session.getAttribute("user");
		
		
		//UserBean userBean = new UserBean();

		// Userエンティティの各フィールドの値をUserBeanにコピー
		//BeanUtils.copyProperties(users, userBean);

		// 会員情報をViewに渡す
		model.addAttribute("user", user);

		return "user/detail/user_detail";
	}
}
