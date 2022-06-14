package jp.co.sss.shop.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "favorite")
@IdClass(value=FavoriteKey.class)
public class Favorite {
	@Id
	@ManyToOne
	@JoinColumn(name = "item_id", referencedColumnName = "id")
	private Item item;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	@Column
	private Integer isfav;

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getIsfav() {
		return isfav;
	}

	public void setIsfav(Integer isfav) {
		this.isfav = isfav;
	}
}
