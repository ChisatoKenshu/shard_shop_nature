package jp.co.sss.shop.controller.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Favorite;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.FavoriteRepository;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderItemRepository;
import jp.co.sss.shop.repository.OrderRepository;
import jp.co.sss.shop.repository.ReviewRepository;

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

	@Autowired
	FavoriteRepository favoriteRepository;
	
	@Autowired
	ReviewRepository reviewRepository;
	
	/**
	 * 注文情報
	 */
	@Autowired
	OrderRepository orderRepository;

	/**
	 * トップ画面 表示処理
	 *
	 * @param model Viewとの値受渡し
	 * @return "/" トップ画面へ
	 */
	@RequestMapping(path = "/")
	public String index(Model model,HttpServletRequest request,HttpSession session) {
		// 売れ筋ソートで商品ID検索
	List<Integer> itemIdSort = orderItemRepository.findIdSUMDescWithQuery();
		// 削除フラグで商品ID検索
	List<Integer> itemIdDelete = itemRepository.findIdWithQuery();
		// 商品情報を格納するリストを宣言
		List<Item> item = new ArrayList<>();
		// 商品情報が入った回数をカウントする変数
		int cnt = 0;
		// 売れ筋ソートしたID順に削除フラグが立っていない商品をItemリストに格納
		for (Integer idSort : itemIdSort) {
			for (Integer idDelete : itemIdDelete) {
				if (idSort == idDelete) {
					item.add(itemRepository.getById(idSort));
					cnt++;
					break;
				}
			}
			// 4件商品がリストに格納されたら終了
			if (cnt == 4) {
				break;
			}
		}
		// モデルにItemリストを渡す
		model.addAttribute("items", item);
		
		
		
		List<Item> items = new ArrayList<>();
		
		// 参照先テーブルに対応付けられたエンティティ Category のオブジェクトを生成
		Category category = new Category();
		
		session = request.getSession(false);
		if(session.getAttribute("user")!=null) {
			UserBean user = (UserBean) session.getAttribute("user");
			Integer loginUserId = user.getId();
			
			List<Integer> itemId = new ArrayList<>();
			List<Integer> OId = orderRepository.findIdByUserId(loginUserId);
			for(int oId : OId) {
				// 購入された商品の商品Idを検索
				List<Integer> IId = orderItemRepository.findItemIdWithQuery(oId);
				for(int iId : IId) {
					itemId.add(iId);
				}
			}
			//itemIdリストの重複を排除したItemIdリストを作成
			List<Integer> PureItemId = new ArrayList<Integer>(new HashSet<>(itemId));
			
			//カテゴリーIdを格納するリストを作成
			List<Integer> CategoryId = new ArrayList<>();
			
 			//ItemIdリストから1ずつ取り出す
			for(int pureItemId : PureItemId) {
				CategoryId.add(itemRepository.findCategoryIdById(pureItemId));
			}
			
			//CategoryIdリストの重複を排除したPureCategoryリストを作成
			List<Integer> PureCategoryId = new ArrayList<Integer>(new HashSet<>(CategoryId));
			
			//商品Idを格納するリストを作成
			List<Integer> itemId2 = new ArrayList<>();
			for(int pureCategoryId : PureCategoryId) {
				List<Integer> IId= itemRepository.findIdByCategoryIdWithQuery(pureCategoryId);
				for(int iId : IId) {
					itemId2.add(iId);
				}
			}
			Collections.shuffle(itemId2);
			//購入したことのある商品の商品Idを排除したものを格納するリストを作成
			List<Integer> itemId3 = new ArrayList<>();
			int cnt2 = 0;
			//itemId3にitemId2をコピー
			for(int i2 : itemId2) {
				itemId3.add(i2);
			}
			
			for(int id2 : itemId2) {
				for(int id : PureItemId) {
					
					if(id2==id) {
						itemId3.remove(cnt2);
						cnt2--;
						break;
					}
					
				}
				cnt2++;
			}
			//購入したことのない商品情報を格納するリストを作成
			List<Item> Item = new ArrayList<>();
			int cnt3 = 0;
			for(int item3 : itemId3) {
			  Item.add(itemRepository.getById(item3));
			  cnt3++;
			  if(cnt3==3) {
				  break;
			  }
			}
			
		model.addAttribute("OItems",Item);
		}
		
		
		
		return "index";
	}

	@RequestMapping(path = "/item/list/{sortType}")
	public String showItemList(@PathVariable int sortType, Model model, HttpSession session) {
		// ソートタイプが1なら新着順でモデルに渡す
		if (sortType == 1) {
			//ログインしているかどうか確認
			if(session.getAttribute("user") != null) {
				Integer userId = ((UserBean) session.getAttribute("user")).getId();
				List<Item> items = itemRepository.findByDeleteFlagOrderByInsertDateDescIdAsc(0);
				List<Favorite> favorites = favoriteRepository.findByUserIdOrderByItemId(userId);
				List<Favorite> emptyList = new ArrayList<Favorite>();
				for(Item item : items) {
					Favorite emptyFav = new Favorite();
					emptyFav.setIsFav(0);
					emptyFav.setItemId(item.getId());
					emptyList.add(emptyFav);
				}
				
				int cnt = 0;
				for(Favorite fav : emptyList) {
					for(Favorite fav2 : favorites) {
						try {
							if(fav2.getItemId() == fav.getItemId()) {
								emptyList.set(cnt, fav2);
								break;
							}else {
								Favorite emptyFav = new Favorite();
								emptyFav.setItemId(fav.getItemId());
								emptyFav.setIsFav(fav.getIsFav());
								emptyList.set(cnt, emptyFav);
							}
						}catch(IndexOutOfBoundsException e){
						}
					}
					cnt++;
				}
				model.addAttribute("favorites", emptyList);
			}
			
			model.addAttribute("items", itemRepository.findByDeleteFlagOrderByInsertDateDescIdAsc(0));
		}
		// ソートタイプが2なら売れ筋順でモデルに渡す
		else {
			List<Integer> itemIdSort = orderItemRepository.findIdSUMDescWithQuery();
			List<Integer> itemIdDelete = itemRepository.findIdWithQuery();
			List<Item> item = new ArrayList<>();
			for (Integer idSort : itemIdSort) {
				for (Integer idDelete : itemIdDelete) {
					if (idSort == idDelete) {
						item.add(itemRepository.getById(idSort));
						break;
					}
				}
			}
			
			//ログインしているかどうか確認
			if(session.getAttribute("user") != null) {
				Integer userId = ((UserBean) session.getAttribute("user")).getId();
				List<Favorite> favorites = favoriteRepository.findByUserIdOrderByItemId(userId);
				List<Favorite> emptyList = new ArrayList<Favorite>();
				for(Item eitem : item) {
					Favorite emptyFav = new Favorite();
					emptyFav.setIsFav(0);
					emptyFav.setItemId(eitem.getId());
					emptyList.add(emptyFav);
				}
				
				int cnt = 0;
				for(Favorite fav : emptyList) {
					for(Favorite fav2 : favorites) {
						try {
							if(fav2.getItemId() == fav.getItemId()) {
								emptyList.set(cnt, fav2);
								break;
							}else {
								Favorite emptyFav = new Favorite();
								emptyFav.setItemId(fav.getItemId());
								emptyFav.setIsFav(fav.getIsFav());
								emptyList.set(cnt, emptyFav);
							}
						}catch(IndexOutOfBoundsException e){
						}
					}
					cnt++;
				}
				model.addAttribute("favorites", emptyList);
			}
			model.addAttribute("items", item);
			model.addAttribute("sortType", "2");
		}

		return "item/list/item_list";
	}

	@RequestMapping(path = "/item/detail/{id}")
	public String showItem(@PathVariable int id, Model model, HttpSession session) {
		model.addAttribute("item", itemRepository.getById(id));
		//ログインしているかどうか確認
		if(session.getAttribute("user") != null) {
			Integer userId = ((UserBean) session.getAttribute("user")).getId();
			Favorite favorite = favoriteRepository.findByUserIdAndItemId(userId, id);
			model.addAttribute("favorite", favorite);	
		}
		
		//レビュー平均
		model.addAttribute("reviewAVG", reviewRepository.findAVGByPermissionFlagGroupByItemIdQuery(id));
		
		return "item/detail/item_detail";
	}

	@RequestMapping(path = "/item/list/category/{sortType}")
	public String showItemListCategory(@PathVariable int sortType, Integer categoryId, Model model, HttpSession session) {
		// ソートした商品IDを格納するリストを宣言
		List<Integer> itemIdSort = new ArrayList<>();
		// ソートタイプが1なら新着順で商品IDを格納
		if (sortType == 1) {
			itemIdSort = itemRepository.findIdOrderByInsertDateDescWithQuery();
		}
		// ソートタイプが2なら売れ筋順で商品IDを格納
		else {
			itemIdSort = orderItemRepository.findIdSUMDescWithQuery();
			model.addAttribute("sortType", "2");
		}
		// カテゴリと削除フラグで商品IDを検索しリストに格納
		List<Integer> itemIdCategory = itemRepository.findIdByCategoryWithQuery(categoryId);
		// 商品情報を格納するリストを宣言
		List<Item> item = new ArrayList<>();
		// ソートしたID順に指定カテゴリかつ削除フラグが立っていない商品をItemリストに格納
		for (Integer idSort : itemIdSort) {
			for (Integer idCategory : itemIdCategory) {
				if (idSort == idCategory) {
					item.add(itemRepository.getById(idSort));
					break;
				}
			}
		}
		
		//ログインしているかどうか確認
		if(session.getAttribute("user") != null) {
			Integer userId = ((UserBean) session.getAttribute("user")).getId();
			List<Favorite> favorites = favoriteRepository.findByUserIdOrderByItemId(userId);
			List<Favorite> emptyList = new ArrayList<Favorite>();
			for(Item eItem : item) {
				Favorite emptyFav = new Favorite();
				emptyFav.setIsFav(0);
				emptyFav.setItemId(eItem.getId());
				emptyList.add(emptyFav);
			}
			
			int cnt = 0;
			for(Favorite fav : emptyList) {
				for(Favorite fav2 : favorites) {
					try {
						if(fav2.getItemId() == fav.getItemId()) {
							emptyList.set(cnt, fav2);
							break;
						}else {
							Favorite emptyFav = new Favorite();
							emptyFav.setItemId(fav.getItemId());
							emptyFav.setIsFav(fav.getIsFav());
							emptyList.set(cnt, emptyFav);
						}
					}catch(IndexOutOfBoundsException e){
					}
				}
				cnt++;
			}
			model.addAttribute("favorites", emptyList);
		}
		
		model.addAttribute("items", item);
		model.addAttribute("categoryId", categoryId);

		return "item/list/item_list";
	}

}
