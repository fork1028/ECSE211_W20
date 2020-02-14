package ca.mcgill.ecse211.project;

import ca.mcgill.ecse211.project.Resources.*;

public class Navigation {

  // Singleton navigation object
  private static Navigation navi;

  // Resources objects odometer and movement
  private static Odometer odometer;
  private static Movement movement;
  private LightLocalizer lightLocalizer;

  // constructor
  public Navigation(LightLocalizer ll) {
    this.odometer = Resources.odometer;
    this.movement = Resources.movement;
    this.lightLocalizer = ll;
  }

  /**
   * do maps below
   */
  public void doMapOne() {
    this.travelTo(1, 3);
    this.travelToLL(2, 2);
    this.travelTo(3, 3);
    this.travelTo(3, 2);
    this.travelToLL(2, 1);
  }

  /**
   * do maps below
   */
  public void doMapTwo() {
    this.travelTo(2, 2);
    this.travelTo(1, 3);
    this.travelTo(3, 3);
    this.travelTo(3, 2);
    this.travelTo(2, 1);
  }

  /**
   * do maps below
   */
  public void doMapThree() {
    this.travelTo(2, 1);
    this.travelTo(3, 2);
    this.travelTo(3, 3);
    this.travelTo(1, 3);
    this.travelTo(2, 2);
  }

  /**
   * do maps below
   */
  public void doMapFour() {
    this.travelTo(1, 2);
    this.travelTo(2, 3);
    this.travelTo(2, 1);
    this.travelTo(3, 2);
    this.travelTo(3, 3);
  }


  /**
   * do bonus below
   */
  public void doBonusOne() {
    this.travelTo(1, 7);
    this.travelTo(4, 4);
    this.travelTo(7, 7);
    this.travelTo(7, 4);
    this.travelTo(4, 1);
  }

  /**
   * do bonus below
   */
  public void doBonusTwo() {
    this.travelTo(4, 4);
    this.travelTo(1, 7);
    this.travelTo(7, 7);
    this.travelTo(7, 4);
    this.travelTo(4, 1);
  }

  /**
   * do bonus below
   */
  public void doBonusThree() {
    this.travelTo(4, 1);
    this.travelTo(7, 4);
    this.travelTo(7, 7);
    this.travelTo(1, 7);
    this.travelTo(4, 4);
  }

  /**
   * do bonus below
   */
  public void doBonusFour() {
    this.travelTo(1, 4);
    this.travelTo(4, 7);
    this.travelTo(4, 1);
    this.travelTo(7, 4);
    this.travelTo(7, 7);
  }

  /**
   * helper method for calling movement to do travel to
   * 
   * @param x
   * @param y
   */
  private void travelTo(int x, int y) {
    movement.travelToNavigation(x * Resources.TILE_SIZE, y * Resources.TILE_SIZE);
    // lightLocalizer.lightLocalize(x * Resources.TILE_SIZE, y * Resources.TILE_SIZE);
  }

  /**
   * helper method for last point to relocalize
   * 
   * @param x
   * @param y
   */
  private void travelToLL(int x, int y) {
    movement.travelToLightLocalization(x * Resources.TILE_SIZE, y * Resources.TILE_SIZE);
    lightLocalizer.lightLocalize(x * Resources.TILE_SIZE, y * Resources.TILE_SIZE);
  }
}
