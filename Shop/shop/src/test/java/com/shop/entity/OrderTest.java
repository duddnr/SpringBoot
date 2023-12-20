package com.shop.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.shop.constant.ItemSellStatus.ItemSellStatus;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderItemRepository;
import com.shop.repository.OrderRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

@Transactional
@SpringBootTest
public class OrderTest 
{
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	OrderItemRepository orderItemRepository;
	
	@Autowired
	ItemRepository itemRepository;
	
	@Autowired
	MemberRepository memberRepository;
	
	@PersistenceContext
	EntityManager em;
	
	public Item createItem()
	{
		Item item = new Item();
		item.setItemNm("테스트 상품");
		item.setPrice(10000);
		item.setItemDetail("상세설명");
		item.setItemSellStatus(ItemSellStatus.SELL);
		item.setStockNumber(100);
		item.setRegTime(LocalDateTime.now());
		item.setUpdateTime(LocalDateTime.now());
		return item;
	}
	
	public Order createOrder()
	{
		Order order = new Order();
		
		for(int i = 0; i < 3; i++)
		{
			Item item = this.createItem();
			itemRepository.save(item);
			
			OrderItem orderItem = new OrderItem();
			orderItem.setItem(item);
			orderItem.setCount(10);
			orderItem.setOrderPrice(1000);
			orderItem.setOrder(order);
			order.getOrderItem().add(orderItem);
		}
		
		Member member = new Member();
		memberRepository.save(member);
		
		order.setMember(member);
		orderRepository.save(order);
		return order;
	}
	
	@Test
	@DisplayName("영속성 전이 테스트")
	public void cascadeTest()
	{
		Order order = new Order();
		
		for(int i = 0; i < 3; i++)
		{
			Item item = this.createItem();
			itemRepository.save(item);
			
			OrderItem orderItem = new OrderItem();
			orderItem.setItem(item);
			orderItem.setCount(10);
			orderItem.setOrderPrice(1000);
			orderItem.setOrder(order);
			order.getOrderItem().add(orderItem);
		}
		
		orderRepository.saveAndFlush(order);
		em.clear();
		
		Order savedOrder = orderRepository.findById(order.getId()).orElseThrow(EntityNotFoundException::new);
		assertEquals(3, savedOrder.getOrderItem().size());
	}
	
	@Test
	@DisplayName("고아객체 제거 테스트")
	public void orphanRemovalTest()
	{
		Order order = this.createOrder();
		order.getOrderItem().remove(0); // orderItem 리스트의 0번째 인덱스를 삭제
		em.flush();
	}
	
	@Test
	@DisplayName("지연 로딩 테스트")
	public void lazyLoadingTest()
	{
		Order order = this.createOrder();
		Long orderItemId = order.getOrderItem().get(0).getId();
		
		em.flush();
		em.clear();
		
		// 데이터 베이스에서 데이터를 가져 올 때 orderitem 테이블만 select 해서 가져오고 연관 관계가 매핑된 엔티티들은 프록시 객체로 넣어둠
		OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(EntityNotFoundException::new);
		System.out.println("Order class : " + orderItem.getOrder().getClass());
		System.out.println("======================================");
		orderItem.getOrder().getOrderDate(); // 프록시에 있는 객체를 로딩 하여 해당 엔티티의 데이터를 가져옴
		System.out.println("======================================");
	}
}