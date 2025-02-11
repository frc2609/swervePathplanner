// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.Distance;

import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.Inches;

import java.util.HashMap;

import edu.wpi.first.units.measure.*; 

public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }

    public static class VisionConstants {
    public static final String LIMELIGHT_NAME = "limelight";
    public static final Distance LIMELIGHT_LENS_HEIGHT = Distance.ofBaseUnits(31, Inches);
    public static final Angle LIMELIGHT_ANGLE = Angle.ofBaseUnits(45, Degrees);

    public static final Distance REEF_APRILTAG_HEIGHT = Distance.ofBaseUnits(6.875, Inches);
    public static final Distance PROCCESSOR_APRILTAG_HEIGHT = Distance.ofBaseUnits(45.875, Inches);
    public static final Distance CORAL_APRILTAG_HEIGHT = Distance.ofBaseUnits(53.25, Inches);

    public static final HashMap<Integer, double[]> aprilTagMap;
    static {
      aprilTagMap = new HashMap<>();
      aprilTagMap.put(18, new double[]{
        -0.5877852522924729,
        -0.8090169943749473,
        0,
        7.923198000000001,
        0.8090169943749473,
        -0.5877852522924729,
        0,
        -3.3706799999999997,
        0,
        0,
        1,
        1.4859,
        0,
        0,
        0,
        1
    });
    }
  }

}