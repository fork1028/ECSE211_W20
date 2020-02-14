package ca.mcgill.ecse211.project;

import lejos.hardware.*;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.Button;

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
    
    int buttonChoice;
    int buttonChoiceMoving;
    
    //Start Odometer thread
    new Thread(Resources.odometer).start();
    
    
    //Making the localizer instances to call the methods
    UltrasonicLocalizer usLocalizer = new UltrasonicLocalizer();
    LightLocalizer lightLocalizer = new LightLocalizer();
    
    //Wait for initial input to start and changing the LCD 
    buttonChoice = waitingForInitialInput();
    
    if (buttonChoice == Button.ID_ENTER) {
      
      //Start the display with the x, y, and theta
      new Thread(new Display()).start();
      //Start the localizing the 0 degrees
      usLocalizer.localize();
      
    }
    
    //Wait for second choice for the moving to (1, 1)
    buttonChoiceMoving = waitingForMovingType();
    
    
    if (buttonChoiceMoving == Button.ID_RIGHT) {
      
      //Right button for light localizing
      lightLocalizer.moveToOrigin();
      
    } else if (buttonChoiceMoving == Button.ID_LEFT) {
      
      //Left for US localizing Starting for the new display
      new Thread(new Display()).start();
      usLocalizer.moveToOrigin();
      
    }
    
    //Playing the music to say that it succeeded
    Music.twokay();
    
    //Wait for the last button click to leave the main thread
    while (Button.waitForAnyPress() != Button.ID_ESCAPE);
    System.exit(0);
    
  }
  
  /**
   * Method to display the initial action to start scanning for zero and will not return until the right button is pressed
   * 
   * @return the button id for enter
   */
  
  private static int waitingForInitialInput() {
    int click;
    Sound.beep();
    Display.showText(
        " Press the Center",
        " Button to Start",
        "  Scanning for  ",
        "   0 degrees   ");
    do {
      click = Button.waitForAnyPress();
    } while (click != Button.ID_ENTER);
    
    return click;
  }
  
  /**
   * Method to display the decision for the moving type to the (1,1) point and will not return until the right button is pressed
   * 
   * @return the button id that was chosen
   */
  
  private static int waitingForMovingType() {
    int click;
    Display.showText(
        " < Left | Right >",
        "        |        ",
        "   US   | Light  ",
        " Sensor | Sensor ",
        "        |        ");
    
    do {
      click = Button.waitForAnyPress();
    } while (click != Button.ID_LEFT && click != Button.ID_RIGHT);
    
    return click;
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
