package frc.robot.commands;

import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.LimelightHelpers;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Limelight;

public class PathToAprilTagCommand extends Command {
    private final CommandSwerveDrivetrain drivetrain;
    private final Limelight limelight;
    private Command pathCommand;

    public PathToAprilTagCommand(CommandSwerveDrivetrain drivetrain, Limelight limelight) {
        this.drivetrain = drivetrain;
        this.limelight = limelight;
        addRequirements(drivetrain, limelight);
    }

    @Override
    public void initialize() {
        // Get the current detected AprilTag pose
        var validTarget = limelight.get_tv();
        System.out.println(validTarget);
        double[] tagYaw = LimelightHelpers.getBotPose_wpiBlue("limelight-seaweed");
        if (validTarget) {    
            //An example trajectory to follow. All units in meters.
            // TrajectoryConfig config =
            // new TrajectoryConfig(
            //         2,
            //         4);
            //     // Add kinematics to ensure max speed is actually obeyed
            //     // .setKinematics(DriveConstants.kDriveKinematics)
            //     // Apply the voltage constraint
            //     // .addConstraint(autoVoltageConstraint);

            // Trajectory exampleTrajectory =
            //     TrajectoryGenerator.generateTrajectory(
            //         // Start at the origin facing the +X direction
                    
            //         new Pose2d(tagYaw[0], tagYaw[1], new Rotation2d(tagYaw[5])),
            //         // Pass through these two interior waypoints, making an 's' curve path
            //         List.of(new Translation2d(1, 1), new Translation2d(2, -1)),
            //         // End 3 meters straight ahead of where we started, facing forward
            //         limelight.get_Pose2d(),
            //         // Pass config
            //         config);

                // Get the first detected tag's pose in field coordinates
                Pose2d targetPose = limelight.get_Pose2d();
    
                // Generate path to the target pose
                pathCommand = drivetrain.getPathPlannerCommandToPose(targetPose);
    
                // Schedule the path command
                pathCommand.schedule();

            
        } else {
            // If no tags found, end the command
            System.out.println("No AprilTags detected!");
            cancel();
        }
    }

    @Override
    public void execute() {
        // Path following is handled by the scheduled path command

    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted) {
            System.out.println("Path to AprilTag was interrupted!");
        }
        if (pathCommand != null) {
            pathCommand.cancel();
        }
    }

    @Override
    public boolean isFinished() {
        return pathCommand != null && pathCommand.isFinished();
    }
}