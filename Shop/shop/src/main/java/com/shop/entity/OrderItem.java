package com.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class OrderItem extends BaseEntity
{
	@Id
	@GeneratedValue
	@Column(name="order_item_id")
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY) // 주문 상품 -> 상품 (지연 로딩) 데이터가 필요 할 시점에 쿼리문이 실행됨
	@JoinColumn(name="item_id")
	private Item item;
	
	@ManyToOne(fetch=FetchType.LAZY) // 주문 상품 -> 주문 (지연 로딩) 데이터가 필요 할 시점에 쿼리문이 실행됨
	@JoinColumn(name="order_id")
	private Order order;
	
	private int orderPrice; // 주문 가격
	private int count; // 수량
	
	public static OrderItem createOrderItem(Item item, int count)
	{
		OrderItem orderItem = new OrderItem();
		orderItem.setItem(item); // 주문할 상품과 주문 수량을 세팅
		orderItem.setCount(count);
		orderItem.setOrderPrice(item.getPrice()); // 현재 시간 기준으로 상품 가격을 주문 가격으로 세팅
		
		item.removeStock(count); // 주문 수량만큼 상품의 재고 수량을 감소
		return orderItem;
	}
	
	public int getTotalPrice()
	{
		return orderPrice * count; // 주문 가격과 주문 수량을 곱해 해당 상품을 주문한 총 가격을 계산
	}
	
	public void cancel()
	{
		this.getItem().addStock(count); // 주문 취소 시 주문 수량만큼 상품 재고를 더해줌
	}
}