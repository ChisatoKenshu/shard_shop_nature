package jp.co.sss.shop.controller.favorite;

import java.util.List;

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
	public String favoriteList(Model model, HttpSession session) {
		Integer userId = ((UserBean) session.getAttribute("user")).getId();
		List<Favorite> favoriteList = favoriteRepository.findByUserIdAndIsFav(userId, 1);
		List<Item> itemList = itemRepository.findAll();
		
		model.addAttribute("favorites", favoriteList);
		model.addAttribute("items", itemList);
		return "favorite/favorite_list";
	}
}
