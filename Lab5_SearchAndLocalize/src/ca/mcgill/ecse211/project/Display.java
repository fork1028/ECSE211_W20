package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.*;

import java.text.DecimalFormat;

/**
 * This class is used to display the content of the odometer variables (x, y, theta).
 * 
 * @author Xinyue Chen (260830761)
 * @author Zheng Yu Cui (260808525)
 */
public class Display {

  private double[] position;
  private static final long DISPLAY_PERIOD = 25;

  private long timeout = Long.MAX_VALUE;

  /**
   * Shows the text on the LCD, line by line.
   * 
   * @param strings comma-separated list of strings, one per line
   */
  public static void showText(String... strings) {
    lcd.clear();
    lcd.clear();
    for (int i = 0; i < strings.length; i++) {
      lcd.drawString(strings[i], 0, i);
    }
  }

}
