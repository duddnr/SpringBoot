package com.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name="item_img")
@SequenceGenerator(name = "ITEMIMG_SEQ", sequenceName = "itemimg_seq", initialValue = 1, allocationSize = 1)
@Data
@Entity
public class ItemImg extends BaseEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "ITEMIMG_SEQ")
	@Column(name="item_img_id")
	private Long id;
	
	private String imgName; // 이미지 파일 이름
	
	private String oriImgName; // 원본 이미지 파일 이름
	
	private String imgUrl; // 이미지 조회 경로
	
	private String repimgYn; // 대표 이미지 여부
	
	@ManyToOne(fetch=FetchType.LAZY) // 상품 엔티티와 다대일 단방향 관계
	@JoinColumn(name="item_id") 
	private Item item;
	
	public void updateItemImg(String oriImgName, String imgName, String imgUrl)
	{
		this.oriImgName = oriImgName;
		this.imgName = imgName;
		this.imgUrl = imgUrl;
	}
}