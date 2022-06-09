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
	public String showBasket() {
		return "basket/shopping_basket";
	}
	
	@RequestMapping(path="/basket/add", method=RequestMethod.POST)
	public String addBasket(Model model, HttpSession session, BasketForm basketForm) {
			int id =  basketForm.getId();
			Item item = itemRepository.getById(id);
			List<BasketBean> basketBeanList = (List<BasketBean>) session.getAttribute("basketBeanList");
			BasketBean basketBean = new BasketBean(id, item.getName(), item.getStock());
			if(basketBeanList.isEmpty()) {
				basketBeanList.add(basketBean);
			}

			int cnt = 0;
			for(BasketBean basketBeanCol : basketBeanList) {
				if(basketBeanCol.getId() == basketBean.getId()) {
					int getOrderNum  = basketBeanCol.getOrderNum() + 1;
					basketBeanList.get(cnt).setOrderNum(getOrderNum);
					System.out.println("\n\n\n\nsdgfsdfg\nstock: " + basketBeanList.get(0).getOrderNum());
				}
				cnt++;
			}
			session.setAttribute("basketBeanList", basketBeanList);
			System.out.println();
			
			session.setAttribute("basketBean", basketBean);
			model.addAttribute("basketBeanList",basketBeanList);
		return "basket/shopping_basket";
	}
}
