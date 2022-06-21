package jp.co.sss.shop.controller.review;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

	@RequestMapping(path = "/review/regist/input/{id}")
	public String registInput(@PathVariable int id, Model model) {

		// 商品IDに該当する商品情報を取得
		Item item = itemRepository.getById(id);
		model.addAttribute("item", item);

		if (!model.containsAttribute("reviewForm")) {
			model.addAttribute("reviewForm", new ReviewForm());
		}

		// ラジオボタンのMapを作成しModelでViewに渡す
		Map<Integer, String> radioEvaluation = new LinkedHashMap<>();
		radioEvaluation.put(1, "☆1");
		radioEvaluation.put(2, "☆2");
		radioEvaluation.put(3, "☆3");
		radioEvaluation.put(4, "☆4");
		radioEvaluation.put(5, "☆5");
		model.addAttribute("evaluations", radioEvaluation);

		return "review/regist/review_regist_input";
	}

	@RequestMapping(path = "/item/regist/input/{id}", method = RequestMethod.POST)
	public String registInputBack(@PathVariable int id, Model model, ReviewForm reviewForm) {
		
		// 商品IDに該当する商品情報を取得
		Item item = itemRepository.getById(id);
		model.addAttribute("item", item);

		// ラジオボタンのMapを作成しModelでViewに渡す
		Map<Integer, String> radioEvaluation = new LinkedHashMap<>();
		radioEvaluation.put(1, "☆1");
		radioEvaluation.put(2, "☆2");
		radioEvaluation.put(3, "☆3");
		radioEvaluation.put(4, "☆4");
		radioEvaluation.put(5, "☆5");
		model.addAttribute("evaluations", radioEvaluation);

		return "item/regist/item_regist_input";
	}

	@RequestMapping(path = "/review/regist/check/{id}")
	public String registCheck(@PathVariable int id, Model model,
			@Valid @ModelAttribute ReviewForm reviewForm, BindingResult result) {
		
		if (result.hasErrors()) {
			return "item/regist/item_regist_input";
		}

		// 商品IDに該当する商品情報を取得
		Item item = itemRepository.getById(id);
		model.addAttribute("item", item);

		return "review/regist/review_regist_check";
	}

}
