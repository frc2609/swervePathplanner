package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.Arrays;

public class SimulatedLimelight extends SubsystemBase implements ILimelight {
    private final NetworkTable limelightTable;
    private final Field2d field;
    private final Pose2d targetPose;
    private final CommandSwerveDrivetrain drivetrain;

    public SimulatedLimelight(String limelightName, Pose2d targetPose, CommandSwerveDrivetrain drivetrain) {
        this.limelightTable = NetworkTableInstance.getDefault().getTable(limelightName);
        this.field = new Field2d();
        this.targetPose = targetPose;
        this.drivetrain = drivetrain;
        SmartDashboard.putData("Field", field);
    }

    @Override
    public void periodic() {
        Pose2d robotPose = drivetrain.getState().Pose;

        Transform2d relative = targetPose.minus(robotPose);

        double tx = Math.toDegrees(Math.atan2(relative.getY(), relative.getX()));
        double ty = Math.toDegrees(Math.atan2(relative.getRotation().getRadians(), 
                                             relative.getTranslation().getNorm()));

        double distance = relative.getTranslation().getNorm();
        double ta = 100.0 / (distance * distance);

        boolean targetVisible = Math.abs(tx) < 30 && Math.abs(ty) < 24;

        limelightTable.getEntry("tv").setDouble(targetVisible ? 1.0 : 0.0);
        limelightTable.getEntry("tx").setDouble(targetVisible ? tx : 0.0);
        limelightTable.getEntry("ty").setDouble(targetVisible ? ty : 0.0);
        limelightTable.getEntry("ta").setDouble(targetVisible ? ta : 0.0);

        field.setRobotPose(robotPose);
        field.getObject("target").setPose(targetPose);
        
        updateFieldVisualization(robotPose);
    }

    private void updateFieldVisualization(Pose2d robotPose) {
        double fovLength = 5.0;
        double halfFOV = Math.toRadians(30);
        
        double robotAngle = robotPose.getRotation().getRadians();
        
        // Create FOV visualization using Pose2d objects
        Pose2d leftFOV = new Pose2d(
            robotPose.getX() + fovLength * Math.cos(robotAngle - halfFOV),
            robotPose.getY() + fovLength * Math.sin(robotAngle - halfFOV),
            new Rotation2d(robotAngle - halfFOV)
        );
        
        Pose2d rightFOV = new Pose2d(
            robotPose.getX() + fovLength * Math.cos(robotAngle + halfFOV),
            robotPose.getY() + fovLength * Math.sin(robotAngle + halfFOV),
            new Rotation2d(robotAngle + halfFOV)
        );
        
        field.getObject("fov_left").setPose(leftFOV);
        field.getObject("fov_right").setPose(rightFOV);
    }

    @Override
    public double get_tx() {
        return limelightTable.getEntry("tx").getDouble(0.0);
    }

    @Override
    public double get_ty() {
        return limelightTable.getEntry("ty").getDouble(0.0);
    }

    @Override
    public double get_ta() {
        return limelightTable.getEntry("ta").getDouble(0.0);
    }

    @Override
    public boolean get_tv() {
        return limelightTable.getEntry("tv").getDouble(0.0) > 0.5;
    }
} 