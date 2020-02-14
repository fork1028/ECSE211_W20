package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.*;

/**
 * Controls the robot's movements based on ultrasonic data. <br>
 * <br>
 * Control of the wall follower is applied periodically by the UltrasonicController thread in the while loop in
 * {@code run()}. Assuming that {@code usSensor.fetchSample()} and {@code 
 * processUsData()} take ~20ms, and that the thread sleeps for 50 ms at the end of each loop, then one cycle through the
 * loop is approximately 70 ms. This corresponds to a sampling rate of 1/70ms or about 14 Hz.
 */
public class UltrasonicController implements Runnable {

  /**
   * The distance remembered by the {@code filter()} method.
   */
  private int prevDistance;

  /**
   * The number of invalid samples seen by {@code filter()} so far.
   */
  private int invalidSampleCount;

  /**
   * Buffer (array) to store US samples. Declared as an instance variable to avoid creating a new array each time
   * {@code readUsSample()} is called.
   */
  private float[] usData = new float[usSensor.sampleSize()];

  /**
   * The controller type.
   */
  private String type;

  /**
   * An integer variable for storing the distance difference.
   */
  private int distError = 0;

  /**
   * The leftSpeed and rightSpeed initialized for the robot.
   */
  private int leftSpeed;
  private int rightSpeed;

  /**
   * Constructor for an abstract UltrasonicController. It makes the robot move forward.
   */
  public UltrasonicController(String type) {
    this.type = type;
    leftMotor.setSpeed(MOTOR_HIGH);
    rightMotor.setSpeed(MOTOR_HIGH);
    leftMotor.forward();
    rightMotor.forward();
  }

  /**
   * Process a movement based on the US distance passed in (BANG-BANG style).
   * 
   * @param distance the distance in cm
   * @throws InterruptedException
   */
  public void bangBangController(int distance) throws InterruptedException {

    // the calculated distance error
    distError = WALL_DIST - distance;

    // if the distError is within the threshold, then move forward
    if (Math.abs(distError) <= WALL_DIST_ERR_THRESH) {
      leftMotor.setSpeed(MOTOR_HIGH);
      rightMotor.setSpeed(MOTOR_HIGH);
      leftMotor.forward();
      rightMotor.forward();
    }

    // when the robot is too close to the wall, turn right
    else if (distError > 0) {
      // when the robot is too close the wall in front of it, move backward in case of hitting
      if (distError > dist_threshold) {
        leftMotor.setSpeed(delta_speed);
        rightMotor.setSpeed(delta_speed + MOTOR_HIGH);
        leftMotor.forward();
        rightMotor.backward();
      }
      // if everything is going well, turn right
      else {
        leftMotor.setSpeed(MOTOR_HIGH + delta_speed);
        rightMotor.setSpeed(MOTOR_HIGH);
        leftMotor.forward();
        rightMotor.forward();
      }
    }
    // when the robot is too far away from the wall, turn left
    else if (distError < 0) {
      leftMotor.setSpeed(MOTOR_HIGH);
      rightMotor.setSpeed(MOTOR_HIGH + delta_speed);
      leftMotor.forward();
      rightMotor.forward();
    }
    Thread.sleep(POLL_SLEEP_TIME);
  }

  /**
   * Process a movement based on the US distance passed in (P style)
   * 
   * @param distance the distance in cm
   */
  public void pTypeController(int distance) {

    // the calculated distance error
    distError = WALL_DIST - distance;

    // if the distError is within the threshold, then move forward
    if (Math.abs(distError) <= WALL_DIST_ERR_THRESH) {
      leftSpeed = MOTOR_HIGH;
      rightSpeed = MOTOR_HIGH;
      leftMotor.setSpeed(leftSpeed);
      rightMotor.setSpeed(rightSpeed);
      leftMotor.forward();
      rightMotor.forward();
    }

    // when the robot is too close to the wall, turn right
    else if (distError + delta_distance > 0) {
      // use p_value to calculate the proportional delta speed needed
      rightSpeed = (int) (MOTOR_HIGH + distError * p_value);
      leftSpeed = MOTOR_HIGH;
      leftMotor.setSpeed(leftSpeed);
      rightMotor.setSpeed(rightSpeed);
      leftMotor.forward();
      rightMotor.backward();

    }
    // when the robot is too far away from the wall, turn left
    else if (distError + delta_distance < 0) {
      // to avoid the infinite distance error case, set a threshold and make the robot do the left turn
      // under constant speed
      if (distError < -delta_distance) {
        leftMotor.setSpeed(delta_speed);
        rightMotor.setSpeed(MOTOR_HIGH + MOTOR_LOW);
        leftMotor.forward();
        rightMotor.forward();
      }
      // otherwise, do the left turn proportionally
      else {
        leftSpeed = MOTOR_HIGH;
        rightSpeed = (int) (MOTOR_HIGH + distError * p_value);
        leftMotor.setSpeed(leftSpeed);
        rightMotor.setSpeed(rightSpeed);
        leftMotor.forward();
        rightMotor.forward();
      }
    }
  }

  /*
   * Samples the US sensor and invokes the selected controller on each cycle (non Javadoc).
   * 
   * @see java.lang.Thread#run()
   */
  public void run() {
    if (type.equals("BangBang")) {
      while (true) {
        try {
          bangBangController(readUsDistance());
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        Main.sleepFor(POLL_SLEEP_TIME);
      }
    } else if (type.equals("PType")) {
      while (true) {
        pTypeController(readUsDistance());
        Main.sleepFor(POLL_SLEEP_TIME);
      }
    }
  }

  /**
   * Returns the filtered distance between the US sensor and an obstacle in cm.
   * 
   * @return the filtered distance between the US sensor and an obstacle in cm
   */
  public int readUsDistance() {
    usSensor.fetchSample(usData, 0);
    // extract from buffer, convert to cm, cast to int, and filter
    return filter((int) (usData[0] * 100.0));
  }

  /**
   * Rudimentary filter - toss out invalid samples corresponding to null signal.
   * 
   * @param distance raw distance measured by the sensor in cm
   * @return the filtered distance in cm
   */
  int filter(int distance) {
    if (distance >= 255 && invalidSampleCount < INVALID_SAMPLE_LIMIT) {
      // bad value, increment the filter value and return the distance remembered from before
      invalidSampleCount++;
      return prevDistance;
    } else {
      if (distance < 255) {
        // distance went below 255: reset filter and remember the input distance.
        invalidSampleCount = 0;
      }
      prevDistance = distance;
      return distance;
    }
  }

}
