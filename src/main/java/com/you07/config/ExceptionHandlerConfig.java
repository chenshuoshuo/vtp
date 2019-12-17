package com.you07.config;//package com.awl.config;

import com.you07.util.message.MessageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @author Egan
 * @date 2019/12/17 9:57
 * 异常处理器
 */
@RestControllerAdvice
public class ExceptionHandlerConfig {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);


	@ExceptionHandler(Exception.class)
	public MessageBean handlerException(Exception e) {
        logger.error("未知异常", e);
		return MessageBean.error(e.getMessage());
	}

}
