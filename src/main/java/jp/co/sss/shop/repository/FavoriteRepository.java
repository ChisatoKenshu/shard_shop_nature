package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.shop.entity.Favorite;
import jp.co.sss.shop.entity.FavoriteKey;

public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteKey> {
	List<Favorite> findByUserId(Integer userId);
}
