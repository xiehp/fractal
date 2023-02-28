package xie.fractal.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import xie.utils.NullUtils;

@Component
public class TimeInterceptor extends BaseInterceptor {

	public static final ThreadLocal<Long> threadLocalTime = new ThreadLocal<>();

	/**
	 * 拦截前处理
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		if (request.getRequestURL().toString().contains("/api/system/visit/visitEvent")) {
			logger.debug("Start Proess: {}", request.getRequestURL());
		} else {
			logger.info("Start Proess: {}", request.getRequestURL());
		}
		threadLocalTime.set(System.currentTimeMillis());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
		// System.out.println(modelAndView);
	}

	/**
	 * 全部完成后处理
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
		Long preTime = NullUtils.ifNull(threadLocalTime.get(), System.currentTimeMillis() - 1);
		Long process_time = System.currentTimeMillis() - preTime;
		MDC.put("process_time", String.valueOf(process_time));
		if (request.getRequestURL().toString().contains("/api/system/visit/visitEvent")) {
			String orgin_url = MDC.get("origin_url");
			String visit_url = MDC.get("visit_url");
			if (visit_url != null && visit_url.equalsIgnoreCase(orgin_url)) {
				logger.info("End Process: {} -> Post Time: {} -> From: {}", request.getRequestURL(), process_time, orgin_url);
			} else {
				logger.info("End Process: {} -> Post Time: {} -> From: {}, To: {}", request.getRequestURL(), process_time, orgin_url, visit_url);
			}
		} else {
			logger.info("End Process: {} -> Post Time: {}", request.getRequestURL(), process_time);
		}
		threadLocalTime.remove();
	}
}