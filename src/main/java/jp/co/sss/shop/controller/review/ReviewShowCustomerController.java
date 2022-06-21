package jp.co.sss.shop.controller.review;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.Review;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.ReviewRepository;

/**
 * レビュー管理 一覧表示機能(運用管理者用)のコントローラクラス
 *
 * @author 横田
 */
@Controller
public class ReviewShowCustomerController {

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
	 * レビュー情報詳細表示処理
	 *
	 * @param id      レビューID
	 * @param model   Viewとの値受渡し
	 * @param session セッション情報
	 * @return "/review/list/review_list" レビュー情報 一覧画面へ
	 */
	@RequestMapping(path = "/review/list/{id}")
	public String showReviewList(@PathVariable int id, Model model, HttpSession session) {
		
		// セッションからユーザIDを取得
		Integer userId = ((UserBean)session.getAttribute("user")).getId();

		// 商品IDに該当する商品情報を取得
		Item item = itemRepository.getById(id);

		// 商品IDに該当する表示許可設定のレビュー情報を取得
		List<Review> reviewList = reviewRepository.findByReviewWhereItemANDPermissionFlagOrderByInsertDateDescQuery(id);

		// ユーザID、商品情報、レビュー情報をViewへ渡す
		model.addAttribute("userId", userId);
		model.addAttribute("item", item);
		model.addAttribute("reviews", reviewList);

		return "review/list/review_list";
	}
	
	

}