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

@Controller
public class UserUpdateController {
	/**
	 * 会員情報
	 */
	@Autowired
	UserRepository userRepository;

	/**
	 * セッション
	 */
	@Autowired
	HttpSession session;

	/**
	 * 会員情報の変更入力画面表示処理
	 *
	 * @param model Viewとの値受渡し
	 * @param form  会員情報フォーム
	 * @return "user/update/user_update_input_admin" 会員情報 変更入力画面へ
	 **/
	@RequestMapping(path="/user/update/input",method=RequestMethod.POST)
	public String updateInput(boolean backFlg,Model model,@ModelAttribute UserForm form) {
		
		// 戻るボタンかどうかを判定
				if (!backFlg) {

					// 変更対象の会員情報を取得
					User user = userRepository.getById(form.getId());
					UserBean userBean = new UserBean();

					// Userエンティティの各フィールドの値をUserBeanにコピー
					BeanUtils.copyProperties(user, userBean);

					// 会員情報をViewに渡す
					model.addAttribute("user", userBean);

				} else {

					UserBean userBean = new UserBean();
					// 入力値を会員情報にコピー
					BeanUtils.copyProperties(form, userBean);

					// 会員情報をViewに渡す
					model.addAttribute("user", userBean);

				}
				return "user/update/user_update_input_admin";
			}
}
