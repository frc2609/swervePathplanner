package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.LimelightHelpers;
import frc.robot.subsystems.CommandSwerveDrivetrain;

public class PathToAprilTagCommand extends Command {
    private final CommandSwerveDrivetrain drivetrain;
    private final String limelightName;
    private Command pathCommand;

    public PathToAprilTagCommand(CommandSwerveDrivetrain drivetrain, String limelightName) {
        this.drivetrain = drivetrain;
        this.limelightName = limelightName;
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        // Get the current detected AprilTag pose
        var results = LimelightHelpers.getLatestResults(limelightName);

        if (results.targets_Fiducials.length > 0) {
            // Get the first detected tag's pose in field coordinates
            Pose2d targetPose = results.targets_Fiducials[0].getRobotPose_FieldSpace2D();

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