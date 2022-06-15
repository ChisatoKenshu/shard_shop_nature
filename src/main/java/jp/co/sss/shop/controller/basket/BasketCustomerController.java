package jp.co.sss.shop.controller.basket;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Favorite;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.form.BasketForm;
import jp.co.sss.shop.repository.FavoriteRepository;
import jp.co.sss.shop.repository.ItemRepository;

@Controller
public class BasketCustomerController {
	@Autowired
	ItemRepository itemRepository;
	@Autowired
	FavoriteRepository favoriteRepository;
	
	@RequestMapping(path="/basket/list", method=RequestMethod.GET)
	public String basketListGet(HttpSession session, Model model) {
		//ログインしていなかったらログイン画面に遷移する
		if(session.getAttribute("user") == null) {
			return "redirect:/login";
		}
		model.addAttribute("isStockGreaterThanOrderNum", true);
		return "basket/shopping_basket";
	}
	
	@RequestMapping(path="/basket/list", method=RequestMethod.POST)
	public String basketList(HttpSession session, Model model) {
		//ログインしていなかったらログイン画面に遷移する
		if(session.getAttribute("user") == null) {
			return "redirect:/login";
		}
		model.addAttribute("isStockGreaterThanOrderNum", true);
		return "basket/shopping_basket";
	}
	
	@RequestMapping(path="/basket/add", method=RequestMethod.POST)
	public String addItem(Model model, HttpSession session, BasketForm basketForm) {
		//ログインしていなかったらログイン画面に遷移する
		if(session.getAttribute("user") == null) {
			return "redirect:/login";
		}
		
		//初期値設定
		int id =  basketForm.getId();
		Item item = itemRepository.getById(id);
		List<BasketBean> sessionBasketBeanList = (List<BasketBean>) session.getAttribute("basketBeanList");
		List<BasketBean> basketBeanList = new ArrayList<BasketBean>();
		BasketBean basketBean = new BasketBean(id, item.getName(), item.getStock());
		boolean isBasketListExistId = false;
		boolean isStockGreaterThanOrderNum = true;
		
		//sessionの買い物かごリストがnull or 空かどうか判定
		if(sessionBasketBeanList == null || sessionBasketBeanList.isEmpty()) {
			session.setAttribute("basketBeanList", basketBeanList);
			basketBeanList.add(basketBean);
			model.addAttribute("basket", basketBean);
		}else {
			//nullじゃないとき
			basketBeanList = sessionBasketBeanList;
			int cnt = 0;
			for(BasketBean basketBeanCol : basketBeanList) {
				if(basketBeanCol.getId() == basketBean.getId()) {
					
					if(basketBeanCol.getStock() > basketBeanCol.getOrderNum()) {
						int getOrderNum  = basketBeanCol.getOrderNum() + 1;
						basketBeanList.get(cnt).setOrderNum(getOrderNum);
					}else {
						isStockGreaterThanOrderNum = false;
					}
					model.addAttribute("basket", basketBeanCol);
					isBasketListExistId = false;
					break;
				}else {
					isBasketListExistId = true;
				}
				cnt++;
			}
		}
		if(isBasketListExistId == true) {
			basketBeanList.add(basketBean);
			model.addAttribute("basket", basketBean);
		}
		model.addAttribute("isStockGreaterThanOrderNum", isStockGreaterThanOrderNum);
		session.setAttribute("basketBeanList", basketBeanList);
		return "redirect:/basket/list";
	}
	
	@RequestMapping(path="/basket/delete", method=RequestMethod.POST)
	public String deleteItem(Integer id, HttpSession session, Model model) {
		Item item = itemRepository.getById(id);
		List<BasketBean> basketBeanList = (List<BasketBean>) session.getAttribute("basketBeanList");
		
		int cnt = 0;
		for(BasketBean basketBeanCol : basketBeanList) {
			if(basketBeanCol.getId() == item.getId()) {
				int getOrderNum  = basketBeanCol.getOrderNum();
				if(getOrderNum == 1) {
					basketBeanList.remove(cnt);
					
				}else {
					getOrderNum--;
					basketBeanList.get(cnt).setOrderNum(getOrderNum);
				}
				break;
			}
			cnt++;
		}
		model.addAttribute("isStockGreaterThanOrderNum", true);
		session.setAttribute("basketBeanList", basketBeanList);
		return "redirect:/basket/list";
	}
	
	@RequestMapping(path="/basket/allDelete", method=RequestMethod.POST)
	public String deleteAll(HttpSession session, Model model) {
		List<BasketBean> basketBeanList = (List<BasketBean>) session.getAttribute("basketBeanList");
		basketBeanList.clear();
		model.addAttribute("isStockGreaterThanOrderNum", true);
		session.setAttribute("basketBeanList", basketBeanList);
		return "redirect:/basket/list";
	}
	
	@RequestMapping("/basket/favorite/add")
	public String addFavoriteItem(Model model, HttpSession session, BasketForm basketForm) {
		//初期値設定
		int id =  basketForm.getId();
		Item item = itemRepository.getById(id);
		List<BasketBean> sessionBasketBeanList = (List<BasketBean>) session.getAttribute("basketBeanList");
		List<BasketBean> basketBeanList = new ArrayList<BasketBean>();
		BasketBean basketBean = new BasketBean(id, item.getName(), item.getStock());
		boolean isBasketListExistId = false;
		boolean isStockGreaterThanOrderNum = true;
		
		//sessionの買い物かごリストがnull or 空かどうか判定
		if(sessionBasketBeanList == null || sessionBasketBeanList.isEmpty()) {
			session.setAttribute("basketBeanList", basketBeanList);
			basketBeanList.add(basketBean);
			model.addAttribute("basket", basketBean);
		}else {
			//nullじゃないとき
			basketBeanList = sessionBasketBeanList;
			int cnt = 0;
			for(BasketBean basketBeanCol : basketBeanList) {
				if(basketBeanCol.getId() == basketBean.getId()) {
					
					if(basketBeanCol.getStock() > basketBeanCol.getOrderNum()) {
						int getOrderNum  = basketBeanCol.getOrderNum() + 1;
						basketBeanList.get(cnt).setOrderNum(getOrderNum);
					}else {
						isStockGreaterThanOrderNum = false;
					}
					model.addAttribute("basket", basketBeanCol);
					isBasketListExistId = false;
					break;
				}else {
					isBasketListExistId = true;
				}
				cnt++;
			}
		}
		if(isBasketListExistId == true) {
			basketBeanList.add(basketBean);
			model.addAttribute("basket", basketBean);
		}
		
		//お気に入り情報を論理削除する
		Integer userId = ((UserBean) session.getAttribute("user")).getId();
		Favorite favorite = favoriteRepository.findByUserIdAndItemId(userId, id);
		favorite.setIsFav(0);
		favoriteRepository.save(favorite);
		
		model.addAttribute("isStockGreaterThanOrderNum", isStockGreaterThanOrderNum);
		session.setAttribute("basketBeanList", basketBeanList);
		return "redirect:/basket/list";
	}
}
