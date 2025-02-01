package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.LimelightTarget_Retro;

public class Limelight extends SubsystemBase implements ILimelight {
    private final String limelightName = "limelight-seaweed";

    @Override
    public double get_tx() {
        return LimelightHelpers.getTX(limelightName);
    }

    @Override
    public double get_ty() {
        return LimelightHelpers.getTY(limelightName);
    }

    @Override
    public double get_ta() {
        return LimelightHelpers.getTA(limelightName);
    }

    @Override
    public boolean get_tv() {
        return LimelightHelpers.getTV(limelightName);
    }

    public double getYaw() {
        double[] botpose = LimelightHelpers.getBotPose_wpiBlue(limelightName);
        if (botpose.length >= 6) {
            return botpose[5]; // Yaw is the 6th element (index 5)
        }
        return 0.0;
    }

    public Pose2d getTargetPose() {
        return LimelightHelpers.toPose2D(LimelightHelpers.getBotPose_wpiBlue(limelightName));
    }

    @Override
    public void periodic() {
        // Debug values
        SmartDashboard.putNumber("Limelight TX", get_tx());
        SmartDashboard.putNumber("Limelight TY", get_ty());
        SmartDashboard.putNumber("Limelight TA", get_ta());
        SmartDashboard.putBoolean("Has Target", get_tv());
        SmartDashboard.putNumber("Target Yaw", getYaw());
    }
}
