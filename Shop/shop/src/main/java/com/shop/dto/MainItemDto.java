package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;

@Data
public class MainItemDto 
{
	private Long id;
	private String itemNm;
	private String itemDetail;
	private String imgUrl;
	private int price;
	
	@QueryProjection // Querydsl로 결과 조회 시 MainItemDto 객체로 바로 받아옴
	public MainItemDto(Long id, String itemNm, String itemDetail, String imgUrl, int price)
	{
		this.id = id;
		this.itemNm = itemNm;
		this.itemDetail = itemDetail;
		this.imgUrl = imgUrl;
		this.price = price;
	}
}