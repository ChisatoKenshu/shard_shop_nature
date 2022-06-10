package jp.co.sss.shop.controller.basket;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.form.BasketForm;
import jp.co.sss.shop.repository.ItemRepository;

@Controller
public class BasketCustomerController {
	@Autowired
	ItemRepository itemRepository;
	
	@RequestMapping("/basket/list")
	public String basketList(Model model) {
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
		List<BasketBean> basketBeanList = (List<BasketBean>) session.getAttribute("basketBeanList");
		BasketBean basketBean = new BasketBean(id, item.getName(), item.getStock());
		boolean isBasketListExistId = false;
		boolean isStockGreaterThanOrderNum = true;
		
		//sessionの買い物かごリストが空かどうか判定
		if(basketBeanList.isEmpty()) {
			basketBeanList.add(basketBean);
			model.addAttribute("basket", basketBean);
		}else {
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
		return "forward:/basket/list";
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
		return "forward:/basket/list";
	}
	
	@RequestMapping(path="/basket/allDelete", method=RequestMethod.POST)
	public String deleteAll(HttpSession session, Model model) {
		List<BasketBean> basketBeanList = (List<BasketBean>) session.getAttribute("basketBeanList");
		basketBeanList.clear();
		model.addAttribute("isStockGreaterThanOrderNum", true);
		session.setAttribute("basketBeanList", basketBeanList);
		return "forward:/basket/list";
	}
}