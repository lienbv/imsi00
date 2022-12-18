package com.vibee.utils;


import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;

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

  public static Date convertStringToDateddMMyyyy(String date){
    if (isEmptyOrNull(date)){
      return null;
    }
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    try {
      return sdf.parse(date);
    } catch (ParseException e) {
      return null;
    }
  }

  public static <T> String beanToString(T value) {
    if (value == null) {
      return null;
    }
    Class<?> clazz = value.getClass();
    if (clazz == int.class || clazz == Integer.class) {
      return "" + value;
    } else if (clazz == String.class) {
      return (String) value;
    } else if (clazz == long.class || clazz == Long.class) {
      return "" + value;
    } else {
      ObjectMapper mapper = new ObjectMapper();
      mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
      String jsonString = "";
      try {
        jsonString = mapper.writeValueAsString(value);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
        jsonString = "Can't build json from object";
      }
      return jsonString;
    }
  }

  public static <T> T stringToBean(String str, Class<T> clazz) {
    if (str == null || str.length() <= 0 || clazz == null) {
      return null;
    }
    if (clazz == int.class || clazz == Integer.class) {
      return (T) Integer.valueOf(str);
    } else if (clazz == String.class) {
      return (T) str;
    } else if (clazz == long.class || clazz == Long.class) {
      return (T) Long.valueOf(str);
    } else {
      ObjectMapper mapper = new ObjectMapper();
      try {
        return mapper.readValue(str, clazz);
      } catch (IOException e) {
        return null;
      }
    }
  }

  public static boolean isNullOrEmpty(String str) {
    return str == null || str.trim().isEmpty();
  }

  public static <T> String listToJson(List<T> list) {
    if (list == null || list.isEmpty()) {
      // return "[]";
      return null;
    }
    String json = "[";
    for (T item : list) {
      json = json + beanToString(item) + ",";
    }
    json = json.substring(0, json.length() - 1) + "]";
    return json;
  }

  public static <T> List<T> jsonToList(String json, Class<T> clazz) throws IOException {
    if (CommonUtil.isNullOrEmpty(json)) {
      return new ArrayList<>();
    }
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
    return mapper.readValue(json, listType);
  }
}
