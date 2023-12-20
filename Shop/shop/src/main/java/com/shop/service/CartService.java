package com.shop.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.shop.dto.CartDetailDto;
import com.shop.dto.CartItemDto;
import com.shop.dto.CartOrderDto;
import com.shop.dto.OrderDto;
import com.shop.entity.Cart;
import com.shop.entity.CartItem;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.repository.CartItemRepository;
import com.shop.repository.CartRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class CartService 
{
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private OrderService orderService;
	
	public Long addCart(CartItemDto cartItemDto, String email)
	{
		Item item = itemRepository.findById(cartItemDto.getItemId()).orElseThrow(EntityNotFoundException::new); // 장바구니에 담을 상품 엔티티 조회
		Member member = memberRepository.findByemail(email); // 현재 로그인한 회원 엔티티를 조회
		
		Cart cart = cartRepository.findByMemberId(member.getId()); // 현재 로그인한 회원의 장바구니 엔티티를 조회
		if(cart == null) // 상품을 처음으로 장바구니에 담을 경우 해당 회원의 장바구니 엔티티를 생성
		{
			cart = Cart.createCart(member);
			cartRepository.save(cart);
		}
		
		CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId()); // 현재 상품이 장바구니에 이미 들어가있는지 조사
		if(savedCartItem != null) // 장바구니에 이미 있던 상품일 경우 기존 수량에 현재 장바구니에 담을 수량 만큼을 더해줌
		{
			savedCartItem.addCount(cartItemDto.getCount());
			return savedCartItem.getId();
		}
		else
		{
			CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount()); // 장바구니 엔티티, 상품 엔티티, 장바구니에 담을 수량을 이용하여 CartItem 엔티티를 생성
			cartItemRepository.save(cartItem); // 장바구니에 들어갈 상품을 저장
			return cartItem.getId();
		}
	}
	
	@Transactional(readOnly = true)
	public List<CartDetailDto> getCartList(String email)
	{
		List<CartDetailDto> cartDetailDtoList = new ArrayList<>();
		
		Member member = memberRepository.findByemail(email);
		Cart cart = cartRepository.findByMemberId(member.getId()); // 현재 로그인한 회원의 장바구니 엔티티를 조회
		if(cart == null) // 장바구니에 상품을 한 번도 안담았을 경우 장바구니 엔티티가 없으므로 빈 리스트를 반환
		{
			return cartDetailDtoList;
		}
		
		cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId()); // 장바구니에 담겨있는 상품 정보를 조회
		return cartDetailDtoList;
	}
	
	@Transactional(readOnly = true)
	public boolean validateCartItem(Long cartItemId, String email)
	{
		Member curMember = memberRepository.findByemail(email); // 현재 로그인한 회원을 조회
		CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
		Member savedMember = cartItem.getCart().getMember(); // 장바구니 상품을 저장한 회원을 조회
		
		if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) { return false; } // 현재 로그인한 회원과 장바구니 상품을 저장한 회원이 다를 경우 false
		
		return true;
	}
	
	public void updateCartItemCount(Long cartItemId, int count) // 장바구니 상품의 수량을 업데이트하는 메서드
	{
		CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
		cartItem.updateCount(count);
	}
	
	public void deleteCartItem(Long cartItemId)
	{
		CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
		cartItemRepository.delete(cartItem);
	}
	
	public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String email)
	{
		List<OrderDto> orderDtoList = new ArrayList<>();
		for(CartOrderDto cartOrderDto : cartOrderDtoList) // 장바구니 페이지에서 전달받은 주문 상품 번호를 이용하여 주문 로직으로 전달할 orderDto 객체를 생성함
		{
			CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);
			
			OrderDto orderDto = new OrderDto();
			orderDto.setItemId(cartItem.getItem().getId());
			orderDto.setCount(cartItem.getCount());
			orderDtoList.add(orderDto);
		}
		
		Long orderId = orderService.orders(orderDtoList, email); // 장바구니에 담은 상품을 주문하도록 주문로직을 호출
		for(CartOrderDto cartOrderDto : cartOrderDtoList) // 주문한 상품들을 장바구니에서 제거함
		{
			CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);
			cartItemRepository.delete(cartItem);
		}
		return orderId;
	}
}