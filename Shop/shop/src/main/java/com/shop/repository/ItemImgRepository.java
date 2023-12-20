package com.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.entity.ItemImg;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long>
{
	List<ItemImg> findByItemIdOrderByIdAsc(Long itemId); // 상품 이미지 아이디의 오름차순으로 가져옴
	
	ItemImg findByItemIdAndRepimgYn(Long itemId, String repimgYn); // 구매 이력 페이지에서 주문 상품의 대표 이미지를 보여줌
}