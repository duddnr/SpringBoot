package com.shop.dto;

import com.shop.constant.ItemSellStatus.ItemSellStatus;

import lombok.Data;

@Data
public class ItemSearchDto 
{
	/*
	 *  all : 상품 등록일 전체
	 *  1d : 최근 하루 동안 등록된 상품
	 *  1w : 최근 일주일 동안 등록된 상품
	 *  1m : 최근 한달 동안 등록된 상품
	 *  6m : 최근 6개월 동안 등록된 상품
	 */
	private String searchDateType; // 현재 시간과 상품 등록일을 비교하여 상품 데이터를 조회
	private ItemSellStatus searchSellStatus; // 상품의 판매 상태를 기준으로 상품 데이터를 조회
	private String searchBy; // 상품을 조회 시 어떤 유형으로 조회할지 선택 (itemNm 상품명, createdBy 상품 등록자 아이디)
	private String searchQuery = ""; // 조회할 검색어를 저장할 변수 (itemNm일 경우 상품명을 기준으로, createdBy일 경우 상품 등록자 아이디 기준)
}