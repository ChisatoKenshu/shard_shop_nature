package jp.co.sss.shop.entity;

import java.io.Serializable;

public class FavoriteKey implements Serializable {
	private Integer itemId;
	private Integer userId;
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
}
