package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.sss.shop.entity.Favorite;
import jp.co.sss.shop.entity.FavoriteKey;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.User;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteKey> {
	List<Favorite> findByUser(User user);
	
	List<Favorite> findByItem(Item item);
}
