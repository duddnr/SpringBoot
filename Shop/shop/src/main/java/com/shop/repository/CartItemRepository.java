package com.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shop.dto.CartDetailDto;
import com.shop.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long>
{
	CartItem findByCartIdAndItemId(Long cartId, Long itemId);
	
	@Query("select new com.shop.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " + // CartDetailDto 생성자를 이용하여 DTO를 반환
						"from CartItem ci, ItemImg im " +
						"join ci.item i " + 
						"where ci.cart.id = :cartId " + 
						"and im.item.id = ci.item.id " + 
						"and im.repimgYn = 'Y' " + // 장바구니에 있는 상품의 대표 이미지만 가져오도록 함
						"order by ci.regTime desc")
	List<CartDetailDto> findCartDetailDtoList(Long cartId);
}