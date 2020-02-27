package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.*;
import lejos.hardware.Sound;
import lejos.hardware.sensor.EV3ColorSensor;
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
  private SensorModes colourSamplerSensor = LIGHT_SENSOR_LOCAL;
  private SampleProvider colourSensor = colourSamplerSensor.getMode("Red");
  private float[] colourSensorValues = new float[colourSamplerSensor.sampleSize()];
  
  //Private objects to store odometer and movement
  private Odometer odometer;
  private UltrasonicLocalizer usLocalizer;
  
  //blic static double[] lineAngles = {0,0,0,0};
  /**
   * Constructor that takes nothing as input and initializes the odometer and movement objects using the objects stored in the resources
   * Also initializes the Light sensor and the array with the values
   */
  public LightLocalizer(UltrasonicLocalizer usLocalizer) {
    this.odometer = Resources.odometer;
    this.usLocalizer = usLocalizer;   
    
  }
  
  /**
   * The method called by main to reorient the robot
   */
  public void localize() {
    
    Movement.turnTo(45);
    
    //Get all the initial value from sensor
     colourSensor.fetchSample(colourSensorValues, 0);
     float red = colourSensorValues[0];
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
         angles[j] = odometer.getTheta() + 155;
         Sound.beep();
         passed = true;
         j++;
       }
       //Get black lines while rotating counter clockwise
       Movement.rotateCounterClockwise();
     }
     for (int i = 0; i <4; i++) {
      
       angles[i] = makePositive(angles[i]);
       System.out.println("angles[" + i + "]: " + angles[i]);
     }
     //calculations to find the (0,0)
     double thetaY = makeObtuse(Math.abs(angles[0] - angles[2]));
     double thetaX = makeObtuse(Math.abs(angles[1] - angles[3]));
     double x = LIGHT_SENSOR_DISTANCE*Math.cos(Math.toRadians(thetaY/2));
     double y = LIGHT_SENSOR_DISTANCE*Math.cos(Math.toRadians(thetaX/2));
     //Set the values calculated as the new positions for the robot
     odometer.setXyt(x, y, odometer.getTheta());
     //Move to the crossing of lines and rotate back to 0deg
     Movement.travelTo(0.0, 0.0, false);
     Movement.turnTo(fixAngle(angles[0], angles[2])+180);
     odometer.setXyt(TILE_SIZE, TILE_SIZE, 0.0);
  }
  private double makePositive(double angle) {
    return (angle < 0) ? angle + 360 : angle;
  }
  private double makeObtuse(double angle) {
    return (angle > 180) ? 360-angle : angle;
  }
  private double fixAngle(double a, double b) {
    double diff = a - b;
    diff = (diff < 0) ? (diff + 360.0) : diff;
    if (diff > 180) {
      diff = 360.0 - diff;
    }
    diff = (180 - diff)/2.0;
    diff = diff + a;
    diff = (diff > 180) ? (diff - 360) : diff;
    return diff;
  }
  
  
   public void lightLocalize(double x, double y) {
     
     //Get all the initial value from sensor
     colourSensor.fetchSample(colourSensorValues, 0);
     float red = colourSensorValues[0];
     
     //Used to make sure the next value is when the line is rotated passed
     boolean passed = false;
     //Array for the 4 angles of the lines
     double[] angles = new double[4];
     //index for the array with angles
     int j = 0;
     
     //store initial orientation so that we know how to switch the array data
     double orientation = odometer.getTheta()%360;
     
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
         angles[j] = odometer.getTheta() + 180.0;
         Sound.beep();
         passed = true;
         j++;
       }
       
       //Get black lines while rotating counter clockwise
       Movement.rotateCounterClockwise();
     }
     
     double temp;
     if (orientation > 180 && orientation < 270) {
       
       Sound.beep();
       
     } else if (orientation > 90 && orientation < 180) {
       
       temp = angles[3];
       angles[3] = angles[2];
       angles[2] = angles[1];
       angles[1] = angles[0];
       angles[0] = temp;
       
     } else if (orientation > 0 && orientation < 90) {
       
       temp = angles[1];
       angles[1] = angles[3];
       angles[3] = temp;
       temp = angles[2];
       angles[2] = angles[0];
       angles[0] = temp;
       
     } else if (orientation > 270 && orientation < 360) {
       
       temp = angles[0];
       angles[0] = angles[1];
       angles[1] = angles[2];
       angles[2] = angles[3];
       angles[3] = temp;
       
     }
     
     //calculations to find the (0,0)
     double thetaY = fixAngle(angles[0], angles[2]);
     double thetaX = fixAngle(angles[1], angles[3]);
     double actualX = -LIGHT_SENSOR_DISTANCE*Math.cos(Math.toRadians(thetaY/2));
     double actualY = -LIGHT_SENSOR_DISTANCE*Math.cos(Math.toRadians(thetaX/2));
     
     //Set the values calculated as the new positions for the robot
     odometer.setXyt(actualX, actualY, odometer.getTheta());
     
     //Move to the crossing of lines and rotate back to 0deg
     Movement.travelTo(0.0, 0.0, false);
     Movement.turnTo(fixAngle(angles[0], angles[2]));
     odometer.setXyt(x, y, 0.0);
   }
   
   /**
    * Uses the color sensor to localize the device and reposition its X and Y position
    */
   public void localize2() {
     
     //Used to make sure the next value is when the line is rotated passed
     boolean passed = false;
     float red = colourSensorValues[0];
   //index for the array with angles
     int j = 0;
     
     Movement.turnTo(45);
     
     double[] lineAngles = new double[4];
     
   //start rotation and clock all 4 gridlines run until the 4 angles are obtained
     while (lineAngles[3] == 0) {
       
       //Get sensor values
       colourSensor.fetchSample(colourSensorValues, 0);
       red = colourSensorValues[0];
       
       //If it's not at a line then set the boolean to false
       if (red > 0.4) {
         passed = false;
         
       //If it got to a line then set passed to true, then beep and store the data in the array
       } else if (red < 0.35 && !passed) {
         lineAngles[j] = odometer.getTheta() + 180.0;
         Sound.beep();
         passed = true;
         j++;
       }
       
       //Get black lines while rotating counter clockwise
       Movement.rotateClockwise();
     }
     
//     for (int i = 0; i < 4; i++) {
//       while(!lineDetected()) {
//         Navigation.rotateRobot(RotateDirection.CLOCKWISE, 1);
//       }
//       lineAngles[i] = odometer.getXyt()[2];
//       System.out.println("odo: "+  odometer.getXyt()[2]);
//    Sound.beep();
//     }
     
     double thetaY = (lineAngles[2]- lineAngles[0])/2.0;
     
     double yOffset = SENSOR_OFFSET*Math.cos(Math.toRadians(thetaY));
     
     double thetaX = (lineAngles[3]-lineAngles[1])/2;
     
     double xOffset = SENSOR_OFFSET*Math.cos(Math.toRadians(thetaX));
     
     //Movement.turnTo(fixAngle(lineAngles[3], lineAngles[1]));
     
     //odometer.setTheta(0);
     
     Movement.turnTo(0);
     Movement.goForward(yOffset);
     Movement.turnTo(90);
     Movement.goForward(xOffset);
     Movement.turnTo(0);
     
     
     
   }
   


}
