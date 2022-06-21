package jp.co.sss.shop.controller.favorite;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.sss.shop.bean.FavBean;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Favorite;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.FavoriteRepository;
import jp.co.sss.shop.repository.ItemRepository;

@Controller
public class FavoriteCustomerController {
	@Autowired
	private FavoriteRepository favoriteRepository;
	@Autowired
	private ItemRepository itemRepository;
	
	@RequestMapping(path="/item/favorite", method=RequestMethod.POST)
	@ResponseBody
	public String addfav(Model model, @RequestParam String json, HttpSession session) {
		//JSON変換用のクラス
	    ObjectMapper mapper = new ObjectMapper();
	    
	    try {
			FavBean favBean =  mapper.readValue(json, FavBean.class);
			UserBean userBean = (UserBean) session.getAttribute("user");
			
			Favorite favorite = new Favorite();
			favorite.setItemId(Integer.parseInt(favBean.getItemId()));
			favorite.setUserId(userBean.getId());
			favorite.setIsFav(Integer.parseInt(favBean.getIsFav()));
			
			favoriteRepository.save(favorite);
		} catch (JsonMappingException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return json;
	}
	
	@RequestMapping("/favorite/list")
	public String showFavorite(Model model, HttpSession session) {
		Integer userId = ((UserBean) session.getAttribute("user")).getId();
		List<Favorite> favoriteList = favoriteRepository.findByUserIdAndIsFav(userId, 1);
		List<Item> itemList = itemRepository.findByDeleteFlag(0);
		List<Favorite> sendFavoriteList = new ArrayList<Favorite>();
		List<Item> sendItemList = new ArrayList<Item>();
		//deleteFlagが0で最大のitemIdを取得
		int maxItemId = itemList.stream().max(Comparator.comparing(Item::getId)).orElseThrow(NoSuchElementException::new).getId();
		
		for(int i = 1; i <= maxItemId; i++) {
			Item empItem = new Item();
			empItem.setId(i);
			empItem.setName("no item");
			empItem.setDeleteFlag(1);
			sendItemList.add(empItem);
			for(Item item : itemList) {
				if(item.getId() == i) {
					sendItemList.set(i -1, item);
					break;
				}
			}
		}
		
		for(Favorite fav : favoriteList) {
			for(Item item : itemList) {
				if(fav.getItemId() == item.getId()) {
					sendFavoriteList.add(fav);
					break;
				}
			}
		}
		
		model.addAttribute("favorites", sendFavoriteList);
		model.addAttribute("items", sendItemList);
		return "favorite/favorite_list";
	}
	
	@RequestMapping("/favorite/delete")
	public String delFavorite(Model model, HttpSession session, Integer itemId) {
		Integer userId = ((UserBean) session.getAttribute("user")).getId();
		Favorite favorite = favoriteRepository.findByUserIdAndItemId(userId, itemId);
		favorite.setIsFav(0);
		
		String itemName = itemRepository.getById(itemId).getName();
		
		favoriteRepository.save(favorite);
		model.addAttribute("itemName", itemName);
		return "forward:/favorite/list";
	}
}
