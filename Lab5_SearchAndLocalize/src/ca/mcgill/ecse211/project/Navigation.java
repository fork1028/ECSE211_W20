package ca.mcgill.ecse211.project;

import lejos.hardware.Sound;

public class Navigation {
  
  //Singleton navigation object
  private static Navigation navi;
  
  //Resources objects odometer and movement
  private static Odometer odometer;
  private static Movement movement;
  private static LightLocalizer lightLocalizer;
  private static UltrasonicLocalizer usLocalizer;
  
  //Thread Manipulation
  private static boolean exit;
  
  //constructor
  public Navigation(LightLocalizer ll, UltrasonicLocalizer usl) {
    Navigation.odometer = Resources.odometer;
    Navigation.lightLocalizer = ll;
    Navigation.usLocalizer = usl;
  }
  
  public void doMap(int[][] map) {
//    odometer.setXyt(1 * Resources.TILE_SIZE, 1 * Resources.TILE_SIZE, 0.0);
    this.travelTo(map[0][0], map[0][1]);
    this.travelTo(map[1][0], map[1][1]);
    lightLocalizer.localize2();
    this.travelTo(map[2][0], map[2][1]);
    this.travelTo(map[3][0], map[3][1]);
    lightLocalizer.localize2();
    this.travelTo(map[4][0], map[4][1]);
    lightLocalizer.localize2();
    Sound.beep();
    Sound.beep();
    Sound.beep();
  
  }
  
  private void travelTo(int x, int y) {
    Movement.travelTo(x * Resources.TILE_SIZE, y * Resources.TILE_SIZE, true);
    //lightLocalizer.localize();
  }
}