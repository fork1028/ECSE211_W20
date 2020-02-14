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

  // Static Resources:
  public static final Port usPort = LocalEV3.get().getPort("S1");

  /**
   * Main entry point
   * 
   * @param args not used
   */

  public static void main(String[] args) {

    int buttonChoice;

    // Start Odometer thread
    new Thread(Resources.odometer).start();


    // Making the localizer instances to call the methods
    UltrasonicLocalizer usLocalizer = new UltrasonicLocalizer();
    LightLocalizer lightLocalizer = new LightLocalizer(usLocalizer);
    Navigation navigation = new Navigation(lightLocalizer);

    // Wait for initial input to start and changing the LCD
    buttonChoice = waitingForInitialInput();

    // Start the display with the x, y, and theta
    new Thread(new Display()).start();

    if (buttonChoice == Button.ID_UP) {

      // Start the localizing the 0 degrees
      usLocalizer.localize();
      Sound.beep();
      lightLocalizer.moveToOrigin();

      // do maps
      //navigation.doMapOne();
      navigation.doBonusOne();
       // navigation.doMapTwo();
      // navigation.doMapThree();
      // navigation.doMapFour();

    }


    // Wait for the last button click to leave the main thread
    while (Button.waitForAnyPress() != Button.ID_ESCAPE);
    System.exit(0);

  }

  /**
   * Method to display the initial action to start scanning for zero and will not return until the right button is
   * pressed
   * 
   * @return the button id for enter
   */

  private static int waitingForInitialInput() {
    int click;
    Sound.beep();
    Display.showText(" Press the Center", " Button to Start", "  Scanning for  ", "   0 degrees   ");
    do {
      click = Button.waitForAnyPress();
    } while (click != Button.ID_LEFT && click != Button.ID_RIGHT && click != Button.ID_DOWN && click != Button.ID_UP);

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
