package com.vibee.utils;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

public class CommonUtil {
  private static String localIp;

  public static String getLocalIp() {
    if (localIp != null && !localIp.isBlank()) {
      return localIp;
    }
    InetAddress result = null;
    try {
      int lowest = Integer.MAX_VALUE;
      for (Enumeration<NetworkInterface> nics = NetworkInterface.getNetworkInterfaces(); nics
          .hasMoreElements();) {
        NetworkInterface ifc = nics.nextElement();
        if (ifc.isUp()) {
          if (ifc.getIndex() < lowest || result == null) {
            lowest = ifc.getIndex();
          } else if (result != null) {
            continue;
          }
          for (Enumeration<InetAddress> addrs = ifc.getInetAddresses(); addrs.hasMoreElements();) {
            InetAddress address = addrs.nextElement();
            if (address instanceof Inet4Address && !address.isLoopbackAddress()) {
              result = address;
            }
          }
        }
      }
    } catch (IOException ex) {
    }
    if (result != null) {
      return result.getHostAddress();
    }
    try {
      return InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
    }
    return null;
  }

  public static boolean isEmptyOrNull(String value){
    if (value==null){
      return true;
    }
    if (value.equals("")){
      return true;
    }
    return false;
  }
  public static String getRemoteAddress(HttpServletRequest request) {
    String remoteAddr = "";
    if (request != null) {
      remoteAddr = request.getHeader("X-FORWARDED-FOR");
      if (remoteAddr == null || "".equals(remoteAddr)) {
        remoteAddr = request.getRemoteAddr();
      }
    }
    return remoteAddr;
  }

  public static String convertDateToStringddMMyyyy(Date date){
    if (date==null){
      return null;
    }
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    return sdf.format(date);
  }

  public static <T> String beanToString(T value){
    String response="";
    return response;
  }

  public static <T> T stringToBean(String value,Class<T> entity){
    T t=null;
    return t;
  }

  public static boolean isNullOrEmpty(String str) {
    return str == null || str.trim().isEmpty();
  }
}
