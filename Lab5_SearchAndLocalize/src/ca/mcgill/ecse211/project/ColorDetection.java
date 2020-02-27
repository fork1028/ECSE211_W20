package ca.mcgill.ecse211.project;


import lejos.robotics.SampleProvider;


public class ColorDetection {

  // RGB indexes
  private static final int RED_INDEX = 0;
  private static final int GREEN_INDEX = 1;
  private static final int BLUE_INDEX = 2;

  // rgb color data in the order of {R,G,B}

  public static double[] RED_COLOR = {0.959771, 0.26671, 0.08776}; // value of red color
  public static double[] GREEN_COLOR = {0.398574, 0.904758, 0.15017}; // value of green color
  public static double[] BLUE_COLOR = {0.149069, 0.780432, 0.60721}; // value of blue color
  public static double[] YELLOW_COLOR = {0.82158, 0.55636, 0.1244}; // value of yellow color



  public static final SampleProvider myColorSample = Resources.LIGHT_SENSOR_DONUT.getMode("RGB"); // get RGB mode
  public static final float[] sampleColor = new float[100]; // create an array for the sensor


  private static double smallest = 1; // the max possible value for a normalized reading is 1
  private static double colorThreshold = 0.2; // all correct readings are smaller than thus thresold, obtained in the
                                              // color sampling process

  /**
   * this method is for calculating the mean and standard deviation for the initial readings and call color() method for
   * telling what the color is where 0=red, 1=green, 2=blue, 3=yellow, and -1 for everything else
   * 
   * @return int color
   */
  public static int calibrate() {

    // obtain reading from sensor and normalize them
    myColorSample.fetchSample(sampleColor, 0);
    double r = rNormalize(sampleColor[0] * 1000, sampleColor[1] * 1000, sampleColor[2] * 1000);
    double g = gNormalize(sampleColor[0] * 1000, sampleColor[1] * 1000, sampleColor[2] * 1000);
    double b = bNormalize(sampleColor[0] * 1000, sampleColor[1] * 1000, sampleColor[2] * 1000);

    // get euclidean distance for each color
    double dBlue = euclidean(r, g, b, BLUE_COLOR[RED_INDEX], BLUE_COLOR[GREEN_INDEX], BLUE_COLOR[BLUE_INDEX]);
    double dGreen = euclidean(r, g, b, GREEN_COLOR[RED_INDEX], GREEN_COLOR[GREEN_INDEX], GREEN_COLOR[BLUE_INDEX]);
    double dYellow = euclidean(r, g, b, YELLOW_COLOR[RED_INDEX], YELLOW_COLOR[GREEN_INDEX], YELLOW_COLOR[BLUE_INDEX]);
    double dOrange = euclidean(r, g, b, RED_COLOR[RED_INDEX], RED_COLOR[GREEN_INDEX], RED_COLOR[BLUE_INDEX]);

    // rank the distances and choose the color --smallest euclidean
    double[] d = {dBlue, dGreen, dYellow, dOrange};
    for (int i = 0; i < 4; i++) {
      if (i == 0)
        smallest = d[i];
      else if (d[i] < smallest)
        smallest = d[i];
    }
    
    //System.out.println("smallest: "+smallest);
    //System.out.println("colorThreshold: "+colorThreshold);

    // return the TR value
    if (smallest <= colorThreshold) {
      if (smallest == dOrange) {
        System.out.println("Orange Ring");
        return 0; // return blue
      }
      if (smallest == dGreen) {
        System.out.println("Green Ring");
        return 1; // return green
      }
      if (smallest == dBlue) {
       System.out.println("Blue Ring");
        return 2; // return yellow
      }
      if (smallest == dYellow) {
        System.out.println("Yellow Ring");
        return 3; // return orange
      }
    }
    //System.out.println("wrong");
    return -1;

  }

  /**
   * calculates the euclidean distance in RGB space
   * 
   * @param rN normalized red reading
   * @param gN normalized green reading
   * @param bN normalized blue reading
   * @param rM mean red value for a color
   * @param gM mean green value for a color
   * @param bM mean blue value for a color
   * @return the euclidean distance in double
   */
  public static double euclidean(double rN, double gN, double bN, double rM, double gM, double bM) {
    return Math.sqrt(Math.pow((rN - rM), 2) + Math.pow((gN - gM), 2) + Math.pow((bN - bM), 2));
  }

  /**
   * this is used for normalizing the r value of a new reading
   * 
   * @param r red reading
   * @param g green reading
   * @param b blue reading
   * @return normalized reading
   */
  public static double rNormalize(float r, float g, float b) {
    System.out.println("r: "+r);
    System.out.println("g: "+g);
    System.out.println("b: "+b);
    return r / (Math.sqrt(Math.pow(r, 2) + Math.pow(g, 2) + Math.pow(b, 2)));
  }

  /**
   * this is used for normalizing the g value of a new reading
   * 
   * @param r red reading
   * @param g green reading
   * @param b blue reading
   * @return normalized reading
   */
  public static double gNormalize(float r, float g, float b) {
    return g / (Math.sqrt(Math.pow(r, 2) + Math.pow(g, 2) + Math.pow(b, 2)));
  }

  /**
   * this is used for normalizing the b value of a new reading
   * 
   * @param r red reading
   * @param g green reading
   * @param b blue reading
   * @return normalized reading
   */
  public static double bNormalize(float r, float g, float b) {
    return b / (Math.sqrt(Math.pow(r, 2) + Math.pow(g, 2) + Math.pow(b, 2)));
  }

}
