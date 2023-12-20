package com.shop.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.entity.QItem;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@SpringBootTest
//@TestPropertySource(locations="classpath:application-test.properties")
public class ItemRepositoryTest 
{
	@Autowired
	ItemRepository itemRepository;
	
	@PersistenceContext
	EntityManager em;
	
	@Test
	@Transactional
	@DisplayName("상품 저장 테스트")
	public void createItemTest()
	{
		Item item = new Item();
		
		item.setItemNm("테스트 상품");
		item.setPrice(10000);
		item.setItemDetail("테스트 상품 상세 설명");
		item.setItemSellStatus(ItemSellStatus.SELL);
		item.setStockNumber(100);
		item.setRegTime(LocalDateTime.now());
		item.setUpdateTime(LocalDateTime.now());
		
		Item savedItem = itemRepository.save(item);
		
		System.out.println(savedItem.toString());
	}
	
	
	@Test
	@Transactional
	public void createItemList()
	{
		for(int i = 0; i < 10; i++)
		{
			Item item = new Item();
			
			item.setItemNm("테스트 상품" + i);
			item.setPrice(10000 + i);
			item.setItemDetail("테스트 상품 상세 설명" + i);
			item.setItemSellStatus(ItemSellStatus.SELL);
			item.setStockNumber(100);
			item.setRegTime(LocalDateTime.now());
			item.setUpdateTime(LocalDateTime.now());
			
			Item savedItem = itemRepository.save(item);
		}
	}
	
	@Test
	@Transactional
	public void createItemList2()
	{
		for(int i = 1; i <= 5; i++)
		{
			Item item = new Item();
			
			item.setItemNm("테스트 상품" + i);
			item.setPrice(10000 + i);
			item.setItemDetail("테스트 상품 상세 설명" + i);
			item.setItemSellStatus(ItemSellStatus.SELL);
			item.setStockNumber(100);
			item.setRegTime(LocalDateTime.now());
			item.setUpdateTime(LocalDateTime.now());
			itemRepository.save(item);
		}
		
		for(int i = 6; i <= 10; i++)
		{
			Item item = new Item();
			
			item.setItemNm("테스트 상품" + i);
			item.setPrice(10000 + i);
			item.setItemDetail("테스트 상품 상세 설명" + i);
			item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
			item.setStockNumber(100);
			item.setRegTime(LocalDateTime.now());
			item.setUpdateTime(LocalDateTime.now());
			itemRepository.save(item);
		}
	}
// 쿼리 메소드	
	@Test
	@DisplayName("상품명 조회 테스트")
	public void findByItemNmTest()
	{
		this.createItemList();
		List<Item> itemLsit = itemRepository.findByItemNm("테스트 상품1");
		
		for(Item item : itemLsit)
		{
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("상품명, 상품상세설명 조회 테스트")
	public void findByItemNmOrItemDetailTest()
	{
		this.createItemList();
		List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품2", "테스트 상품 상세 설명5");
		
		for(Item item : itemList)
		{
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("가격 LessThan 조회 테스트")
	public void findByPriceLessThanTest()
	{
		this.createItemList();
		List<Item> itemList = itemRepository.findByPriceLessThan(10007);
		
		for(Item item : itemList)
		{
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("가격 내림차순 조회 테스트")
	public void findByPriceLessThanOrderByPriceDesc()
	{
		this.createItemList();
		List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10008);
		
		for(Item item : itemList)
		{
			System.out.println(item.toString());
		}
	}
// 쿼리 메소드
	
// @Query
	@Test
	@DisplayName("@Query를 이용한 상품 조회 테스트")
	public void findByItemDetailTest()
	{
		this.createItemList();
		List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
		
		for(Item item : itemList)
		{
			System.out.println(item.toString());
		}
	}
// @Query
	
// Querydsl
	@Test
	@DisplayName("Querydsl 조회 테스트")
	public void queryDslTest()
	{
		this.createItemList();
		JPAQueryFactory queryFactory = new JPAQueryFactory(em); // 쿼리를 동적으로 생성
		QItem qItem = QItem.item; // Querydsl을 통해 생성된 QItem 객체를 이용
		
		// 자바 소스코드 이지만 SQL문과 비슷하게 작성 가능
		JPAQuery<Item> query = queryFactory.selectFrom(qItem)
							.where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
							.where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))
							.orderBy(qItem.price.desc());
		List<Item> itemList = query.fetch(); // JPAQuery 메소드중 하나인 fetch()를 이용하여 쿼리 결과를 리스트로 반환함(fetch() 실행 지점에서 쿼리문 실행)
		
		for(Item item : itemList)
		{
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("Querydsl 조회 테스트 2")
	public void queryDslTest2()
	{
		this.createItemList2();
		
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		QItem item = QItem.item;
		String itemDetail = "테스트 상품 상세 설명";
		int price = 10003;
		String itemSellStat = "SELL";
		
		// 조건
		booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
		booleanBuilder.and(item.price.gt(price));		
		if(StringUtils.equals(itemSellStat, ItemSellStatus.SELL))
		{
			booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
		}
		// 조건
		
		Pageable pageable = PageRequest.of(0, 5); // 조회 할 페이지 번호. 페이지 당 조회 할 데이터 갯수
		Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable); // 해당 페이지에서 조건에 맞는 데이터를 조회
		
		System.out.println("total elements : " + itemPagingResult.getTotalElements());
		
		List<Item> resultItemList = itemPagingResult.getContent();
		for(Item resultItem : resultItemList)
		{
			System.out.println(resultItem.toString());
		}
	}
// Querydsl	
}