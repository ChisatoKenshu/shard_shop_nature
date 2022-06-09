package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jp.co.sss.shop.entity.OrderItem;

/**
 * order_itemsテーブル用リポジトリ
 *
 * @author System Shared
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
	
	@Query("SELECT oi.itemId FROM OrderItem oi GROUP BY oi.itemId ORDER BY SUM(oi.quantity) DESC oi.itemId ASC")
	public List<Integer> findByIdSUMDescWithQuery();
}
