package com.vibee.config.interceptor;

import com.vibee.enums.HeaderEnum;
import com.vibee.model.request.RequestContext;
import com.vibee.model.request.RequestContextHolder;
import com.vibee.utils.CommonUtil;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestHeaderFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    RequestContext ctx = new RequestContext();
    RequestContextHolder.set(ctx);
    String clientMessageId = request.getHeader(HeaderEnum.CLIENT_MESSAGE_ID.getLabel());
    if (CommonUtil.isEmptyOrNull(clientMessageId)) {
      clientMessageId = UUID.randomUUID().toString();
    }
    ctx.setClientMessageId(clientMessageId);
    String clientTimeStr = request.getHeader(HeaderEnum.CLIENT_TIME.getLabel());
    ctx.setClientTime(clientTimeStr);
    ctx.setReceivedTime(System.currentTimeMillis());
    String clientIp = CommonUtil.getRemoteAddress(request);
    ctx.setClientIp(clientIp);
    String path = request.getRequestURI();
    ctx.setPath(path);

    ThreadContext.put("clientIp", clientIp);
    ThreadContext.put("clientMessageId", clientMessageId);
    ThreadContext.put("clientTime", clientTimeStr);
    ThreadContext.put("path", path);

    filterChain.doFilter(request, response);
  }

}
