package ca.mcgill.ecse211.project;

import lejos.hardware.*;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.Button;
import static ca.mcgill.ecse211.project.Resources.*;

/**
 * The Main class has the main thread that first initializes the difference sensors and redirects the threads
 * 
 * @author Xinyue Chen (260830761)
 * @author Zheng Yu Cui (260808525)
 */

public class Main {
  
  //Static Resources:
  public static final Port usPort = LocalEV3.get().getPort("S1");
  
  /**
   * Main entry point
   * 
   * @param args not used
   */
  
  public static void main(String[] args) {
    

    
    //Start Odometer thread
    new Thread(Resources.odometer).start();
    
    //Making the localizer instances to call the methods
    UltrasonicLocalizer usLocalizer_Local = new UltrasonicLocalizer(US_SENSOR_LOCAL);
    UltrasonicLocalizer usLocalizer_Donut = new UltrasonicLocalizer(US_SENSOR_DONUT);
    
    Resources.setMovement(usLocalizer_Donut);
    
    LightLocalizer lightLocalizer_Local = new LightLocalizer(usLocalizer_Local);
    
    Navigation navigation = new Navigation(lightLocalizer_Local, usLocalizer_Local);
    
    Sound.beep();
    
    Button.waitForAnyPress();
//    lightLocalizer_Local.localize();
//      Movement.turnBy(720);
//      
//      Button.waitForAnyPress();
//      
 //     odometer.setXyt(0.0, 0.0, 0.0);
//        Movement.travelTo(0.0, 200.0, true);
        System.out.println("#: " + (Movement.counter()-1));
//      Movement.turnTo(180);
//        Movement.goForward(TILE_SIZE);
    
    //Start the localizing the 0 degrees
    //usLocalizer_Donut.reorient();
    //Sound.beep();
    //usLocalizer_Donut.moveToPosition();
    //lightLocalizer_Local.localize2();
    //Wait for initial input to start and changing the LCD 
    chooseMap();
    navigation.doMap(curArray);
    while (Button.waitForAnyPress() != Button.ID_ESCAPE);
    System.out.println("Done main");
    System.exit(0);
  }

  
  
  
//  
//  }
//     else if (buttonChoice == Button.ID_ENTER) {
//      Movement.goForward(200);
////      Movement.turnTo(45);
////      lightLocalizer_Local.localize();
//      while(Button.waitForAnyPress() == Button.ID_ENTER) {
//        Movement.turnTo(45);
//        lightLocalizer_Local.localize();
//      }
//      
//    }
   
    
    
    //Playing the music to say that it succeeded
    //Music.twokay();
    
  
//  /**
//   * Method to display the initial action to start scanning for zero and will not return until the right button is pressed
//   * 
//   * @return the button id for enter
//   */
//  private static int waitingForInitialInput() {
//    int click;
//    Sound.beep();
//    Display.showText(
//        "Up --------> Map1 ",
//        "Down ------> Map2 ",
//        "Right -----> Map3 ",
//        "Left ------> Map4 ");
//    do {
//      click = Button.waitForAnyPress();
//    } while (click != Button.ID_LEFT && click != Button.ID_RIGHT && click != Button.ID_DOWN && click != Button.ID_UP && click != Button.ID_ENTER);
//    
//    return click;
//  }
  
  public static void chooseMap() {
    int buttonChoice;
    int[][] MapChoice = null;
    Display.showText("<   Map Run Options >",
        "<                    >",
        "<    Up    : Run 1   >",
        "<    Down  : Run 2   >",
        "<    Left  : Run 3   >",
        "<    Right : Run 4   >");     

    buttonChoice = Button.waitForAnyPress();
      switch(buttonChoice) {
        case Button.ID_UP: 
          MapChoice = map1;
          break;
        case Button.ID_DOWN:
          MapChoice = map2;
          break;
        case Button.ID_LEFT:
          MapChoice = map3;
          break;
        case Button.ID_RIGHT:
          MapChoice = map4;
          break;
      }
      
      lcd.clear();
    curArray = MapChoice;   
  }
  
  
  /**
   * Method stop the main thread, only used in Display
   * 
   * @param duration is the duration in ms of how long should the thread stop
   */
  
  public static void sleepFor(long duration) {
    try {
      Thread.sleep(duration);
    } catch (InterruptedException e) {
      // There is nothing to be done here
    }
  }
}