package jp.co.sss.shop.controller.user;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.UserRepository;
import jp.co.sss.shop.util.Constant;

@Controller
public class UserDeleteController {

	/**
	 * 会員管理 削除機能(一般会員)のコントローラークラス
	 * 
	 * 
	 */
	@Autowired
	UserRepository userRepository;
	
	/**
	 * 会員情報削除確認処理
	 * 
	 * @Param model Viewとの値受渡し
	 * @Param form 会員情報フォーム
	 * @return "user/delete/user_delete_cheek" 会員情報 削除確認画面へ
	 */
	
	@RequestMapping(path="/user/delete/check",method=RequestMethod.POST)
	public String deleteCheck(Model model,@ModelAttribute UserForm form) {
		
		//削除対象の会員情報を取得
		User user = userRepository.getById(form.getId());
		
		UserBean userBean = new UserBean();
		
		//Userンティティの各フィールドの値をUserbeanにコピー
		BeanUtils.copyProperties(user,userBean);
		
		model.addAttribute("user",userBean);
		
		return "user/delete/user_delete_check";
	}
	/**
	 * 会員情報削除完了処理
	 *
	 * @param form 会員情報フォーム
	 * @return "user/delete/user_delete_complete_admin" 会員情報 削除完了画面へ
	 */
	@RequestMapping(path="/user/delete/complete",method=RequestMethod.POST)
	public String deleteComplete(@ModelAttribute UserForm form,HttpSession session) {
		
		//削除対象の会員情報を取得
		User user = userRepository.getById(form.getId());
		
		//削除フラグを立てる
		user.setDeleteFlag(Constant.DELETED);
		
		
		
		//会員情報を保存
		userRepository.save(user);
		
		return "redirect:/user/delete/complete";
	}
	/**
	 * 会員情報削除完了処理
	 *
	 * @param form 会員情報フォーム
	 * @return "user/delete/user_delete_complete_admin" 会員情報 削除完了画面へ
	 */
	@RequestMapping(path = "/user/delete/complete", method = RequestMethod.GET)
	public String deleteCompleteRedirect(HttpSession session) {
		session.invalidate();
		return "user/delete/user_delete_complete";
	}
	/**
	 * 
	 *
	 * @param model Viewとの値受渡し
	 * @param form  会員情報フォーム
	 * @return "/user/detail/{id}" 会員情報 詳細画面へ
	 */
	@RequestMapping(path = "/user/delete/check", method = RequestMethod.GET)
	public String deleteBack(Model model, @ModelAttribute UserForm form) {

		// 削除対象の会員情報を取得
		User user = userRepository.getById(form.getId());

		UserBean userBean = new UserBean();

		// Userエンティティの各フィールドの値をUserBeanにコピー
		BeanUtils.copyProperties(user, userBean);

		// 会員情報をViewに渡す
		model.addAttribute("user", userBean);

		return "user/detail/{id}";
	}

}
