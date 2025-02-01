package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import com.ctre.phoenix6.swerve.SwerveRequest;

public class TurnToAngleCommand extends Command {
    private final CommandSwerveDrivetrain drivetrain;
    private final double targetAngleDegrees;
    private final PIDController rotationController;
    private final SwerveRequest.FieldCentric drive;

    public TurnToAngleCommand(CommandSwerveDrivetrain drivetrain, double targetAngleDegrees) {
        this.drivetrain = drivetrain;
        this.targetAngleDegrees = targetAngleDegrees;
        
        // PID controller for rotation
        rotationController = new PIDController(0.09, 0, 0.001);
        rotationController.enableContinuousInput(-180, 180);
        rotationController.setTolerance(2.0); // 2 degrees tolerance
        
        drive = new SwerveRequest.FieldCentric()
            .withDeadband(0.1)
            .withRotationalDeadband(0.1);

        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        rotationController.reset();
    }

    @Override
    public void execute() {
        double currentAngle = drivetrain.getState().Pose.getRotation().getDegrees();
        double rotationSpeed = rotationController.calculate(currentAngle, targetAngleDegrees);
        
        // Increased speed limit from 0.5 to 0.8 (80% max speed)
        rotationSpeed = Math.min(Math.max(rotationSpeed, -1), 1);
        
        // Debug output
        SmartDashboard.putNumber("Target Angle", targetAngleDegrees);
        SmartDashboard.putNumber("Current Angle", currentAngle);
        SmartDashboard.putNumber("Rotation Speed", rotationSpeed);

        // Apply to drivetrain (zero translation, only rotation)
        drivetrain.setControl(drive
            .withVelocityX(0)
            .withVelocityY(0)
            .withRotationalRate(rotationSpeed));
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.setControl(drive
            .withVelocityX(0)
            .withVelocityY(0)
            .withRotationalRate(0));
    }

    @Override
    public boolean isFinished() {
        return rotationController.atSetpoint();
    }
} 