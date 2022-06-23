package jp.co.sss.shop.controller.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.entity.Review;
import jp.co.sss.shop.repository.ReviewRepository;

/**
 * レビュー管理 削除機能(運用管理者用)のコントローラクラス
 *
 * @author 横田
 */
@Controller
public class ReviewDeleteAdminController {
	
	/**
	 * レビュー情報
	 */
	@Autowired
	ReviewRepository reviewRepository;
	
	/**
	 * レビュー情報削除確認処理
	 *
	 * @param id レビューID
	 * @param model Viewとの値受渡し
	 * @return "review/delete/review_delete_check_admin" レビュー情報 削除確認画面へ
	 */
	@RequestMapping(path = "/review/delete/check/admin", method = RequestMethod.POST)
	public String deleteCheck(Integer id, Model model) {
	
		// 削除対象のレビュー情報を取得
		Review review = reviewRepository.getById(id);

		// レビュー情報をViewに渡す
		model.addAttribute("review", review);

		return "review/delete/review_delete_check_admin";
	}
	
	/**
	 * レビュー情報削除完了処理
	 *
	 * @param id レビューID
	 * @return "redirect:/review/delete/complete/admin" レビュー情報 削除完了画面へ
	 */
	@RequestMapping(path = "/review/delete/complete/admin", method = RequestMethod.POST)
	public String deleteComplete(Integer id) {
		
		// 削除対象のレビュー情報から商品IDを取得しリダイレクト先に受け渡す
		Review review = reviewRepository.getById(id);
		Integer itemId = review.getItem().getId();

		// 削除対象のレビュー情報を物理削除
		reviewRepository.deleteById(id);

		return "redirect:/review/delete/complete/admin/" + itemId;
	}
	
	/**
	 * レビュー情報削除完了画面表示
	 * 
	 * @return "review/delete/review_delete_complete_admin"  レビュー情報 削除完了画面へ
	 */
	@RequestMapping(path = "/review/delete/complete/admin/{itemId}", method = RequestMethod.GET)
	public String deleteCompleteRedirect(Model model, @PathVariable int itemId) {
		// レビュー情報をViewに渡す
		model.addAttribute("itemId", itemId);

		return "review/delete/review_delete_complete_admin";
	}

}