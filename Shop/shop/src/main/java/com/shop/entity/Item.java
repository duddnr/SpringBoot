package com.shop.entity;

import com.shop.constant.ItemSellStatus.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import com.shop.exception.OutOfStockException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Table(name="item") // 엔티티와 매핑할 테이블을 지정
@SequenceGenerator(name = "ITEM_SEQ", sequenceName = "item_seq", initialValue = 1, allocationSize = 1)
@Entity
public class Item extends BaseEntity
{
	@Id
	@Column(name="item_id") // 필드와 매핑할 컬럼의 이름 설정
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "ITEM_SEQ") // JPA 구현체가 자동으로 생성 전략 결정
	// (mySql = IDENTITY, Oracle = SEQUENCE)
	private Long id; // 상품 아이디
	
	@Column(nullable = false, length = 50) // null 값의 허용 여부 설정
	private String itemNm; // 상품 이름
	
	@Column(name="price", nullable = false)
	private int price; // 상품 가격
	
	@Column(nullable = false)
	private int stockNumber; // 재고 수량
	
	@Lob // BLOB, CLOB 타입 매핑
	@Column(nullable = false)
	private String itemDetail; // 상품 상세 설명
	
	@Enumerated(EnumType.STRING) // enum 타입 매핑
	private ItemSellStatus itemSellStatus; // 상품 판매 상태
	
	public void updateItem(ItemFormDto itemFormDto)
	{
		this.itemNm = itemFormDto.getItemNm();
		this.price = itemFormDto.getPrice();
		this.stockNumber = itemFormDto.getStockNumber();
		this.itemDetail = itemFormDto.getItemDetail();
		this.itemSellStatus = itemFormDto.getItemSellStatus();
	}
	
	public void removeStock(int stockNumber)
	{
		int restStock = this.stockNumber - stockNumber; // 상품의 재고 수량에서 주문 후 남은 재고 수량을 구함
		if(restStock < 0)
		{
			// 상품의 재고가 주문 수량보다 작을 경우 재고 부족 예외를 발생
			throw new OutOfStockException("상품의 재고가 부족 합니다.(현재 재고 수량 : " + this.stockNumber + ")");
		}
		this.stockNumber = restStock; // 주문 후 남은 재고 수량을 상품의 현재 재고 값으로 할당
	}
	
	public void addStock(int stockNumber) // 상품의 재고를 증가시키는 메소드
	{
		this.stockNumber += stockNumber;
	}
}