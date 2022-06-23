package jp.co.sss.shop.controller.review;

import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.Review;
import jp.co.sss.shop.form.ReviewForm;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.ReviewRepository;
import jp.co.sss.shop.repository.UserRepository;

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

	@Autowired
	UserRepository userRepository;

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

	@RequestMapping(path = "/review/regist/input/{id}", method = RequestMethod.POST)
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

		return "review/regist/review_regist_input";
	}

	@RequestMapping(path = "/review/regist/check/{id}")
	public String registCheck(@PathVariable int id, Model model, @Valid @ModelAttribute ReviewForm reviewForm,
			BindingResult result) {

		if (result.hasErrors()) {
			Item item = itemRepository.getById(id);
			model.addAttribute("item", item);
			
			Map<Integer, String> radioEvaluation = new LinkedHashMap<>();
			radioEvaluation.put(1, "☆1");
			radioEvaluation.put(2, "☆2");
			radioEvaluation.put(3, "☆3");
			radioEvaluation.put(4, "☆4");
			radioEvaluation.put(5, "☆5");
			model.addAttribute("evaluations", radioEvaluation);
			
			return "review/regist/review_regist_input";
		}

		// 商品IDに該当する商品情報を取得
		Item item = itemRepository.getById(id);
		model.addAttribute("item", item);

		return "review/regist/review_regist_check";
	}

	@RequestMapping(path = "/review/regist/complete")
	public String registComplete(Model model, ReviewForm reviewForm, int itemId, HttpSession session) {

		// Formクラス内の各フィールドの値をエンティティにコピー
		Review review = new Review();

		review.setItem(itemRepository.getById(itemId));

		Integer userId = ((UserBean) session.getAttribute("user")).getId();
		review.setUser(userRepository.getById(userId));

		review.setEvaluation(reviewForm.getEvaluation());
		review.setTitle(reviewForm.getTitle());
		review.setNickname(reviewForm.getNickname());
		review.setReviewText(reviewForm.getReviewText());

		// 現在の日時を登録日時としてレビュー情報に追加
		long nowDate = System.currentTimeMillis();
		Date date = new Date(nowDate);
		review.setInsertDate(date);

		// 商品情報を保存
		reviewRepository.save(review);

		return "redirect:/review/regist/complete/" + itemId;
	}

	@RequestMapping(path = "/review/regist/complete/{itemId}", method = RequestMethod.GET)
	public String registCompleteRedirect(Model model, @PathVariable int itemId) {
		// レビュー情報をViewに渡す
		model.addAttribute("itemId", itemId);

		return "review/regist/review_regist_complete";
	}

}
