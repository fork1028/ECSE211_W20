package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.*;
import lejos.hardware.Sound;
import lejos.robotics.SampleProvider;

/**
 * Class that does all the ultrasonic behaviour: finding the 0deg and the US finding of (1, 1)
 * 
 * @author Xinyue Chen
 * @author Zheng Yu Cui
 */

public class UltrasonicLocalizer {

  // Initialize starting variables
  private static SampleProvider myDistance = US_SENSOR.getMode("Distance");
  private static float[] usData = new float[myDistance.sampleSize()];

  // Initialize variables
  private double angleA, angleB, deltaT;

  public enum Direction {
    LEFT, RIGHT
  };

  // private objects used by the localizer
  private Odometer odometer;
  private Movement movement;

  /**
   * Constructor that initializes the private variable with variables stored in Resources
   */
  public UltrasonicLocalizer() {

    this.odometer = Resources.odometer;
    this.movement = Resources.movement;

  }

  /**
   * Method that find the 0deg according to whichever orientation
   */
  public void localize() {

    // Getting the two falling angles
    angleA = getAngle(Direction.RIGHT);
    angleB = getAngle(Direction.LEFT);

    System.out.println("angle a: " + angleA);
    System.out.print("angle b: " + angleB);

    // Indicate a good pause to show that the two angles are obtained
    //movement.stopMotors();

    // Branches to know which way is the right way to find the difference between the old 0deg to the actual 0deg
    // (north)
    if (angleA > angleB) {
      deltaT = 42.0 - (angleA + angleB) / 2;
    } else if (angleA < angleB) {
      deltaT = 228.0 - (angleA + angleB) / 2;
    }

    // New angle that indicates the actual orientation of the robot according to north
    double updatedAngle = deltaT + odometer.getTheta();
    odometer.setXyt(0.0, 0.0, updatedAngle);

    // Turn to the true north and stop motors
    movement.turnTo(0.0, true);

    // Say that the movement is done so that the display knows
    movement.setIsDone(true);

  }

  /**
   * Method to move the robot to (1,1) using the ultrasonic sensor
   */

  public void moveToOrigin() {

    // The x and y coordinates of (1,1) according to the current location of the robot
    double x, y;

    // Get the x distance from the wall
    movement.turnBy(180.0);
    x = getFilteredData();

    // Get the y distance from the wall
    movement.turnBy(-270.0);
    y = getFilteredData();

    // Move to the (1,1) point on the wall knowing that TILE_SIZE is the distance from the wall to the line
    movement.travelTo(TILE_SIZE - x, TILE_SIZE - y);

    // turn back to true north
    movement.turnTo(0.0, true);

    odometer.setXyt(TILE_SIZE, TILE_SIZE, 0.0);

  }

  /**
   * Method to move the robot in a specific direction to calculate the falling angle according to the right distance
   * 
   * @param dir direction of rotation to choose which rotation to turn first
   * @return the falling angle
   */
  private double getAngle(Direction dir) {

    // Choosing which falling edge we're detecting
    if (dir == Direction.RIGHT) {

      // Looking for Empty space
      while (getFilteredData() < 1000) {
        movement.rotateClockwise();
      }

      // Turn as long as Empty Space
      while (getFilteredData() > WALL_DISTANCE) {
        movement.rotateClockwise();
      }

    } else {

      // Looking for Empty space
      while (getFilteredData() < 1000) {
        movement.rotateCounterClockwise();
      }

      // Turn as long as Empty Space
      while (getFilteredData() > WALL_DISTANCE) {
        movement.rotateCounterClockwise();
      }
    }

    Sound.beep();

    return odometer.getTheta();
  }

  /**
   * Filter the data using the five values from the sensor and taking the smallest value, if it is infinity cap it at
   * 1000
   * 
   * @return the filtered data
   */
  public double getFilteredData() {

    // Allocating 5 spots for the sensor's data
    double[] data = new double[5];

    // Store each sensor datum in the array
    for (int i = 0; i < data.length; i++) {
      US_SENSOR.fetchSample(usData, 0);
      double distance = (double) (usData[0] * 100.0);
      data[i] = distance;
    }

    // Get the smallest value in the array
    double smallest = data[0];
    for (int i = 0; i < data.length; i++) {
      smallest = (data[i] < smallest) ? smallest : data[i];
    }

    // If the smallest distance is infinity then cap it at 1000
    smallest = (smallest > 1000) ? 1000 : smallest;
    return smallest;
  }
}
