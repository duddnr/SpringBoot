package com.shop.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDto 
{
	private Long id; // 상품 아이디
	private String itemNm; // 상품 이름
	private int price; // 상품 가격
	private String itemDetail; // 상품 상세 설명
	private String sellStatCd; // 상품 재고 상태
	private LocalDateTime regTime; // 상품 생성 시간
	private LocalDateTime updateTime; // 상품 수정 시간
}