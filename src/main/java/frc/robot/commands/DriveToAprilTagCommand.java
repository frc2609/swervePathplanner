package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.LimelightHelpers;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import com.ctre.phoenix6.swerve.SwerveRequest;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveToAprilTagCommand extends Command {
    private final CommandSwerveDrivetrain drivetrain;
    private final String limelightName;
    private final SwerveRequest.FieldCentric drive;
    
    // PID constants
    private final double kP_Forward = 0.1;  // Forward/backward control
    private final double kP_Strafe = 0.1;   // Left/right control
    private final double kP_Rotation = 0.1; // Rotation control

    public DriveToAprilTagCommand(CommandSwerveDrivetrain drivetrain, String limelightName) {
        this.drivetrain = drivetrain;
        this.limelightName = limelightName;
        this.drive = new SwerveRequest.FieldCentric()
            .withDeadband(4.73 * 0.1)
            .withRotationalDeadband(2 * 0.1);
        
        addRequirements(drivetrain);
    }

    private double calculateForwardSpeed() {
        double targetingForwardSpeed = LimelightHelpers.getTY(limelightName) * kP_Forward;
        // Convert to meters per second
        // targetingForwardSpeed *= 0.75;
        return targetingForwardSpeed;
    }

    private double calculateStrafeSpeed() {
        double targetingStrafeSpeed = LimelightHelpers.getTX(limelightName) * kP_Strafe;
        // Convert to meters per second
        // targetingStrafeSpeed *= 0.75;
        return targetingStrafeSpeed;
    }

    private double calculateRotationSpeed() {
        double targetingRotationSpeed = LimelightHelpers.getTX(limelightName) * kP_Rotation;
        // Convert to radians per second
        // targetingRotationSpeed *= 0.75;
        return targetingRotationSpeed;
    }

    @Override
    public void execute() {
        boolean hasTarget = LimelightHelpers.getTV(limelightName);
        
        if (hasTarget) {
            // Calculate speeds
            double forwardSpeed = calculateForwardSpeed();
            double strafeSpeed = calculateStrafeSpeed();
            double rotationSpeed = calculateRotationSpeed();
            
            // Debug output
            SmartDashboard.putNumber("Forward Speed", forwardSpeed);
            SmartDashboard.putNumber("Strafe Speed", strafeSpeed);
            SmartDashboard.putNumber("Rotation Speed", rotationSpeed);
            
            // Apply to drivetrain
            drivetrain.setControl(drive
                .withVelocityX(forwardSpeed)
                .withVelocityY(strafeSpeed)
                .withRotationalRate(rotationSpeed));
        } else {
            // No target visible, stop moving
            drivetrain.setControl(drive
                .withVelocityX(0)
                .withVelocityY(0)
                .withRotationalRate(0));
        }
    }

    @Override
    public void end(boolean interrupted) {
        // Stop the robot
        drivetrain.setControl(drive
            .withVelocityX(0)
            .withVelocityY(0)
            .withRotationalRate(0));
    }

    @Override
    public boolean isFinished() {
        // Optional: finish when close enough to target
        if (!LimelightHelpers.getTV(limelightName)) {
            return false;
        }
        
        double tx = Math.abs(LimelightHelpers.getTX(limelightName));
        double ty = Math.abs(LimelightHelpers.getTY(limelightName));
        double ta = LimelightHelpers.getTA(limelightName);
        
        // Adjust these thresholds as needed
        return tx < 1.0 && ty < 1.0 && ta > 5.0;
    }
} 