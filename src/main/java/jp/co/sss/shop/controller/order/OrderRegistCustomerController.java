package jp.co.sss.shop.controller.order;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.bean.OrderBean;
import jp.co.sss.shop.bean.OrderItemBean;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.Order;
import jp.co.sss.shop.entity.OrderItem;
import jp.co.sss.shop.entity.User;
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
	 * 届け先情報入力処理
	 *
	 * @param model Viewとの値受渡し
	 * @return "item/update/item_update_input" カテゴリ情報 変更入力画面へ
	 */
	@RequestMapping(path = "/address/input", method = RequestMethod.POST)
	public String updateInput(boolean backFlg, Model model, @ModelAttribute OrderForm form, HttpSession session) {

		// 戻るボタンで遷移してきたか判定
		if (!backFlg) {

			// ユーザ情報を取得
			Integer userId = ((UserBean) session.getAttribute("user")).getId();
			User user = userRepository.getById(userId);

			// 届け先情報を生成し、name,postalCode,address,phoneNumberをコピー
			OrderBean orderBean = new OrderBean();
			BeanUtils.copyProperties(user, orderBean);

			// 届け先情報をViewに渡す
			model.addAttribute("order", orderBean);

		} else {

			// 届け先情報の生成
			OrderBean orderBean = new OrderBean();
			BeanUtils.copyProperties(form, orderBean);

			// 届け先情報をViewに渡す
			model.addAttribute("order", orderBean);
		}
		return "order/regist/order_address_input";
	}

	/**
	 * 支払情報入力処理
	 *
	 * @param model Viewとの値受渡し
	 * @return "order/regist/order_check" 注文内容確認画面へ
	 */
	@RequestMapping(path = "/payment/input", method = RequestMethod.POST)
	public String inputPayment(Model model, @Valid @ModelAttribute OrderForm form, BindingResult result,
			HttpSession session) {

		// 入力値を注文情報にコピー
		OrderBean orderBean = new OrderBean();
		BeanUtils.copyProperties(form, orderBean);

		// 注文情報をViewに渡す
		model.addAttribute("order", orderBean);

		// 入力値にエラーがあった場合、届け先入力画面に戻る
		if (result.hasErrors()) {

			return "order/regist/order_address_input";
		}

		// ラジオボタンのMapを作成しModelでViewに渡す
		Map<Integer, String> radioPayment = new LinkedHashMap<>();
		radioPayment.put(1, "クレジットカード");
		radioPayment.put(2, "銀行振込");
		radioPayment.put(3, "着払い");
		radioPayment.put(4, "電子マネー");
		radioPayment.put(5, "コンビニ決済");
		// ラジオボタンの初期値を設定
		form.setPayMethod(1);
		model.addAttribute("payments", radioPayment);

		return "order/regist/order_payment_input";
	}

	/**
	 * 注文内容確認処理
	 *
	 * @param form 届け先情報, 支払情報
	 * @return "order/check" 注文情報確認画面へ
	 */
	@RequestMapping(path = "/order/check", method = RequestMethod.POST)
	public String checkOrder(Model model, @Valid @ModelAttribute OrderForm form, BindingResult result,
			HttpSession session, OrderBean order) {

		OrderBean orderBean = order;
		BeanUtils.copyProperties(form, orderBean);

		// 注文情報をViewに渡す
		model.addAttribute("order", orderBean);

		// 入力値にエラーがあった場合、入力画面に戻る
		if (result.hasErrors()) {

			return "order/regist/order_payment_input";
		}

		// OrderItemBean型の空のリストを作成
		List<OrderItemBean> orderItemBeans = new ArrayList<OrderItemBean>();
		
		// 金額合計表示用変数
		Integer total = 0;
		
		// 在庫確認用Map
		Map<String, Integer> stockMap = new LinkedHashMap<>();

		// セッションから買い物かご情報のリストを取得
		List<BasketBean> basketBeans = (List<BasketBean>) session.getAttribute("basketBeanList");

		// リスト内の商品と個数を計算して空のリストに追加していく
		for (BasketBean bean : basketBeans) {
			OrderItemBean orderItem = new OrderItemBean();
			Item item = new Item();

			// リスト内の商品情報を取得
			item = itemRepository.getById(bean.getId());

			// id, name, price, imageをコピー
			BeanUtils.copyProperties(item, orderItem);
			
			// orderNumを設定
			orderItem.setOrderNum(bean.getOrderNum());

			// subtotalを設定しtotalに加算
			Integer subtotal = orderItem.getPrice() * orderItem.getOrderNum();
			orderItem.setSubtotal(subtotal);
			total += subtotal;
			
			// 在庫の確認
			Integer stock = item.getStock() - orderItem.getOrderNum();
			if (item.getStock() == 0) {
				stockMap.put(item.getName(), 0);
			}else if (stock < 0) {
				stockMap.put(item.getName(), 1);
			}

			// 注文商品情報リストに追加
			orderItemBeans.add(orderItem);
		}

		// 注文商品情報をViewに渡す
		model.addAttribute("orderItemBeans", orderItemBeans);
		model.addAttribute("total", total);
		model.addAttribute("stocks", stockMap);
		

		session.setAttribute("orderItemBeans", orderItemBeans);

		return "order/regist/order_check";
	}

	/**
	 * 商品情報登録完了画面
	 *
	 * @return "order/regist/order_complete" 注文完了画面へ
	 */
	@RequestMapping(path = "/order/complete", method = RequestMethod.POST)
	public String orderComplete(@ModelAttribute OrderForm form, HttpSession session) {
		Order order = new Order();

		// 入力値を注文情報にコピー
		BeanUtils.copyProperties(form, order);

		// 現在の日時を注文日時として注文情報に追加
		long nowDate = System.currentTimeMillis();
		Date date = new Date(nowDate);
		order.setInsertDate(date);

		// ユーザ情報を注文情報に追加
		Integer userId = ((UserBean) session.getAttribute("user")).getId();
		order.setUser(userRepository.getById(userId));

		// 空の注文商品情報を生成して注文情報に追加
		List<OrderItem> orderItems = new ArrayList<>();
		order.setOrderItemsList(orderItems);

		// 注文情報を保存
		orderRepository.save(order);

		// セッションから注文商品情報のリストを取得
		List<OrderItemBean> orderItemBeans = (List<OrderItemBean>) session.getAttribute("orderItemBeans");

		// 注文商品情報のリストをorderItemに格納して登録
		for (OrderItemBean bean : orderItemBeans) {
			OrderItem orderItem = new OrderItem();
			Item item = new Item();

			// リスト内の商品情報を取得
			item = itemRepository.getById(bean.getId());

			// orderItemの情報を設定
			orderItem.setQuantity(bean.getOrderNum());
			orderItem.setOrder(order);
			orderItem.setItem(item);
			orderItem.setPrice(item.getPrice());
			
			// 商品の在庫を変更
			item.setStock(item.getStock() - orderItem.getQuantity());
			itemRepository.save(item);

			// 注文商品情報をリポジトリに保存する
			orderItemRepository.save(orderItem);
			// 注文商品情報リストに追加
			orderItems.add(orderItem);
		}

		// 注文商品情報を上書きして保存
		order.setOrderItemsList(orderItems);
		orderRepository.save(order);
		
		List<BasketBean> basketBeanList = (List<BasketBean>) session.getAttribute("basketBeanList");
		basketBeanList.clear();
		session.setAttribute("basketBeanList", basketBeanList);

		return "order/regist/order_complete";
	}

}
