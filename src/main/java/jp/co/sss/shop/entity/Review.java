package jp.co.sss.shop.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * レビュー情報のエンティティクラス
 *
 * @author 横田
 */
@Entity
@Table(name = "reviews")
public class Review {
	
	/**
	 * レビューID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_reviews_gen")
	@SequenceGenerator(name = "seq_reviews_gen", sequenceName = "seq_reviews", allocationSize = 1)
	private Integer id;
	
	/**
	 * 会員情報
	 */
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	/**
	 * 商品情報
	 */
	@ManyToOne
	@JoinColumn(name = "item_id", referencedColumnName = "id")
	private Item item;
	
	/**
	 * 評価値
	 */
	@Column
	private Integer evaluation;
	
	/**
	 * タイトル
	 */
	@Column
	private String title;
	
	/**
	 * ニックネーム
	 */
	@Column
	private String nickname;
	
	/**
	 * レビュー本文
	 */
	@Column
	private String reviewText;
	
	/**
	 * 表示許可フラグ
	 */
	@Column
	private Integer permissionFlag;
	
	/**
	 * 登録日時
	 */
	@Column(insertable = false)
	private Date insertDate;
	
	// デフォルトコンストラクタ
	public Review() {
		permissionFlag = 0;
	}
	
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
