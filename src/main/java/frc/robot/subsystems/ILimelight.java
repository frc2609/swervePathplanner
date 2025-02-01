package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Subsystem;

public interface ILimelight extends Subsystem {
    double get_tx();
    double get_ty();
    double get_ta();
    boolean get_tv();
} 