package top.plgxs.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class SinosoftException implements HandlerExceptionResolver {

	private static final Logger log = LoggerFactory.getLogger(SinosoftException.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		
		//输出异常
		log.debug("【统一异常处理】", ex);
		
		return null;
	}

}
