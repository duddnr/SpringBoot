package com.shop.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus.ItemSellStatus;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.dto.QMainItemDto;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import com.shop.entity.QItemImg;

import jakarta.persistence.EntityManager;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom
{
	private JPAQueryFactory queryFactory; // JPAQueryFactory 클래스 사용
	
	public ItemRepositoryCustomImpl(EntityManager em)
	{
		this.queryFactory = new JPAQueryFactory(em); // JPAQueryFactory 생성자로 EntityManager 객체를 넣음
	}
	
	private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus)
	{
		/*
		 * 상품 판매 상태 조건이 전체일 경우 null을 리턴 (리턴값이 null이면 where절에서 해당 조건을 무시)
		 * 상품 판매 상태 조건이 null이 아닐 경우 판매중 또는 품절 상태라면 해당 조건의 상품만 조회함
		 */
		return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
	}
	
	private BooleanExpression regDtsAfter(String searchDataType)
	{
		/*
		 *  searchDataType 값의 따라 dateTime의 값을 이전 시간의 값으로 세팅 후 해당 시간 이후로 등록된 상품만 조회 가능
		 *  searchDataType 값이 "1m"인 경우 dateTime의 시간을 한 달 전으로 세팅 후 최근 한 달 동안 등록된 상품만 조회
		 */
		LocalDateTime dateTime = LocalDateTime.now();
		
		if(StringUtils.equals("all", searchDataType) || searchDataType == null) { return null; }
		else if(StringUtils.equals("id", searchDataType)) { dateTime = dateTime.minusDays(1); }
		else if(StringUtils.equals("1w", searchDataType)) { dateTime = dateTime.minusWeeks(1); }
		else if(StringUtils.equals("1m", searchDataType)) { dateTime = dateTime.minusMonths(1); }
		else if(StringUtils.equals("6m", searchDataType)) { dateTime = dateTime.minusMonths(6); }
		
		return QItem.item.regTime.after(dateTime);
	}
	
	private BooleanExpression searchByLike(String searchBy, String searchQuery)
	{
		// searchBy 값에 따라 상품명에 검색어를 포함하고 있는 상품 또는 상품 생성자의 아이디에 검색어를 포함하고 있는 상품을 조회
		if(StringUtils.equals("itemNm", searchBy))
		{
			return QItem.item.itemNm.like("%" + searchQuery + "%");
		}
		else if(StringUtils.equals("createdBy", searchBy))
		{
			return QItem.item.createdBy.like("%" + searchQuery + "%");
		}
		return null;
	}
	
	@Override
	public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable)
	{
		/*
		 *  queryFactory를 이용하여 쿼리를 생성
		 *   - selectFrom(QItem.item) 상품 데이터를 조회하기 위해 QItem의 item을 지정
		 *   -  where 조건절 : BooleanExpression을 반환하는 조건문들을 넣음 ',' 단위로 넣을 경우 and 조건으로 인식함
		 *   - offset : 데이터를 가지고 올 시작 인덱스를 지정
		 *   - limit : 한번에 가지고 올 최대 개수를 지정
		 *   - fetch() : 조회한 리스트 및 전체 개수를 포함하는 List를 반환, 상품 데이터 리스트 조회 및 상품 데이터 젠체 개수를 조회하는 2번의 쿼리문이 진행
		 */
		List<Item> results = queryFactory
				.selectFrom(QItem.item)
				.where(regDtsAfter(itemSearchDto.getSearchDateType()),
								searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
								searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
				.orderBy(QItem.item.id.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
        long total = queryFactory
        		.select(Wildcard.count).from(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .fetchOne();

		return new PageImpl<>(results, pageable, total); // 조회한 데이터를 Page 클래스의 구현체인 PageImpl 객체로 반환
	}
	
	private BooleanExpression itemNmLike(String searchQuery)
	{
		// 검색어가 null이 아니라면 상품명에 해당 검색어가 포함되는 상품을 조회하는 조건을 반환
		return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%" + searchQuery + "%");
	}
	
	@Override
	public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable)
	{
		QItem item = QItem.item;
		QItemImg itemImg = QItemImg.itemImg;
		
		List<MainItemDto> results = queryFactory
				// QMainItemDto 생성자에 반환할 값들을 넣어줌 DTO로 바로 조회가 가능하다
				.select(new QMainItemDto(item.id, item.itemNm, item.itemDetail, itemImg.imgUrl, item.price))
				.from(itemImg)
				.join(itemImg.item, item)
				.where(itemImg.repimgYn.eq("Y"))
				.where(itemNmLike(itemSearchDto.getSearchQuery()))
				.orderBy(item.id.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		long total = queryFactory
				.select(Wildcard.count)
				.from(itemImg)
				.join(itemImg.item, item)
				.where(itemImg.repimgYn.eq("Y"))
				.where(itemNmLike(itemSearchDto.getSearchQuery()))
				.fetchOne();
		
		return new PageImpl<>(results, pageable, total);
	}
}