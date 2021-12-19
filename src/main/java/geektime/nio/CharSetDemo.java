package geektime.nio;

import java.nio.charset.*;
import java.util.*;

public class CharSetDemo {
  public static void main(String[] args) {
    Charset charset=Charset.forName("GB18030");
    System.out.println("Default Charset: " + charset.defaultCharset());

    SortedMap<String,Charset> sortedMap= charset.availableCharsets();
    Set<Map.Entry<String,Charset>> entrySet= sortedMap.entrySet();
    for (Map.Entry<String, Charset> entry : entrySet) {
      System.out.println(entry.getKey()+":"+entry.getValue());
    }
  }
}
