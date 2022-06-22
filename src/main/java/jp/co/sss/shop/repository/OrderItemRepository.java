package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.OrderItem;

/**
 * order_itemsテーブル用リポジトリ
 *
 * @author System Shared
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
	
	/** OrderItemテーブルから商品IDを売れた個数の降順で取り出す*/
	@Query("SELECT oi.item.id FROM OrderItem oi GROUP BY oi.item.id ORDER BY SUM(oi.quantity) DESC, oi.order.id ASC")
	public List<Integer> findIdSUMDescWithQuery();
	
	
	//購入された商品の商品Idを検索
	//@Query("SELECT DISTINCT oi.item.id FROM OrderItem oi")
	//public List<Integer> findItemIdWithQuery();
	
	/** 購入された商品のカテゴリーIdを検索*/
	@Query("select i.category.id from Item i")
	public Integer findCategoryIdById(Integer id);
	
	//商品情報をカテゴリーIdで検索
	@Query("select i from Item i where i.deleteFlag = 0 and i.category.id = :category")
	public List<Item> findByDeleteFlagAndCategoryId(Category category);


}
