package com.vibee.model.request;

import org.springframework.stereotype.Component;

@Component
public class RequestContextHolder {
  private static final ThreadLocal<RequestContext> holder = new ThreadLocal<>();

  public static void set(RequestContext context) {
    holder.set(context);
  }

  public static RequestContext get() {
    return holder.get();
  }

  public static void remove() {
    holder.remove();
  }
}
