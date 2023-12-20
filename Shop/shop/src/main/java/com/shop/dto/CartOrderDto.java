package com.shop.dto;

import java.util.List;

import lombok.Data;

@Data
public class CartOrderDto 
{
	private Long cartItemId;
	private List<CartOrderDto> cartOrderDtoList; // 장바구니에서 여러 개의 상품을 주문하므로 CartOrderDto가 자기 자신을 List로 가지고 있도록 만듬
}