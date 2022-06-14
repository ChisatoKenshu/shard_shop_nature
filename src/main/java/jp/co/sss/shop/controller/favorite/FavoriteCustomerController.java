package jp.co.sss.shop.controller.favorite;

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
import jp.co.sss.shop.repository.FavoriteRepository;

@Controller
public class FavoriteCustomerController {
	@Autowired
	private FavoriteRepository favoriteRepository;
	
	@RequestMapping(path="/item/favorite", method=RequestMethod.POST)
	@ResponseBody
	public String addfav(Model model, @RequestParam String json, HttpSession session) {
		//JSON変換用のクラス
	    ObjectMapper mapper = new ObjectMapper();
	    
	    try {
			FavBean favBean =  mapper.readValue(json, FavBean.class);
			UserBean userBean = (UserBean) session.getAttribute("user");
			
			Favorite favorite = new Favorite();
			favorite.setItemId(favBean.getItemId());
			favorite.setUserId(userBean.getId());
			favorite.setIsFav(favBean.getIsFav());
			
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
}
