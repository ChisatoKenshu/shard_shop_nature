package jp.co.sss.shop.controller.item;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
		//売れ筋ソートで商品ID検索
		List<Integer> itemIdSort = orderItemRepository.findIdSUMDescWithQuery();
		//削除フラグで商品ID検索
		List<Integer> itemIdStock = itemRepository.findIdWithQuery();
		//商品情報を格納するリストを宣言
		List<Item> item = new ArrayList<>();
		//商品情報が入った回数をカウントする変数
		int cnt = 0;
		//売れ筋ソートしたID順に削除フラグが立っていない商品をItemリストに格納
		for (Integer idSort : itemIdSort) {
			for (Integer idStock : itemIdStock) {
				if (idSort == idStock) {
					item.add(itemRepository.getById(idSort));
					cnt ++;
					break;
				}
			}
			//4件商品がリストに格納されたら終了
			if (cnt == 4) {
				break;
			}
		}
		//モデルにItemリストを渡す
		model.addAttribute("items", item);
		
		Integer id;
		Integer CId;
		List<Integer> itemId = orderItemRepository.findIdWithQuery();
		for (Integer i = 0; i<itemId.size();i++) {
			id = itemId.get(i);
			List<Integer> CategoryId = itemRepository.findCategoryIdById(id);
			
		}
		
		return "index";
	}
	
	@RequestMapping(path = "/item/list/{sortType}")
	public String showItemList(@PathVariable int sortType, Model model) {
		//ソートタイプが1なら新着順でモデルに渡す
		if (sortType == 1) {
			model.addAttribute("items", itemRepository.findByDeleteFlagOrderByInsertDateDescIdAsc(0));
		}
		//ソートタイプが2なら売れ筋順でモデルに渡す
		else {
			List<Integer> itemIdSort = orderItemRepository.findIdSUMDescWithQuery();
			List<Integer> itemIdStock = itemRepository.findIdWithQuery();
			List<Item> item = new ArrayList<>();
			for (Integer idSort : itemIdSort) {
				for (Integer idStock : itemIdStock) {
					if (idSort == idStock) {
						item.add(itemRepository.getById(idSort));
						break;
					}
				}
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

	@RequestMapping(path = "/item/list/category/{sortType}")
	public String showItemListCategory(@PathVariable int sortType, Integer categoryId, Model model) {
		//ソートした商品IDを格納するリストを宣言
		List<Integer> itemIdSort = new ArrayList<>();
		//ソートタイプが1なら新着順で商品IDを格納
		if (sortType == 1) {
			itemIdSort = itemRepository.findIdOrderByInsertDateDescWithQuery();
		}
		//ソートタイプが2なら売れ筋順で商品IDを格納
		else {
			itemIdSort = orderItemRepository.findIdSUMDescWithQuery();
			model.addAttribute("sortType", "2");
		}
		//カテゴリと削除フラグで商品IDを検索しリストに格納
		List<Integer> itemIdCategory = itemRepository.findIdByCategoryWithQuery(categoryId);
		//商品情報を格納するリストを宣言
		List<Item> item = new ArrayList<>();
		//ソートしたID順に指定カテゴリかつ削除フラグが立っていない商品をItemリストに格納
		for (Integer idSort : itemIdSort) {
			for (Integer idCategory : itemIdCategory) {
				if (idSort == idCategory) {
					item.add(itemRepository.getById(idSort));
					break;
				}
			}
		}
		model.addAttribute("items", item);
		model.addAttribute("categoryId", categoryId);
		
		return "item/list/item_list";
	}

}
