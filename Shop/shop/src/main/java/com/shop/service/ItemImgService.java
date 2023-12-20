package com.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class ItemImgService 
{
	@Value("${itemImglocation}") // application에서 등록한 itemImgLocation 값을 불러옴
	private String itemImgLocation;
	
	@Autowired
	ItemImgRepository itemImgRepository;
	
	@Autowired
	FileService fileService;
	
	public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception
	{
		String oriImgName = itemImgFile.getOriginalFilename();
		String imgName = "";
		String imgUrl = "";
		
		if(!StringUtils.isEmpty(oriImgName)) // 파일 업로드
		{
			// 상품의 이미지를 등록 했다면 저장할 경로, 파일 이름, 파일을 파일의 바이트 배열을 파일 업로드 파라미터로 uploadFile 메소드 호출
			imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
			imgUrl = "/images/item" + imgName; // 저장한 상품 이미지를 불러올 경로 
		}
		
		// oriImgName = 업로드 했던 상품 이미지 파일의 원래 이름
		// imgName = 실제 로컬에 저장된 상품 이미지의 파일 이름
		// imgUrl = 업로드 결과 로컬에 저장된 상품 이미지 파일을 불러오는 경로
		itemImg.updateItemImg(oriImgName, imgName, imgUrl); // 상품 이미지 저장
		itemImgRepository.save(itemImg);
	}
	
	public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception
	{
		if(!itemImgFile.isEmpty()) // 상품 이미지를 수정한 경우
		{
			ItemImg savedItemImg = itemImgRepository.findById(itemImgId).orElseThrow(EntityNotFoundException::new); // 상품 이미지 아이디를 이용해 기존에 있던 상품 이미지 엔티티를 조회
			
			if(!StringUtils.isEmpty(savedItemImg.getImgName())) // 기존에 등록된 상품 이미지 파일이 있을 경우
			{
				fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName()); // 파일 삭제
			}
			
			String oriImgName = itemImgFile.getOriginalFilename();
			String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes()); // 업데이트한 상품 이미지 파일을 업로드
			
			String imgUrl = "/images/item/" + imgName;
			savedItemImg.updateItemImg(oriImgName, imgName, imgUrl); // 변경된 상품 이미지 정보를 세팅
		}
	}
}