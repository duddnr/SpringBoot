package com.shop.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.extern.java.Log;

@Log
@Service
public class FileService 
{
	public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception
	{
		UUID uuid = UUID.randomUUID(); // UUID는 서로 다른 개체들을 구별하기 위해 이름 부여 시 사용
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		
		String savedFileName = uuid.toString() + extension; // UUID로 받은 값과 원래 파일의 이름의 확장자를 조합하여 저장될 파일을 생성
		String fileUploadFullUrl = uploadPath + "/" + savedFileName;
		
		FileOutputStream fos = new FileOutputStream(fileUploadFullUrl); // 바이트 단위의 출력을 내보내는 클래스 생성자로 파일이 저장될 위치와 파일 이름을 넘겨 파일에 쓸 파일 출력 스트림을 생성
		
		fos.write(fileData); // 파일 출력 스트림에 입력
		fos.close();
		
		return savedFileName; // 업로드된 파일 이름을 반환
	}
	
	public void deleteFile(String filePath) throws Exception
	{
		File deleteFile = new File(filePath); // 파일이 저장된 경로를 이용하여 파일 객체 생성
		
		if(deleteFile.exists()) // 해당 파일이 존재하면 파일 삭제
		{
			deleteFile.delete();
			log.info("파일을 삭제하였습니다.");
		}
		else
		{
			log.info("파일이 존재하지 않습니다.");
		}
	}
}