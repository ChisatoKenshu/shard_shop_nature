package jp.co.sss.shop.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "favorite")
@IdClass(value=FavoriteKey.class)
public class Favorite {
	@Id
	private Integer itemId;
	
	@Id
	private Integer userId;
	
	@Column
	private Integer isFav;

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getIsFav() {
		return isFav;
	}

	public void setIsFav(Integer isFav) {
		this.isFav = isFav;
	}
}
