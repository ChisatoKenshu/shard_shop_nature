package jp.co.sss.shop.controller.review;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.Review;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.ReviewRepository;
import jp.co.sss.shop.repository.UserRepository;

/**
 * レビュー管理 一覧表示機能(運用管理者用)のコントローラクラス
 *
 * @author 横田
 */
@Controller
public class ReviewShowAdminController {

	/**
	 * レビュー情報
	 */
	@Autowired
	ReviewRepository reviewRepository;

	/**
	 * 商品情報
	 */
	@Autowired
	ItemRepository itemRepository;

	/**
	 * ユーザ情報
	 */
	@Autowired
	UserRepository userRepository;

	/**
	 * レビュー情報一覧表示処理
	 *
	 * @param id      レビューID
	 * @param model   Viewとの値受渡し
	 * @return "/review/list/review_list_admin" レビュー情報 一覧画面へ
	 */
	@RequestMapping(path = "/review/list/admin/{id}")
	public String showReviewList(@PathVariable int id, Model model) {

		// 商品IDに該当する商品情報を取得
		Item item = itemRepository.getById(id);

		// 商品IDに該当するレビュー情報を取得
		List<Review> reviewList = reviewRepository.findByReviewWhereItemOrderByInsertDateDescQuery(id);

		// 商品情報、レビュー情報をViewへ渡す
		model.addAttribute("item", item);
		model.addAttribute("reviews", reviewList);

		return "review/list/review_list_admin";
	}
	
	/**
	 * レビュー情報表示切替処理
	 *
	 * @param id      商品ID
	 * @param reviewId   レビューID
	 * @return "/review/list/review_list_admin" レビュー情報 一覧画面へ
	 */
	@RequestMapping(path = "/review/permission_switching/admin/{id}", method = RequestMethod.POST)
	public String permissionSwitching(@PathVariable int id, Integer reviewId) {

		// レビュー情報を取得
		Review review = reviewRepository.getById(reviewId);
		
		// 表示許可フラグを切り替え
		if (review.getPermissionFlag() == 0) {
			review.setPermissionFlag(1);
		} else {
			review.setPermissionFlag(0);
		}
		
		// レビュー情報を上書き
		reviewRepository.save(review);

		return "redirect:/review/list/admin/" + id;
	}

}
