package com.shop.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shop.dto.OrderDto;
import com.shop.dto.OrderHistDto;
import com.shop.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class OrderController 
{
	@Autowired
	private OrderService orderService;
	
	// @ResponseBody 자바 객체를 HTTP 요청의 body로 전달
	// @RequestBody HTTP 요청의 본문 body에 담긴 내용을 자바 객체로 전달
	@PostMapping("/order")
	public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult, Principal principal)
	{
		if(bindingResult.hasErrors()) // 주문 정보를 받는 orderDto 객체에 데이터 바인딩 시 에러가 있는지 검사
		{
			StringBuilder sb = new StringBuilder();
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();
			for(FieldError fieldError : fieldErrors)
			{
				sb.append(fieldError.getDefaultMessage());
			}
			return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
		}
		
		// 현재 로그인 유저 정보를 얻기 위해 principal 객체에서 현재 로그인한 회원의 이메일 정보를 조회
		String email = principal.getName(); 
		Long orderId;
		try
		{
			// 화면으로부터 넘어오는 주문 정보와 회원의 이메일 정보를 이용하여 주문 로직을 호출
			orderId = orderService.order(orderDto, email);
		}
		catch(Exception e)
		{
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Long>(orderId, HttpStatus.OK);
	}
	
	@GetMapping(value = {"/orders", "/orders/{page}"})
	public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model)
	{
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4); // 한 번에 가지고 올 주문의 개수를 4개로 설정
		Page<OrderHistDto> ordersHistDtoList = orderService.getOrderList(principal.getName(), pageable); // 현재 로그인한 회원의 이메일과 페이징 객체를 파리미터로 전달
		
		model.addAttribute("orders", ordersHistDtoList);
		model.addAttribute("page", pageable.getPageNumber());
		model.addAttribute("maxPage", 5);
		
		return "order/orderHist";
	}
	
	@PostMapping("/order/{orderId}/cancel")
	public @ResponseBody ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId, Principal principal)
	{
		if(!orderService.validateOrder(orderId, principal.getName())) // 자바스크립트에서 취소할 주문 번호는 조작이 가능하므로 다른 사람의 주문을 취소하지 못하도록 취소 권한을 검사
		{
			return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
		}
		
		orderService.cancelOrder(orderId); // 주문 취소 로직 호출
		return new ResponseEntity<Long>(orderId, HttpStatus.OK);
	}
}