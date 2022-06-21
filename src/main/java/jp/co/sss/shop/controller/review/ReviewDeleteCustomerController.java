package jp.co.sss.shop.controller.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.entity.Review;
import jp.co.sss.shop.repository.ReviewRepository;

/**
 * レビュー管理 削除機能(一般会員用)のコントローラクラス
 *
 * @author 横田
 */
@Controller
public class ReviewDeleteCustomerController {
	
	/**
	 * レビュー情報
	 */
	@Autowired
	ReviewRepository reviewRepository;
	
	/**
	 * 商品情報削除確認処理
	 *
	 * @param id 商品ID
	 * @param model Viewとの値受渡し
	 * @return "item/delete/item_delete_check" 商品情報 削除確認画面へ
	 */
	@RequestMapping(path = "/review/delete/check", method = RequestMethod.POST)
	public String deleteCheck(Integer id, Model model) {
	
		// 削除対象の商品情報を取得
		Review review = reviewRepository.getById(id);

		// 商品情報をViewに渡す
		model.addAttribute("review", review);

		return "review/delete/review_delete_check";
	}

}
