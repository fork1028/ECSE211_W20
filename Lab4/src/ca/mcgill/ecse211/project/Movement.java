package ca.mcgill.ecse211.project;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import ca.mcgill.ecse211.project.Resources.*;

/**
 * The Movement class moves the robot in the grid according to the values in the odometer
 * 
 * @author Xinyue Chen (260830761)
 * @author Zheng Yu Cui (260808525)
 */
public class Movement {

  // singleton
  private static Movement movement;

  // Different speeds for the robot's rotation
  final static int FAST = 200;
  final static int SLOW = 100;

  // The angle error for when you turn to the right angle otherwise it shimmies
  final static double DEG_ERR = 2.0;

  // Private object that reference to the odometer and motors
  private Odometer odometer;
  private EV3LargeRegulatedMotor leftMotor, rightMotor;

  // If the movement is done
  private boolean isDone = false;

  /**
   * Constructor that initializes the odometer and leftmotors using the objects in the resources
   * 
   */
  private Movement() {

    this.odometer = Resources.odometer;
    this.leftMotor = Resources.leftMotor;
    this.rightMotor = Resources.rightMotor;

    this.leftMotor.setAcceleration(Resources.ACCELERATION);
    this.rightMotor.setAcceleration(Resources.ACCELERATION);

    this.leftMotor.setSpeed(Resources.ROTATE_SPEED);
    this.rightMotor.setSpeed(Resources.ROTATE_SPEED);

  }

  /**
   * Returns the Movement Object. Use this method to obtain an instance of Movement.
   * 
   * @return the Movement Object
   */
  public static synchronized Movement getMovement() {
    if (movement == null) {
      movement = new Movement();
    }

    return movement;
  }

  /**
   * Rotate the robot clockwise
   */
  public void rotateClockwise() {
    this.leftMotor.setSpeed(Resources.ROTATE_SPEED);
    this.rightMotor.setSpeed(Resources.ROTATE_SPEED);
    this.leftMotor.forward();
    this.rightMotor.backward();
  }

  /**
   * Rotate the robot counter clockwise
   */
  public void rotateCounterClockwise() {
    this.leftMotor.setSpeed(Resources.ROTATE_SPEED);
    this.rightMotor.setSpeed(Resources.ROTATE_SPEED);
    this.leftMotor.backward();
    this.rightMotor.forward();
  }

  /**
   * Move the robot forward until forever
   */
  public void goForward() {
    this.leftMotor.setSpeed(Resources.FORWARD_SPEED);
    this.rightMotor.setSpeed(Resources.FORWARD_SPEED);
    this.leftMotor.forward();
    this.rightMotor.forward();
  }

  /**
   * Functions to set the motor speeds jointly
   * 
   * @param lSpd left motor speed
   * @param rSpd right motor speed
   */
  public void setSpeeds(float lSpd, float rSpd) {
    this.leftMotor.setSpeed(lSpd);
    this.rightMotor.setSpeed(rSpd);
  }

  public void setSpeeds(int lSpd, int rSpd) {
    this.leftMotor.setSpeed(lSpd);
    this.rightMotor.setSpeed(rSpd);
  }

  /**
   * Function to set the acceleration
   * 
   * @param acc
   */
  public void setAccelerations(int acc) {
    this.leftMotor.setAcceleration(acc);
    this.rightMotor.setAcceleration(acc);
  }

  /**
   * TravelTo function to move the robot to the coordinates x and y in the coordinate system set by the odometer
   * 
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public void travelTo(double x, double y) {



    // Difference from current point to the destination point
    double angle; // angle between the current point and destination
    double distance; // distance between current point and destination

    // Math to calculate the angle using trigonometry and Pytharian theorem
    angle = Math.toDegrees(Math.atan2(x - odometer.getX(), y - odometer.getY()));
    distance = Math.hypot((x - odometer.getX()), (y - odometer.getY()));

    // Turn to the correct angle in the direction of the destination and move the right distance to the destination
    this.turnTo(angle);

    // set speeds
    setSpeeds(100, 100);

    this.goForward(distance);

    //this.stopMotors();
  }

  /**
   * Travelto for the last point on the map
   * 
   * @param x horizontal coordinate
   * @param y vertical coordinate
   */
  public void travelToLightLocalization(double x, double y) {



    // Difference from current point to the destination point
    double angle; // angle between the current point and destination
    double distance; // distance between current point and destination

    // Math to calculate the angle using trigonometry and Pytharian theorem
    angle = Math.toDegrees(Math.atan2(x - odometer.getX(), y - odometer.getY()));
    distance = Math.hypot((x - odometer.getX()), (y - odometer.getY()));

    // Turn to the correct angle in the direction of the destination and move the right distance to the destination
    this.turnTo(angle);

    // set speeds
    setSpeeds(100, 100);

    // move the robot 2cm+distance forward
    this.goForward(distance - 10);

    //this.stopMotors();
  }

  /**
   * Travel to for the normal points in navigation
   * 
   * @param x horizontal coordinate
   * @param y vertical coordinate
   */
  public void travelToNavigation(double x, double y) {



    // Difference from current point to the destination point
    double angle; // angle between the current point and destination
    double distance; // distance between current point and destination

    // Math to calculate the angle using trigonometry and Pytharian theorem
    angle = Math.toDegrees(Math.atan2(x - odometer.getX(), y - odometer.getY()));
    distance = Math.hypot((x - odometer.getX()), (y - odometer.getY()));

    // Turn to the correct angle in the direction of the destination and move the right distance to the destination
    this.turnTo(angle);

    // set speeds
    setSpeeds(100, 100);

    // move the robot 2cm+distance forward
    this.goForward(distance);

    //this.stopMotors();
  }

  /**
   * Method for the robot to turn to the angle theta
   * 
   * @param theta
   */
  public void turnTo(double theta) {

    // calculate the minimum angle
    double angle = getMinAngle(theta - odometer.getTheta());

    // rotate wheels
    leftMotor.rotate(convertAngle(angle, 0), true);
    rightMotor.rotate(-convertAngle(angle, 1), false);
  }

  /**
   * Method for calculating the minimum angle
   * 
   * @param angle
   * @return
   */
  public double getMinAngle(double angle) {
    // if the angle is larger than 180 degrees, minus 360
    if (angle > 180) {
      angle -= 2 * 180;
    }
    // if the angle is smaller than 180 degrees, plus 360
    else if (angle < -180) {
      angle += 2 * 180;
    }
    return angle;
  }


  /**
   * TurntTo function turns the robot to the angle from the coordinate system set by the odometer
   * 
   * @param angle the final angle according to the coordinate system
   * @param stop boolean to check if it should stop after rotating
   */
  public void turnTo(double angle, boolean stop) {

    // Angle difference between the current angle and the wanted angle
    double error = angle - this.odometer.getTheta();


    if (error <= -180.0) {
      // A
      this.turnBy(error + 360);
    } else if (error < 0.0) {
      // B
      this.turnBy(error);
    } else if (error >= 180.0) {
      // C
      this.turnBy(error - 360);
    } else if (error > 0.0) {
      // D
      this.turnBy(error);
    }

  }

  /**
   * Go forward a set distance in cm
   * 
   * @param distance distance in cm
   */
  public void goForward(double distance) {
    leftMotor.rotate(convertDistance(distance, 0), true);
    rightMotor.rotate(convertDistance(distance, 1), false);

  }

  /**
   * Turn the robot by a certain degrees. Clockwise is positive
   * 
   * @param angle the number of degrees the robot needs to rotate
   */
  public void turnBy(double angle) {
    leftMotor.rotate(convertAngle(angle, 0), true);
    rightMotor.rotate(-convertAngle(angle, 1), false);
  }

  /**
   * Converts input distance to the total rotation of each wheel needed to cover that distance.
   * 
   * @param distance the input distance
   * @param direction the left wheel or the right wheel (0 is left and otherwise it's right)
   * @return the wheel rotations necessary to cover the distance
   */
  public static int convertDistance(double distance, int direction) {
    double radius;
    if (direction == 0) {
      radius = Resources.WHEEL_RAD_LEFT;
    } else {
      radius = Resources.WHEEL_RAD_RIGHT;
    }
    int dist = (int) ((180.0 * distance) / (Math.PI * radius));
    return dist;
  }

  /**
   * Converts input angle to the total rotation of each wheel needed to rotate the robot by that angle.
   * 
   * @param angle the input angle
   * @param direction the left wheel or the right wheel (0 is left and otherwise it's right)
   * @return the wheel rotations necessary to rotate the robot by the angle
   */
  public static int convertAngle(double angle, int direction) {
    return convertDistance(Math.PI * Resources.BASE_WIDTH * angle / 360.0, direction);
  }

//  /**
//   * Stops both motors.
//   */
//  public void stopMotors() {
//    leftMotor.stop(true);
//    rightMotor.stop(true);
//  }

  /**
   * Method that tells when the movement is done. Used in USLocalize to signify that the movement is done
   * 
   * @param flag true if the movement is done
   */
  public void setIsDone(boolean flag) {
    this.isDone = flag;
  }

  /**
   * Method to get if the movement is done
   * 
   * @return true if the movement is done
   */
  public boolean getIsDone() {
    return isDone;
  }
}
