package com.shop.dto;

import org.modelmapper.ModelMapper;

import com.shop.entity.ItemImg;

import lombok.Data;

@Data
public class ItemImgDto 
{
	private Long id;
	private String imgName;
	private String oriImgName;
	private String imgUrl;
	private String repImgYn;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public static ItemImgDto of(ItemImg itemImg)
	{
		// ItemImg 엔티티를 받아서 ItemImg 객체의 변수 자료형과 이름이 같을 때 ItemImgDto 객체로 값을 복사하여 반환
		return modelMapper.map(itemImg, ItemImgDto.class);
	}
}