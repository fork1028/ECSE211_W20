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
  
  //Initialize variables for the colour sensor
  
  private SensorModes colourSamplerSensor;
  private SampleProvider colourSensor;
  private float[] colourSensorValues;
  
  //Initialize the colour Value
  
  private float colourValue;
  
  //Initialize array to store line angles
  
  private double[] lineMeasures = new double[4];
  
  //Private objects to store odometer and movement
  private Odometer odometer;
  private Movement movement;
  
  /**
   * Constructor that takes nothing as input and initializes the odometer and movement objects using the objects stored in the resources
   * Also initializes the Light sensor and the array with the values
   */
  public LightLocalizer() {
    this.odometer = Resources.odometer;
    this.movement = Resources.movement;
    
    //Initializes the sensor and the array that stores its data
    colourSamplerSensor = Resources.LIGHT_SENSOR;
    colourSensor = colourSamplerSensor.getMode("Red");
    colourSensorValues = new float[colourSamplerSensor.sampleSize()];
  }
  
  /**
   * The method called by main to move the robot to the (1,1) on the board
   */
  public void moveToOrigin() {
     
    //Get all the initial value from sensor
     colourSensor.fetchSample(colourSensorValues, 0);
     float red = colourSensorValues[0];
     
     //Turn generally towards (1,1)
     movement.turnTo(45, true);
     
     //Run loop as long as it doesn't detect black line
     while (red > 0.4) {
       
       //Move forward
       movement.goForward();
       
       //Get new data
       colourSensor.fetchSample(colourSensorValues, 0);
       red = colourSensorValues[0];
       
     }
     
     //Sound tell that the robot detected the line
     Sound.beep();
     
     //Move forward a little bit so that wheel axle center is on the (1,1)
     movement.goForward(5.0);
     
     //Used to make sure the next value is when the line is rotated passed
     boolean passed = false;
     //Array for the 4 angles of the lines
     double[] angles = new double[4];
     //index for the array with angles
     int j = 0;
     
   //start rotation and clock all 4 gridlines run until the 4 angles are obtained
     while (angles[3] == 0) {
       
       //Get sensor values
       colourSensor.fetchSample(colourSensorValues, 0);
       red = colourSensorValues[0];
       
       //If it's not at a line then set the boolean to false
       if (red > 0.4) {
         passed = false;
         
       //If it got to a line then set passed to true, then beep and store the data in the array
       } else if (red < 0.35 && !passed) {
         angles[j] = odometer.getTheta();
         Sound.beep();
         passed = true;
         j++;
       }
       
       //Get black lines while rotating counter clockwise
       movement.rotateCounterClockwise();
     }
     
     //calculations to find the (0,0)
     double thetaY = angleDifference(angles[0], angles[2]);
     double thetaX = angleDifference(angles[1], angles[3]);
     double x = -LIGHT_SENSOR_DISTANCE*Math.cos(Math.toRadians(thetaY/2));
     double y = -LIGHT_SENSOR_DISTANCE*Math.cos(Math.toRadians(thetaX/2));
     
     //Set the values calculated as the new positions for the robot
     odometer.setXyt(x, y, odometer.getTheta());
     
     //Move to the crossing of lines and rotate back to 0deg
     movement.travelTo(0.0, 0.0);
     movement.turnTo(0.0, true);
  }
  
  /**
   * Takes the difference between two angles and adjust them so that they are constantly between -180 and 180
   * @param a first angle
   * @param b second angle
   * @return the angle difference between the two so that they're always between -180 and 180
   */
  private double angleDifference(double a, double b) {
    
    //Calculating difference
    double diff = a - b;
    
    //Adjusting angle so taht its always inbetween -180 and 180
    if (diff > 180) {
      diff -= 360;
    } else if (diff < -180) {
      diff += 360;
    }
    return diff;
  }
}
