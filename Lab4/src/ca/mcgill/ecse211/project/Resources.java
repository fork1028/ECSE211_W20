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
   * The wheel radius in centimeters.
   */
  public static final double WHEEL_RAD_LEFT = 2.25;
  public static final double WHEEL_RAD_RIGHT = 2.25;

  /**
   * The robot width in centimeters.
   */
  public static final double BASE_WIDTH = 10.4;

  /**
   * The speed at which the robot moves forward in degrees per second.
   */
  public static final int FORWARD_SPEED = 100;

  /**
   * The speed at which the robot rotates in degrees per second.
   */
  public static final int ROTATE_SPEED = 60;

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
  public static final double LIGHT_SENSOR_DISTANCE = 3.60;

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
   * The ultrasonic sensor.
   */
  public static final EV3UltrasonicSensor US_SENSOR = new EV3UltrasonicSensor(LocalEV3.get().getPort("S1"));

  /**
   * The odometer.
   */
  public static Odometer odometer = Odometer.getOdometer();

  /**
   * The Movement
   */
  public static Movement movement = Movement.getMovement();

  /**
   * The light sensor
   */
  public static final EV3ColorSensor LIGHT_SENSOR = new EV3ColorSensor(LocalEV3.get().getPort("S4"));

  /**
   * Value of two pi
   */
  public static final double twopi = 2 * Math.PI;

}
