package com.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@ToString
@Table(name="cart")
@SequenceGenerator(name = "CART_SEQ", sequenceName = "cart_seq", initialValue = 1, allocationSize = 1)
@Data
@Entity
public class Cart 
{
	@Id
	@Column(name="cart_id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "CART_SEQ")
	private Long id;
	
	@OneToOne(fetch=FetchType.LAZY) // 장바구니 -> 회원을 일대일로 매핑  (장바구니가 회원을 참조하는 일대일 단방향 매핑)
	@JoinColumn(name="member_id") // 매핑할 외래키를 지정
	private Member member;
	
	public static Cart createCart(Member member)
	{
		Cart cart = new Cart();
		cart.setMember(member);
		return cart;
	}
}