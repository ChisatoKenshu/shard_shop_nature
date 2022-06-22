package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jp.co.sss.shop.entity.Review;

/**
 * reviewsテーブル用リポジトリ
 *
 * @author 横田
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

	// 特定商品のレビュー全てを日時の新しい順に取得
	@Query("SELECT r FROM Review r WHERE r.item.id = :itemId ORDER BY r.insertDate DESC")
	public List<Review> findByReviewWhereItemOrderByInsertDateDescQuery(Integer itemId);

	// 特定商品のレビュー全てを日時の新しい順に取得
	@Query("SELECT r FROM Review r WHERE r.item.id = :itemId AND r.permissionFlag = 1 ORDER BY r.insertDate DESC")
	public List<Review> findByReviewWhereItemANDPermissionFlagOrderByInsertDateDescQuery(Integer itemId);

	// 特定商品のレビュー平均を算出
	@Query("SELECT ROUND(AVG(r.evaluation), 1) FROM Review r WHERE r.permissionFlag = 1 AND r.item.id = :itemId GROUP BY r.item.id")
	public Double findAVGByPermissionFlagGroupByItemIdQuery(Integer itemId);

}
