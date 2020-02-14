package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.*;
import lejos.hardware.Sound;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

/**
 * The LightLocalizer uses the light sensor to move the robot
 * 
 * @author Xinyue Chen (260830761)
 * @author Zheng Yu Cui (260808525)
 */
public class LightLocalizer {

  // Initialize variables for the colour sensor

  private SensorModes colourSamplerSensor;
  private SampleProvider colourSensor;
  private float[] colourSensorValues;

  // Initialize the colour Value

  private float colourValue;

  // Initialize array to store line angles

  private double[] lineMeasures = new double[4];

  // Private objects to store odometer and movement
  private Odometer odometer;
  private Movement movement;
  private UltrasonicLocalizer usLocalizer;

  /**
   * Constructor that takes nothing as input and initializes the odometer and movement objects using the objects stored
   * in the resources Also initializes the Light sensor and the array with the values
   */
  public LightLocalizer(UltrasonicLocalizer usLocalizer) {
    this.odometer = Resources.odometer;
    this.movement = Resources.movement;
    this.usLocalizer = usLocalizer;

    // Initializes the sensor and the array that stores its data
    colourSamplerSensor = Resources.LIGHT_SENSOR;
    colourSensor = colourSamplerSensor.getMode("Red");
    colourSensorValues = new float[colourSamplerSensor.sampleSize()];
  }

  /**
   * The method called by main to move the robot to the (1,1) on the board
   */
  public void moveToOrigin() {

    // Get all the initial value from sensor
    colourSensor.fetchSample(colourSensorValues, 0);
    float red = colourSensorValues[0];

    // Turn generally towards (1,1)
    movement.turnTo(45, true);

    // Run loop as long as it doesn't detect black line
    while (red > 0.4) {

      // Move forward
      movement.goForward();

      // Get new data
      colourSensor.fetchSample(colourSensorValues, 0);
      red = colourSensorValues[0];

    }

    // Sound tell that the robot detected the line
    Sound.beep();

    // Move forward a little bit so that wheel axle center is on the (1,1)
    movement.goForward(5.0);

    // Used to make sure the next value is when the line is rotated passed
    boolean passed = false;
    // Array for the 4 angles of the lines
    double[] angles = new double[4];
    // index for the array with angles
    int j = 0;

    // start rotation and clock all 4 gridlines run until the 4 angles are obtained
    while (angles[3] == 0) {

      // Get sensor values
      colourSensor.fetchSample(colourSensorValues, 0);
      red = colourSensorValues[0];

      // If it's not at a line then set the boolean to false
      if (red > 0.35) {
        passed = false;

        // If it got to a line then set passed to true, then beep and store the data in the array
      } else if (red < 0.5 && !passed) {
        angles[j] = odometer.getTheta();
        Sound.beep();
        passed = true;
        j++;
      }

      // Get black lines while rotating counter clockwise
      movement.rotateCounterClockwise();
    }

    // calculations to find the (0,0)
    double thetaY = angleDifference(angles[0], angles[2]);
    double thetaX = angleDifference(angles[1], angles[3]);
    double x = LIGHT_SENSOR_DISTANCE * Math.cos(Math.toRadians(thetaY / 2));
    double y = LIGHT_SENSOR_DISTANCE * Math.cos(Math.toRadians(thetaX / 2));

    // Set the values calculated as the new positions for the robot
    odometer.setXyt(x, y, odometer.getTheta());

    // Move to the crossing of lines and rotate back to 0deg
    movement.travelTo(0.0, 0.0);
    movement.turnTo(-10.0, true);
    odometer.setXyt(TILE_SIZE, TILE_SIZE, 0.0);
  }

  /**
   * Takes the difference between two angles and adjust them so that they are constantly between -180 and 180
   * 
   * @param a first angle
   * @param b second angle
   * @return the angle difference between the two so that they're always between -180 and 180
   */
  private double angleDifference(double a, double b) {

    // Calculating difference
    double diff = a - b;

    return diff;
  }

  /**
   * Method for finding the difference from the old 0 degrees to the true north
   * 
   * @param a
   * @param b
   * @return
   */
  private double fixAngle(double a, double b) {
    // angles above and below the current robot position
    double north, south;
    // difference between north and south
    double difference;

    // calculation
    north = Math.abs(a);
    south = Math.abs(180 - b);

    // making sure it's positive
    south = (south >= 180) ? (360 - south) : south;

    difference = (south - north) / 2.0;

    return difference;
  }


  /**
   * Method for localize with light sensor
   * 
   * @param x
   * @param y
   */
  public void lightLocalize(double x, double y) {

    // Get all the initial value from sensor
    colourSensor.fetchSample(colourSensorValues, 0);
    float red = colourSensorValues[0];

    // Run loop as long as it doesn't detect black line
    while (red > 0.4) {

      // Move forward
      movement.goForward();

      // Get new data
      colourSensor.fetchSample(colourSensorValues, 0);
      red = colourSensorValues[0];

    }

    // Sound tell that the robot detected the line
    Sound.beep();

    // Move forward a little bit so that wheel axle center is on the (1,1)
    movement.goForward(5.0);

    // Used to make sure the next value is when the line is rotated passed
    boolean passed = false;
    // Array for the 4 angles of the lines
    double[] angles = new double[4];
    double[] distances = new double[4];
    // index for the array with angles
    int j = 0;

    // start rotation and clock all 4 gridlines run until the 4 angles are obtained
    while (angles[3] == 0) {

      // Get sensor values
      colourSensor.fetchSample(colourSensorValues, 0);
      red = colourSensorValues[0];

      // If it's not at a line then set the boolean to false
      if (red > 0.4) {
        passed = false;

        // If it got to a line then set passed to true, then beep and store the data in the array
      } else if (red < 0.35 && !passed) {
        angles[j] = odometer.getTheta();
        distances[j] = usLocalizer.getFilteredData();
        Sound.beep();
        passed = true;
        j++;
      }

      // Get black lines while rotating counter clockwise
      movement.rotateCounterClockwise();
    }

    double temp;

    // re-orienting the angles so that angle[0] faces north
    if (distances[0] >= 1000 && distances[1] < 1000) {
      Sound.beep();
    } else if (distances[0] < 1000 && distances[1] < 1000) {
      temp = angles[3];
      angles[3] = angles[2];
      angles[2] = angles[1];
      angles[1] = angles[0];
      angles[0] = temp;
    } else if (distances[0] < 1000 && distances[1] >= 1000) {
      temp = angles[1];
      angles[1] = angles[3];
      angles[3] = temp;
      temp = angles[2];
      angles[2] = angles[0];
      angles[0] = angles[2];
    } else if (distances[0] >= 1000 && distances[1] >= 1000) {
      temp = angles[0];
      angles[0] = angles[1];
      angles[1] = angles[2];
      angles[2] = angles[3];
      angles[3] = temp;
    }

    // calculations to find the (0,0)
    double thetaY = angleDifference(angles[0], angles[2]);
    double thetaX = angleDifference(angles[1], angles[3]);
    double actualX = -LIGHT_SENSOR_DISTANCE * Math.cos(Math.toRadians(thetaY / 2));
    double actualY = LIGHT_SENSOR_DISTANCE * Math.cos(Math.toRadians(thetaX / 2));

    // Set the values calculated as the new positions for the robot
    odometer.setXyt(actualX, actualY, odometer.getTheta());

    // Move to the crossing of lines and rotate back to 0deg
    movement.travelTo(0.0, 0.0);
    movement.turnTo(0.0, true);
    odometer.setXyt(x, y, 0.0);
  }
}
