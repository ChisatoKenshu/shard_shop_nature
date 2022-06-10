package jp.co.sss.shop.controller.order;

import java.sql.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Order;
import jp.co.sss.shop.entity.OrderItem;
import jp.co.sss.shop.form.OrderForm;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderItemRepository;
import jp.co.sss.shop.repository.OrderRepository;
import jp.co.sss.shop.repository.UserRepository;

/**
 * 注文管理 注文登録機能(一般会員用)のコントローラクラス
 *
 * @author 横田
 */
@Controller
public class OrderRegistCustomerController {

	/**
	 * サーブレットコンテキスト
	 */
	@Autowired
	ServletContext context;

	/**
	 * ユーザ情報
	 */
	@Autowired
	public UserRepository userRepository;

	/**
	 * 商品情報
	 */
	@Autowired
	public ItemRepository itemRepository;

	/**
	 * 注文商品情報
	 */
	@Autowired
	public OrderItemRepository orderItemRepository;

	/**
	 * 注文情報
	 */
	@Autowired
	public OrderRepository orderRepository;

	/**
	 * 商品情報登録完了画面
	 *
	 * @return "item/regist/item_regist_complete" 商品情報 登録完了画面へ
	 */
	@RequestMapping(path = "/item/regist/complete", method = RequestMethod.GET)
	public String orderComplete(@ModelAttribute OrderForm form, HttpSession session) {
		
		// セッションの注文情報を格納する
		
		List<OrderItem> orderItems = OrderRegistCustomerController.automaticCast(session.getAttribute("basketeBean"));

		// Formクラス内の各フィールドの値を注文情報に追加
		Order order = new Order();
		order.setPostalCode(form.getPostalCode());
		order.setAddress(form.getAddress());
		order.setName(form.getName());
		order.setPhoneNumber(form.getPhoneNumber());
		order.setPayMethod(form.getPayMethod());
		
		// 現在の日時を注文日時として注文情報に追加
		long nowDate = System.currentTimeMillis();
		Date date = new Date(nowDate);
		order.setInsertDate(date);
		
		// ユーザ情報を注文情報に追加
		Integer userId = ((UserBean)session.getAttribute("user")).getId();
		order.setUser(userRepository.getById(userId));
		
		// 注文商品情報を注文情報に追加
		
		order.setOrderItemsList();
		

		

		// 商品情報を保存
		orderRepository.save(order);

		return "order/regist/order_complete";
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T automaticCast(Object object) {
	    T castedObject = (T) object;
	    return castedObject;
	}

}
