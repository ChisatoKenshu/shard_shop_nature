package jp.co.sss.shop.form;

import java.sql.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.User;

/**
 * レビュー情報のフォーム
 *
 * @author 横田
 */
public class ReviewForm {

	/**
	 * レビューID
	 */
	private Integer id;
	
	/**
	 * 会員情報
	 */
	private User user;
	
	/**
	 * 商品情報
	 */
	private Item item;
	
	/**
	 * 評価値
	 */
	@NotNull
	@Max(5)
	@Min(1)
	private Integer evaluation;
	
	/**
	 * タイトル
	 */
	@NotBlank
	@Size(max = 20)
	private String title;
	
	/**
	 * ニックネーム
	 */
	@NotBlank
	@Size(max = 20)
	private String nickname;
	
	/**
	 * レビュー本文
	 */
	@NotBlank
	@Size(max = 200)
	private String reviewText;
	
	/**
	 * 表示許可フラグ
	 */
	private Integer permissionFlag;
	
	/**
	 * 登録日時
	 */
	private Date insertDate;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
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
	
	public Integer getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(Integer evaluation) {
		this.evaluation = evaluation;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public String getReviewText() {
		return reviewText;
	}

	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}
	
	public Integer getPermissionFlag() {
		return permissionFlag;
	}

	public void setPermissionFlag(Integer permissionFlag) {
		this.permissionFlag = permissionFlag;
	}
	
	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

}
