/*
 *  Copyright HNA©2017. All rights reserved.
 */
package com.question.admin.config.upload.aop;

import java.lang.reflect.Method;

import com.question.admin.config.upload.annotation.FileSlotDisabled;
import com.question.admin.config.upload.entity.FileSlot;
import com.question.admin.config.upload.support.FileResolver;
import com.question.admin.config.upload.support.MultipartContextHolder;
import com.question.admin.config.upload.support.MultipartRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Order(2)
@Component
@Slf4j
public class MultipartAspect {

	@Value("${file.multipart.autoSaveEnabled:true}")
	private Boolean autoSaveEnabled = true;

	@Autowired
    FileResolver fileResolver;

	@Before(value = "@annotation(org.springframework.web.bind.annotation.PostMapping)")
	public void beforAdvice(JoinPoint joinPoint) throws Throwable {
		if (!this.autoSaveEnabled) {
			return;
		}

		// 如果有@FileSlotDisabled注解， 不继续处理。
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		FileSlotDisabled fileSlotDisabledAnnotation = method.getAnnotation(FileSlotDisabled.class);
		if (fileSlotDisabledAnnotation != null) {
			return;
		}

		Object[] args = joinPoint.getArgs();
		if (args == null) {
			return;
		}

		Object target = joinPoint.getTarget();
		if (target instanceof ErrorController) {
			return;
		}

		//
		MultipartRequestWrapper requestWrapper = MultipartContextHolder.currentRequestAttributes();
		if (requestWrapper == null) {
			return;
		}

		if (!requestWrapper.isMultipartRequest()) {
			return;
		}

		for (Object arg : args) {
			if (arg instanceof FileSlot) {
				fileResolver.resolveFile(requestWrapper, (FileSlot) arg);
			}
		}
	}

}
