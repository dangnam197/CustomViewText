package com.example.customview.textdesign;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum LineType {
  TYPE_1_2_2, TYPE_2_2_1, TYPE_1;
  private static final List<LineType> VALUES =
    Collections.unmodifiableList(Arrays.asList(values()));
  private static final int SIZE = VALUES.size();
  private static final Random RANDOM = new Random();

  public static LineType randomLineType()  {
    return VALUES.get(RANDOM.nextInt(SIZE));
  }
}