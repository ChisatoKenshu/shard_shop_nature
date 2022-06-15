package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Item;

/**
 * itemsテーブル用リポジトリ
 *
 * @author System Shared
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

	/** 商品IDを削除フラグで検索*/
	@Query("SELECT i.id FROM Item i WHERE i.deleteFlag = 0 ORDER BY i.id ASC")
	public List<Integer> findIdWithQuery();
	
	/**  商品情報を新着順で検索*/
	public List<Item> findByDeleteFlagOrderByInsertDateDescIdAsc(int deleteFlag);
	
	/** 商品IDを新着順で検索*/
	@Query("SELECT i.id FROM Item i WHERE i.deleteFlag = 0 ORDER BY i.insertDate DESC, i.id ASC")
	public List<Integer> findIdOrderByInsertDateDescWithQuery();
	
	/**  商品情報をカテゴリで検索*/
	public List<Item> findByDeleteFlagAndCategory(int deleteFlag, Category category);
	
	/**  商品IDをカテゴリで検索*/
	@Query("SELECT i.id FROM Item i WHERE i.deleteFlag = 0 AND i.category.id = :id")
	public List<Integer> findIdByCategoryWithQuery(Integer id);

	/** 購入された商品のカテゴリーIdを検索*/
	@Query("select i.category.id from Item i")
	public List<Item> findCategoryIdById(Integer id);


}
