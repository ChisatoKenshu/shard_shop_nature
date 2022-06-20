package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jp.co.sss.shop.entity.Review;

/**
 * reviewsテーブル用リポジトリ
 *
 * @author 横田
 */
public interface ReviewRepository extends JpaRepository<Review, Integer> {
	
	// 特定商品のレビュー全てを日時の新しい順に取得
	@Query("SELECT r.id FROM Review r WHERE r.item.id = :itemId ORDER BY r.insertDate DESC, r.id ASC")
	public List<Review> findByReviewWhereItemOrderByInsertDateDescQuery(Integer itemId);

}
