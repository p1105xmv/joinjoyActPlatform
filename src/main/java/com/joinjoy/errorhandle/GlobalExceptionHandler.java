package com.joinjoy.errorhandle;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value=MaxUploadSizeExceededException.class)
	public String imageMaxSizeHandler(Model model) {
		model.addAttribute("sizeError","檔案過大，請重新上傳 😀");
		
		return "photos/uploadPage";
	}

}
