package jp.co.sss.shop.controller.basket;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BasketCustomerController {
	@RequestMapping("/basket/list")
	public String showBasket() {
		return "basket/shopping_basket";
	}
	
	@RequestMapping("/basket/add")
	public String addBasket() {
		return "basket/shopping_basket";
	}
}
