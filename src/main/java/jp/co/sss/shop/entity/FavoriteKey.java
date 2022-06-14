package jp.co.sss.shop.entity;

import java.io.Serializable;

public class FavoriteKey implements Serializable {
	private User user;
	private Item item;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
}
