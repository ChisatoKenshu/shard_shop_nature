package jp.co.sss.shop.controller.order;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Item;
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
	@RequestMapping(path = "/order/complete", method = RequestMethod.GET)
	public String orderComplete(@ModelAttribute OrderForm form, HttpSession session) {
		Order order = new Order();
		
		// 入力値を注文情報にコピー
		BeanUtils.copyProperties(form, order);
		
		// 現在の日時を注文日時として注文情報に追加
		long nowDate = System.currentTimeMillis();
		Date date = new Date(nowDate);
		order.setInsertDate(date);
		
		// ユーザ情報を注文情報に追加
		Integer userId = ((UserBean)session.getAttribute("user")).getId();
		order.setUser(userRepository.getById(userId));
		
		// 空の注文商品情報を生成して注文情報に追加
		List<OrderItem> orderItems = new ArrayList<>();
		order.setOrderItemsList(orderItems);
		
		// 注文情報を保存
		orderRepository.save(order);
		
		
		
		// セッションから注文商品情報のリストを取得
		List<BasketBean> beans = (List<BasketBean>) session.getAttribute("basketBeanList");
		
		// 注文商品情報のリストをorderItemに格納して登録
		for(BasketBean bean: beans) {
			OrderItem orderItem = new OrderItem();
			Item item = new Item();
			
			// リスト内の商品情報を取得
			item = itemRepository.getById(bean.getId());
			
			// orderItemの情報を設定
			orderItem.setId(item.getId());
			orderItem.setQuantity(bean.getOrderNum());
			orderItem.setOrder(order);
			orderItem.setItem(item);
			orderItem.setPrice(item.getPrice());
			
			// 注文商品情報をリポジトリに保存する
			orderItemRepository.save(orderItem);
			// 注文商品情報リストに追加
			orderItems.add(orderItem);
		}
		
		// 注文商品情報を上書きして保存
		order.setOrderItemsList(orderItems);
		orderRepository.save(order);

		return "order/regist/order_complete";
	}

}
