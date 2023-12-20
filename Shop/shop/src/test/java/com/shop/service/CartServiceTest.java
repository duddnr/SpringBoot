package com.shop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.shop.constant.ItemSellStatus.ItemSellStatus;
import com.shop.dto.CartItemDto;
import com.shop.entity.CartItem;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.repository.CartItemRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;

@Transactional
@SpringBootTest
public class CartServiceTest 
{
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	public Item saveItem()
	{
		Item item = new Item();
		item.setItemNm("테스트 상품");
		item.setPrice(10000);
		item.setItemDetail("테스트 상품 상세 설명");
		item.setItemSellStatus(ItemSellStatus.SELL);
		item.setStockNumber(100);
		return itemRepository.save(item);
	}
	
	public Member saveMember()
	{
		Member member = new Member();
		member.setEmail("test1@test.com");
		return memberRepository.save(member);
	}
	
	@Test
	@DisplayName("장바구니 담기 테스트")
	public void addCart()
	{
		Item item = saveItem();
		Member member = saveMember();
		
		CartItemDto cartItemDto = new CartItemDto();
		cartItemDto.setCount(5);
		cartItemDto.setItemId(item.getId());
		
		Long cartItemId = cartService.addCart(cartItemDto, member.getEmail()); // 상품을 장바구니에 담는 로직 호출 결과 생성된 장바구니 상품 아이디를 cartItemId 변수에 저장
		
		CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new); // 장바구니 상품 아이디를 이용하여 생성된 장바구니 상품 정보를 조회
		
		assertEquals(item.getId(), cartItem.getItem().getId()); // 상품 아이디와 장바구니에 저장된 상품 아이디가 같다면 ok
		assertEquals(cartItemDto.getCount(), cartItem.getCount()); // 장바구니에 담았던 수량과 실제로 장바구니에 저장된 수량이 같다면 ok
	}
}