package com.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name="cart_item")
@Data
@Entity
public class CartItem extends BaseEntity
{
	@Id
	@GeneratedValue
	@Column(name="cart_item_id")
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY) // 장바구니 상품 -> 장바구니 (다대일 관계)
	@JoinColumn(name="cart_id")
	private Cart cart;
	
	@ManyToOne(fetch=FetchType.LAZY) // 장바구니 상품 -> 상품
	@JoinColumn(name="item_id")
	private Item item;
	
	private int count;
	
	public static CartItem createCartItem(Cart cart, Item item, int count)
	{
		CartItem cartItem = new CartItem();
		cartItem.setCart(cart);
		cartItem.setItem(item);
		cartItem.setCount(count);
		return cartItem;
	}
	
	public void addCount(int count) // 장바구니에 기존에 담겨있는 상품인데 해당 상품을 추가로 장바구니에 담을 때 기존 수량에 현재 담을 수량을 더함
	{
		this.count += count;
	}
	
	public void updateCount(int count) // 장바구니 수량 업데이트
	{
		this.count = count;
	}
}