package jp.co.sss.shop.controller.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.form.ReviewForm;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.ReviewRepository;

@Controller
public class ReviewRegistCustomerController {
	
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
	
	@RequestMapping(path = "/review/regist/{id}")
	public String showReviewList(@PathVariable int id, Model model) {
		
		// 商品IDに該当する商品情報を取得
		Item item = itemRepository.getById(id);
		model.addAttribute("item", item);
		
		if (!model.containsAttribute("reviewForm")) {
			model.addAttribute("reviewForm", new ReviewForm());
		}
		
		return "review/regist/review_regist_input";
	}

}
