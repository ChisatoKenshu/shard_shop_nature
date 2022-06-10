package jp.co.sss.shop.controller.item;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderItemRepository;

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
	 * 商品情報
	 */
	@Autowired
	OrderItemRepository orderItemRepository;

	
	/**
	 * トップ画面 表示処理
	 *
	 * @param model    Viewとの値受渡し
	 * @return "/" トップ画面へ
	 */
	@RequestMapping(path = "/")
	public String index(Model model) {
		List<Integer> itemId = orderItemRepository.findIdSUMDescWithQuery();
		List<Item> item = new ArrayList<>();
		for (Integer id : itemId) {
			item.add(itemRepository.getById(id));
		}
		model.addAttribute("items", item);
		return "index";
	}
	
	@RequestMapping(path = "/item/list/{sortType}")
	public String showItemList(@PathVariable int sortType, Model model) {
		if (sortType == 1) {
			model.addAttribute("items", itemRepository.findByDeleteFlagOrderByInsertDateDescIdAsc(0));
		} else {
			List<Integer> itemId = orderItemRepository.findIdSUMDescWithQuery();
			List<Item> item = new ArrayList<>();
			for (Integer id : itemId) {
				item.add(itemRepository.getById(id));
			}
			model.addAttribute("items", item);
			model.addAttribute("sortType", "2");
		}
		
		return "item/list/item_list";
	}
	
	@RequestMapping(path = "/item/detail/{id}")
	public String showItem(@PathVariable int id, Model model) {
		model.addAttribute("item", itemRepository.getById(id));
		
		return "item/detail/item_detail";
	}
	
	@RequestMapping(path = "/item/list/category/1")
	public String showItemCategory(Integer categoryId, Model model) {
		Category category = new Category();
		category.setId(categoryId);
		List<Item> item = itemRepository.findByDeleteFlagAndCategory(0, category);
		model.addAttribute("items", item);
		
		return "item/list/item_list";
	}

	
}
