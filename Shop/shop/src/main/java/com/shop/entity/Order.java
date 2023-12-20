package com.shop.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.shop.constant.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name="orders")
@Data
@Entity
public class Order extends BaseEntity
{
	@Id
	@GeneratedValue
	@Column(name="order_id")
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY) // 주문 -> 회원 (다대일 관계)
	@JoinColumn(name="member_id")
	private Member member;
	
	@OneToMany(mappedBy="order", cascade=CascadeType.ALL, 
			orphanRemoval=true, fetch=FetchType.LAZY) // OrderItem에서 외래키를 가지고 있으므로 연관 관계 주인을 설정, 부모 엔티티 영속성 상태 변화를 자식 엔티티에 모두 전이하는 ALL 옵션
	private List<OrderItem> orderItem = new ArrayList<>();
	
	
	private LocalDateTime orderDate; // 주문일
	
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus; // 주문상태
	
	public void addOrderItem(OrderItem orderItem)
	{
		// orderItem에는 주문 상품 정보들을 담아줌
		this.orderItem.add(orderItem);
		// order와 orderItem이 양방향 참조 관계 이므로 orderItem 객체에서도 order객체를 세팅
		orderItem.setOrder(this);
	}
	
	public static Order createOrder(Member member, List<OrderItem> orderItemList)
	{
		Order order = new Order();
		order.setMember(member); // 상품을 주문한 회원의 정보를 세팅
		for(OrderItem orderItem : orderItemList)
		{
			// 상품 페이지에는 1개의 상품을 주문하지만, 장바구니 페이지에는 한 번의 여러개의 상품을 주문할 수 있어서 여러개의 주문 상품을 담을 수 있도록 리스트형태로 파라미터 값을 받으며 주문 객체에 orderItem 객체를 추가
			order.addOrderItem(orderItem);
		}
		
		order.setOrderStatus(OrderStatus.ORDER); // 주문 상태를 세팅함
		order.setOrderDate(LocalDateTime.now()); // 현재 시간을 주문 시간으로 세팅
		return order;
	}
	
	public int getTotalPrice() // 총 주문 금액 메서드
	{
		int totalPrice = 0;
		for(OrderItem orderItem : this.orderItem)
		{
			totalPrice += orderItem.getTotalPrice();
		}
		return totalPrice;
	}
	
	public void cancelOrder()
	{
		this.orderStatus = OrderStatus.CANCEL;
		
		for(OrderItem orderItem : this.orderItem)
		{
			orderItem.cancel();
		}
	}
	
}