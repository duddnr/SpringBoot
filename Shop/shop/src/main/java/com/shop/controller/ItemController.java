package com.shop.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.service.ItemService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ItemController 
{
	@Autowired
	ItemService itemService;
	
	@GetMapping(value = "/admin/item/new")
	public String itemForm(Model model)
	{
		model.addAttribute("itemFormDto", new ItemFormDto());
		return "/item/itemForm";
	}
	
	/*
	 * 작동원리
	 * 
	 * itemController
	 *  - /item/itemForm.html에서 itemFormDto 데이터를 받아옴
	 *  - itemService에서 saveItem 메서드를 호출 해 itemFormDto 데이터와 itemImgFileList 이미지 파일 리스트를 파라미터로 넘김
	 *  
	 *  itemService
	 *  - itemController에서 넘긴 itemFormDto 객체를 Item 엔티티 객체로 변환 후 데이터베이스에 저장
	 *  - itemImgFileList에 있는 이미지 개수 만큼 반복문을 돌림
	 *  - 반복문에서 itemImg 엔티티를 생성 후 연관 관계인 item 엔티티 객체를 저장
	 *  - 반복문에서 첫번째 이미지일 경우 대표 상품 이미지 결정 하고 나머지는 false
	 *  - 반복문에서 item 엔티티 객체 설정, 첫번째 이미지 대표 이미지 설정 후 ItemImgService에서 saveItemImg 메서드를 호출
	 *  - saveItemImg 메서드 파라미터를 itemImg와 itemFileList의 i번째 이미지를 파라미터로 넘김
	 *  - 반복문이 끝나면 Item의 아이디를 반환
	 *  
	 *  ItemImgService
	 *  - 전달 받은 itemFileList의 i번째 원본 파일 이름을 저장
	 *  - FileService에서 uploadFile 메서드를 호출
	 *  - uploadFile 메서드 파라미터를 application에서 설정한 프로퍼티, 원본 파일 이름, itemFileList의 i번재 파일 바이트를 파라미터로 넘김
	 *  - ItemImg에서 updateItemImg 메서드 호출
	 *  - updateItemImg 메서드 파라미터를 원본 파일 이미지 이름, 저장될 파일 이름, 로컬에 저장된 상품 이미지 파일을 불러오는 경로 파라미터로 넘김
	 *  - ItemImg를 데이터베이스에 저장 -최종 단계-
	 *  - 다시 itemService의 반복문으로 돌아감
	 *  
	 *  FileService
	 *  - 중복 방지를 위해 UUID 클래스를 사용
	 *  - 원본 파일 이름의 확장자를 받아옴
	 *  - UUID로 랜덤으로 생성한 이름과 원본 파일 이름의 확장자를 조합하여 저장 (저장될 파일 이름)
	 *  - application에서 설정한 프로퍼티와 위에서 조합한 이름과 조합하여 저장  (경로 + 저장될 파일 이름)
	 *  - 파일이 저장될 위치와 파일 이름을 넘겨 파일 출력 스트림을 생성
	 *  - 파일 출력 스트림에 입력 후 close
	 *  - 저장될 파일 이름을 반환
	 */
	@PostMapping("/admin/item/new")
	public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model, @RequestParam(value="itemImgFile", required=false) List<MultipartFile> itemImgFileList)
	{
		if(bindingResult.hasErrors()) { return "item/itemForm"; }
		
		// 상품 등록 시 첫번째 이미지가 없다면 에러 메시지와 함께 상품 등록 페이지로 전환 (메인 페이지에서 보여줄 상품 이미지로 사용하기 위해 필수)
		if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null)
		{
			model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
			return "item/itemForm";
		}
		
		try
		{
			itemService.saveItem(itemFormDto, itemImgFileList);
		}
		catch(Exception e)
		{
			model.addAttribute("errorMessage", "상품 등록 중 에러가 발행하였습니다.");
			return "item/itemForm";
		}
		
		return "redirect:/";
	}
	
	/*
	 * 작동 원리
	 * 
	 * ItemController
	 * - @PathVariable 어노테이션으로 주소의 itemId 값을 가져옴
	 * - itemService의 getItemDtl 메서드 호출
	 * - 반환한 ItemFormDto를 모델에 담음
	 * 
	 * ItemService
	 * - 상품 아이디를 가진 이미지 엔티티 객체를 가져옴
	 * - 가져온 이미지 엔티티 객체를 Dto로 변환시켜 itemImgDtoList에 넣음
	 * - 상품 아이디를 이용해 상품 엔티티 객체를 가져옴
	 * - 상품 엔티티 객체를 Dto로 변환
	 * - ItemFormDto에 있는 itemImgDtoList 변수에 이미지 엔티티 객체를 변환하여 집어넣은 itemImgDtoList를 넘김
	 * - ItemFormDto를 반환 
	 */
	@GetMapping(value = "/admin/item/{itemId}")
	public String itemDtl(@PathVariable("itemId") Long itemId, Model model)
	{
		try
		{
			ItemFormDto itemFormDto = itemService.getItemDtl(itemId); // 조회한 상품 데이터를 모델에 담기
			model.addAttribute("itemFormDto", itemFormDto);
		}
		catch(EntityNotFoundException e) // 상품 엔티티가 존재하지 않는 경우
		{
			model.addAttribute("errorMessage", " 존재하지 않는 상품 입니다.");
			model.addAttribute("itemFormDto", new ItemFormDto());
			return "item/itemForm";
		}
		return "item/itemForm";
	}
	
	/*
	 * 작동 원리
	 * 
	 * ItemController
	 * - itemService의 updateItem 메서드 호출
	 * - 파라미터로 ItemFormDto와 이미지 파일 리스트를 넘김
	 * 
	 * ItemService
	 * - ItemFormDto에 id를 이용하여 상품 엔티티 객체를 조회
	 * - 상품 엔티티 객체에 updateItem 메서드를 호출하여 ItemFormDto의 데이터를 덮어씌움
	 * - itemForm.html에서 상품 이미지 수정 시 itemImgIds에 있는 이미지 아이디를 가져옴
	 * - ItemImgService의 updateItemImg 메서드 호출
	 * - 파라미터로 수정된 이미지 아이디, 새로 등록한 이미지 파일을 넘김
	 * 
	 * ItemImgService
	 * - 이미지 아이디를 이용해 기존에 있던 이미지 엔티티 객체를 조회
	 * - 기존에 등록된 이미지가 있을 경우 삭제함
	 * - fileService의 uploadFile 메소드 호출
	 * - 기존에 있던 이미지 엔티티 객체에서 updateItemImg 메서드 호출
	 * - 파라미터로 새로 등록할 이미지 원본 이름, 새로 업도르할 파일 이름, 경로를 넘겨줌
	 */
	@PostMapping("/admin/item/{itemId}")
	public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model)
	{
		if(bindingResult.hasErrors())
		{
			return "item/itemForm";
		}
		
		if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null)
		{
			model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
		}
		
		try
		{
			itemService.updateItem(itemFormDto, itemImgFileList);
		}
		catch(Exception e)
		{
			model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
			return "item/itemForm";
		}
		return "redirect:/";
	}
	
	@GetMapping(value = {"/admin/items", "/admin/items/{page}"}) // 상품 관리 화면 진입 시 URL에 페이지 번호가 없는 경우, 페이지 번호가 있는 경우 2가지를 매핑
	public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model)
	{
		/*
		 * 페이징을 위해 PageRequest.of 메소드를 통해 Pageable 객체를 생성
		 * 첫번째 파라미터는 조회할 페이지 번호, 두번째 파라미터는 한 번에 가지고 올 데이터 수
		 * URL 경로에 페이지 번호가 있으면 해당 페이지를 조회하고, 페이지 번호가 없으면 0페이지를 조회하도록함
		 */
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
		
		// 조회 조건과 페이징 정보를 파리미터로 넘겨 Page<Item> 객체를 반환
		Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
		System.out.println(items.getTotalPages());
		
		model.addAttribute("items", items); // 조회한 상품 데이터 및 페이징 정보를 전달
		model.addAttribute("itemSearchDto", itemSearchDto); // 페이지 전환 시 기존 검색 조건을 유지한 채 이동할 수 있도록 다시 전달
		model.addAttribute("maxPage", 5); // 상품 관리 메뉴 하단에 보여줄 페이지 번호의 최대 개수
		return "item/itemMng";
	}
	
	@GetMapping(value = "/item/{itemId}")
	public String itemDtl(Model model, @PathVariable("itemId") Long itemId)
	{
		ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
		model.addAttribute("item", itemFormDto);
		return "item/itemDtl";
	}
}