package ca.mcgill.ecse211.project;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

/**
 * This class is used to define static resources in one place for easy access and to avoid cluttering the rest of the
 * codebase. All resources can be imported at once like this:
 * 
 * <p>
 * {@code import static ca.mcgill.ecse211.lab3.Resources.*;}
 * 
 * 
 * @author Xinyue Chen (260830761)
 * @author Zheng Yu Cui (260808525)
 */
public class Resources {

  /**
   * Wheel ratio
   */
  private static final double RATIO = 0.9976;
  
  /**
   * The wheel radius in centimeters.
   */
  public static final double WHEEL_RAD_LEFT = 2.130;
  public static final double WHEEL_RAD_RIGHT = 2.130;

  /**
   * The robot width in centimeters.
   */
  public static final double BASE_WIDTH = 10.625;

  /**
   * The speed at which the robot moves forward in degrees per second right.
   */
  public static final int FORWARD_SPEED_RIGHT = 150;
  
  /**
   * The speed at which the robot moves forward in degrees par second left.
   */
  public static final int FORWARD_SPEED_LEFT = (int) (FORWARD_SPEED_RIGHT * RATIO);

  /**
   * The speed at which the robot rotates in degrees per second.
   */
  public static final int ROTATE_SPEED_RIGHT = 100;
  /**
   * The speed at which the robot rotates in degrees per second.
   */
  public static final int ROTATE_SPEED_LEFT = (int)(ROTATE_SPEED_RIGHT * RATIO);
 
  /**
   * The motor acceleration in degrees per second squared.
   */
  public static final int ACCELERATION = 1500;

  /**
   * Timeout period in milliseconds.
   */
  public static final int TIMEOUT_PERIOD = 3000;

  /**
   * The tile size in centimeters. Note that 30.48 cm = 1 ft.
   */
  public static final double TILE_SIZE = 30.48;
  
  /**
   * Wall distance
   */
  public static final double WALL_DISTANCE = 50;
  
  /**
   * Error margin
   */
  public static final double MARGIN = 2;
  
  /**
   * Distance between sensor and wheel axle
   */
  public static final double LIGHT_SENSOR_DISTANCE = 13.5;
  
  /**
   * Maps for lab 5
   */
  public static final int[][] map1 = {{1,7},{3,4},{7,7},{7,4},{4,1}};
  public static final int[][] map2 = {{5,4},{1,7},{7,7},{7,4},{4,1}};
  public static final int[][] map3 = {{3,1},{7,4},{7,7},{1,7},{1,4}};
  public static final int[][] map4 = {{1,4},{3,7},{3,1},{7,4},{7,7}};
  
  public static int[][] curArray=null;
  
  public static final double SENSOR_OFFSET = 14.7;

  /**
   * The odometer.
   */
  public static Odometer odometer = Odometer.getOdometer();
  
  /**
   * The Movement
   */
  public static Movement movement;
  
  public static void setMovement(UltrasonicLocalizer usl) { Resources.movement = Movement.getMovement(usl); }
  
  /**
   * The left motor.
   */
  public static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.A);

  /**
   * The right motor.
   */
  public static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.D);
  
  /**
   * The LCD.
   */
  public static final TextLCD lcd = LocalEV3.get().getTextLCD();
  
  /**
   * The ultrasonic sensor for localization
   */
  public static final EV3UltrasonicSensor US_SENSOR_LOCAL = 
      new EV3UltrasonicSensor(LocalEV3.get().getPort("S1"));
  
  /**
   * The ultrasonic sensor for donut
   */
  public static final EV3UltrasonicSensor US_SENSOR_DONUT = 
      new EV3UltrasonicSensor(LocalEV3.get().getPort("S2"));
  
  /**
   * The light sensor for localizer
   */
  public static final EV3ColorSensor LIGHT_SENSOR_LOCAL =
      new EV3ColorSensor(LocalEV3.get().getPort("S3"));
  
  /**
   * The light sensor for donut
   */
  public static final EV3ColorSensor LIGHT_SENSOR_DONUT = 
      new EV3ColorSensor(LocalEV3.get().getPort("S4"));
  
}
