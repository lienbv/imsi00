package com.vibee.model.request;

import lombok.Data;

@Data
public class RequestContext {
  private String clientMessageId;
  private String clientTime;
  private long receivedTime;
  private String path;
  private String clientIp;
  private String clientName;
}
