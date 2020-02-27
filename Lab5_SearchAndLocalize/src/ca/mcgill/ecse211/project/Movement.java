package ca.mcgill.ecse211.project;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import ca.mcgill.ecse211.project.Resources.*;
import ca.mcgill.ecse211.project.UltrasonicLocalizer.*;

/**
 * The Movement class moves the robot in the grid according to the values in the odometer
 * 
 * @author Xinyue Chen (260830761)
 * @author Zheng Yu Cui (260808525)
 */
public class Movement {
  
    //singleton
    private static Movement movement;

    //Different speeds for the robot's rotation
    final static int FAST = 200;
    final static int SLOW = 100;
    
    //The angle error for when you turn to the right angle otherwise it shimmies
    final static double DEG_ERR = 2.0;
    
    //Private object that reference to the odometer and motors
    private static Odometer odometer;
    private static EV3LargeRegulatedMotor leftMotor, rightMotor;
    
    //Private static reference to the US sensor for the donut detection
    private static UltrasonicLocalizer usLocalizer_Donut;
    
    //If the movement is done
    private boolean isDone = false;
    
    private static int count=0;
    private static int index;
    private static String[] colors=new String[5];
    
    /**
     * Constructor that initializes the odometer and leftmotors using the objects in the resources
     * 
     */
    private Movement(UltrasonicLocalizer usl) {
      
      Movement.odometer = Resources.odometer;
      Movement.leftMotor = Resources.leftMotor;
      Movement.rightMotor = Resources.rightMotor;
      
      Movement.leftMotor.setAcceleration(Resources.ACCELERATION);
      Movement.rightMotor.setAcceleration(Resources.ACCELERATION);
      
      Movement.leftMotor.setSpeed(Resources.ROTATE_SPEED_LEFT);
      Movement.rightMotor.setSpeed(Resources.ROTATE_SPEED_RIGHT);
      
      Movement.usLocalizer_Donut = usl;
      
      
    }

    /**
     * Returns the Movement Object. Use this method to obtain an instance of Movement.
     * 
     * @return the Movement Object
     */
    public static synchronized Movement getMovement(UltrasonicLocalizer usl) {
      if (movement == null) {
        movement = new Movement(usl);
      }

      return movement;
    }
    
    /**
     * Rotate the robot clockwise
     */
    public static void rotateClockwise() {
      leftMotor.setSpeed(Resources.ROTATE_SPEED_LEFT);
      rightMotor.setSpeed(Resources.ROTATE_SPEED_RIGHT);
      leftMotor.forward();
      rightMotor.backward();
    }
    
    /**
     * Rotate the robot counter clockwise
     */    
    public static void rotateCounterClockwise() {
      leftMotor.setSpeed(Resources.ROTATE_SPEED_LEFT);
      rightMotor.setSpeed(Resources.ROTATE_SPEED_RIGHT);
      leftMotor.backward();
      rightMotor.forward();
    }
    
    /**
     * Move the robot forward until forever
     */
    public static void goForward() {
      leftMotor.setSpeed(Resources.FORWARD_SPEED_LEFT);
      rightMotor.setSpeed(Resources.FORWARD_SPEED_RIGHT);
      leftMotor.forward();
      rightMotor.forward();
    }
    
    /**
     *  Functions to set the motor speeds jointly
     *  @param lSpd left motor speed
     *  @param rSpd right motor speed
     */
    public static void setSpeeds(float lSpd, float rSpd) {
        leftMotor.setSpeed(lSpd);
        rightMotor.setSpeed(rSpd);
    }
    public static void setSpeeds(int lSpd, int rSpd) {
      leftMotor.setSpeed(lSpd);
      rightMotor.setSpeed(rSpd);
    }
    
    /**
     * Moves the robot backward continuously.
     */
    public static void moveBack() {
      leftMotor.setSpeed(Resources.FORWARD_SPEED_RIGHT);
      rightMotor.setSpeed(Resources.FORWARD_SPEED_RIGHT);
      leftMotor.backward();
      rightMotor.backward();
    }
    
    
    /**
     * Function to set the acceleration
     * @param acc
     */
    public void setAccelerations(int acc) {
      leftMotor.setAcceleration(acc);
      rightMotor.setAcceleration(acc);
    }
    
    /**
     * Moves the robot to an inputed grid point
     * 
     * @param x
     * @param y
     */
    public static void travelTo(double x, double y, boolean isNavigating) {
      
      double CurX = odometer.getX();
      double CurY = odometer.getY();
      double theta = getTheta(x, y, CurX, CurY);
      double distance = distance(x, y, CurX, CurY);
      
   
      if (x < CurX && y < CurY) {
        theta = - 90 - theta;

      } else if (x > CurX && y < CurY) {
        theta = 90 + theta;

      } else if (x < CurX && y > CurY) {
        theta = -theta;

      } 

      turnTo(theta);
      while (distance(odometer.getX(), odometer.getY(), CurX, CurY) <= distance) {
//       System.out.println("distance: "+ usLocalizer_Donut.getFilteredData());
        if (isNavigating && UltrasonicLocalizer.objectDetected(usLocalizer_Donut.getFilteredData())) {
          
          Sound.beep();
          stopMotors();
          //CHANGE TO CALIBRATE
          index=ColorDetection.calibrate();
          counter();
          Button.waitForAnyPress(5000);
        }
        goForward();
      }
      
      odometer.setXyt(x, y, odometer.getTheta());
      stopMotors();
    }
    
    /**
     * helper method for getting the color string arrays
     */
    public static String[] colors() {
      return colors;
    }
    
    /**
     * helper method for getting the color
     */
    public static void getColor() {
      if(index==0) colors[counter()]="Orange";
      else if(index==1) colors[counter()]="Green";
      else if(index==2) colors[counter()]="Blue";
      else if(index==3) colors[counter()]="Yellow";
    }
    
    /**
     * helper method for getting the number of rings detected
     * @return
     */
    public static int counter() {
      count++;
      return count;
    }
    
    public static double distance(double x, double y, double zeroX, double zeroY) {
      return Math.sqrt(Math.pow(x - zeroX, 2) + Math.pow(y - zeroY, 2));
    }
    
    /**
     * Computes the distance that needs to be travelled
     * 
     * @param X
     * @param Y
     * @param CurX
     * @param CurY
     * @return
     */
    public static double getDistance(double X, double Y, double CurX, double CurY) { 
      double dist = 0;
      double deltaX = X - CurX;
      double deltaY = Y - CurY;
      double a = Math.pow(deltaX, 2);
      double b = Math.pow(deltaY, 2);
      dist = Math.sqrt(a + b);
      return dist;
    }
    
    /**
     * Computes the minimal angle.
     * 
     * @param X
     * @param Y
     * @param CurX
     * @param CurY
     * @return
     */
    public static double getTheta(double X, double Y, double CurX, double CurY) {
      double theta = 0;
      double exes = Math.abs((X - CurX));
      double whys = Math.abs((Y - CurY));
      if (Y < CurY) {
        theta = Math.toDegrees(Math.atan2(whys,exes));
      } else if (Y > CurY) {
        theta = Math.toDegrees(Math.atan2(exes,whys));
      }
      return theta;
    }
    
    /**
     * Obtain the minimal angle so that it is always in-between -180 and 180
     * @param angle that might needs changing
     * @return angle an angle that remains in-between -180 and 180
     */
    public double getMinAngle(double angle) {
      if (angle > 180) {
          angle -= 2 * 180;
      } else if (angle < -180) {
          angle += 2 * 180 ;
      }
      return angle;
  }
    
    
    /**
     * Rotates the robot to a certain angle
     * 
     * @param angle
     */
    public static void turnTo(double angle) {  //In our case angle is almost estimate to be like thefigurative north 
      double rotationAngle;
      
      double curAngle = odometer.getXyt()[2];
      
      if ((angle - curAngle)>180) {
        rotationAngle = angle - curAngle -360;
      }
      else if ((angle - curAngle)< -180) {
        rotationAngle = angle - curAngle + 360;
      }
      else {
        rotationAngle = angle - curAngle;  

      }
      turnBy(rotationAngle);
    }
    
    /**
     * Go forward a set distance in cm
     * @param distance distance in cm
     */
    public static void goForward(double distance) {
      leftMotor.rotate(convertDistance(distance, 0), true);
      rightMotor.rotate(convertDistance(distance, 1), false);
    
    }
    
    /**
     * Turn the robot by a certain degrees. Clockwise is positive
     * @param angle the number of degrees the robot needs to rotate
     */
    public static void turnBy(double angle) {
      leftMotor.setSpeed(Resources.ROTATE_SPEED_LEFT);
      rightMotor.setSpeed(Resources.ROTATE_SPEED_RIGHT);
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
    
    
    /**
     * Stops both motors.
     */
    public static void stopMotors() {
      leftMotor.stop(true);
      rightMotor.stop(false);
    }
}