package com.shop.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemImgDto;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class ItemService 
{
	@Autowired
	ItemRepository itemRepository;
	
	@Autowired
	ItemImgService itemImgService;
	
	@Autowired
	ItemImgRepository itemImgRepository;
	
	public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception
	{
		// 상품 등록 폼으로 부터 입력 받은 데이터를 이용해 객체 생성
		Item item = itemFormDto.createItem();
		itemRepository.save(item);
		
		for(int i = 0; i < itemImgFileList.size(); i++ )
		{
			ItemImg itemImg = new ItemImg();
			itemImg.setItem(item);
			
			if(i == 0) // 첫 번째 이미지일 경우 대표 상품 이미지 여부 값을 Y로 세팅
			{
				itemImg.setRepimgYn("Y");
			}
			else
			{
				itemImg.setRepimgYn("N");
			}
			
			// 상품 이미지 정보를 저장
			itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
		}
		
		return item.getId();
	}
	
	@Transactional(readOnly = true)
	public ItemFormDto getItemDtl(Long itemId)
	{
		List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId); // 해당 상품의 이미지를 조회
		List<ItemImgDto> itemImgDtoList = new ArrayList<>();
		
		for(ItemImg itemImg : itemImgList) // 조회한 ItemImg 엔티티를 ItemImgDto 객체로 만들어 리스트에 추가
		{
			ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
			itemImgDtoList.add(itemImgDto);
		}
		
		Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new); // 상품의 아이디를 통해 상품 엔티티 조회
		ItemFormDto itemFormDto = ItemFormDto.of(item);
		itemFormDto.setItemImgDtoList(itemImgDtoList);
		return itemFormDto;
	}
	
	public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception
	{
		// 상품 수정
		Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new); // 상품 아이디를 이용 하여 상품 엔티티를 조회
		item.updateItem(itemFormDto); // 상품 엔티티를 업데이트
		
		List<Long> itemImgIds = itemFormDto.getItemImgIds(); // 상품 이미지 아이디 리스트를 조회
		for(int i = 0; i < itemImgIds.size(); i++)
		{
			itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i)); // 이미지 업데이트
		}
		return item.getId();
	}
	
	@Transactional(readOnly = true)
	public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable)
	{
		return itemRepository.getAdminItemPage(itemSearchDto, pageable);
	}
	
	@Transactional(readOnly = true)
	public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable)
	{
		return itemRepository.getMainItemPage(itemSearchDto, pageable);
	}
}