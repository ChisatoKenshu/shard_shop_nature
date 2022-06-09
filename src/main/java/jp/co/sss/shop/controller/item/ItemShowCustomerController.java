package jp.co.sss.shop.controller.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.ItemRepository;

/**
 * 商品管理 一覧表示機能(一般会員用)のコントローラクラス
 *
 * @author SystemShared
 */
@Controller
public class ItemShowCustomerController {
	/**
	 * 商品情報
	 */
	@Autowired
	ItemRepository itemRepository;

	
	/**
	 * トップ画面 表示処理
	 *
	 * @param model    Viewとの値受渡し
	 * @return "/" トップ画面へ
	 */
	@RequestMapping(path = "/")
	public String index(Model model) {
		
		return "index";
	}
	
	@RequestMapping(path = "/item/list/findAll")
	public String itemListFindAll(Model model) {
		model.addAttribute("items", itemRepository.findAll());
		return "item/list/item_list";
	}
	
	@RequestMapping(path = "/item/detail/{id}")
	public String itemDetail(@PathVariable int id, Model model) {
		model.addAttribute("item", itemRepository.getById(id));
		
		return "item/detail/item_detail";
	}
	
	@RequestMapping(path = "/item/list/category/1")
	public String item(Integer categoryId, Model model) {
		Category category = new Category();
		category.setId(categoryId);
		List<Item> item = itemRepository.findByCategory(category);
		model.addAttribute("items", item);
		
		return "item/list/item_list";
	}

	
}
