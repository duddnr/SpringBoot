package com.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.shop.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom
{
	// 기존 데이터베이스에서 사용하던 쿼리를 그대로 사용해야 할 때 nativeQuery = true
	// @Query(value = "select * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
	@Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc") // JPQL : 객체 대상으로 쿼리문을 실행
	List<Item> findByItemDetail(@Param("itemDetail") String itemDetail); // @Param 어노테이션 으로 JPQL에 들어갈 변수를 지정해줌(:itemDetail)
	
	List<Item> findByItemNm(String ItemNm); // 상품명으로 데이터를 조회함
	List<Item> findByItemNmOrItemDetail(String itemNm, String ItemDetail); // 상품명 또는 상품 상세설명으로 데이터를 조회함
	List<Item> findByPriceLessThan(int price); // 파라미터 값 보다 작은 상품 가격 데이터를 조회함
	List<Item> findByPriceLessThanOrderByPriceDesc(int price); // 파라미터 값 보다 작은 상품 가격을 내림차순으로 데이터를 조회함
}