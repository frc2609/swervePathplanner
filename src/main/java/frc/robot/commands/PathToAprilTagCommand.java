package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.LimelightHelpers;
import frc.robot.subsystems.CommandSwerveDrivetrain;

public class PathToAprilTagCommand extends Command {
    private final CommandSwerveDrivetrain drivetrain;
    private final String limelightName;
    private Command pathCommand;
    private boolean hasInitialized = false;
    private boolean isGeneratingPath = false;
    private Pose2d targetPose;

    public PathToAprilTagCommand(CommandSwerveDrivetrain drivetrain, String limelightName) {
        this.drivetrain = drivetrain;
        this.limelightName = limelightName;
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        hasInitialized = false;
        isGeneratingPath = false;
        
        // Get the current detected AprilTag pose
        LimelightHelpers.setPipelineIndex(limelightName, 0);
        boolean hasTarget = LimelightHelpers.getTV(limelightName);
        
        // Debug output...
        System.out.println("=== AprilTag Detection Debug ===");
        System.out.println("Has Target: " + hasTarget);
        System.out.println("Tag ID: " + LimelightHelpers.getFiducialID(limelightName));
        System.out.println("TX: " + LimelightHelpers.getTX(limelightName));
        System.out.println("TY: " + LimelightHelpers.getTY(limelightName));
        System.out.println("TA: " + LimelightHelpers.getTA(limelightName));
        
        double[] botpose = LimelightHelpers.getBotPose_wpiBlue(limelightName);
        System.out.println("Botpose Length: " + botpose.length);
        if (botpose.length >= 6) {
            System.out.println("Botpose: [" + 
                String.join(", ", java.util.Arrays.stream(botpose)
                    .mapToObj(String::valueOf)
                    .toArray(String[]::new)) + "]");
        }
        System.out.println("==============================");

        if (hasTarget && botpose.length >= 6) {
            targetPose = LimelightHelpers.toPose2D(botpose);
            SmartDashboard.putString("Target Pose", targetPose.toString());
            isGeneratingPath = true;
            hasInitialized = true;
        } else {
            System.out.println("No valid AprilTag pose detected!");
        }
    }

    @Override
    public void execute() {
        if (hasInitialized && isGeneratingPath) {
            // Generate path in execute loop
            pathCommand = drivetrain.getPathPlannerCommandToPose(targetPose);
            isGeneratingPath = false;
            pathCommand.schedule();
        }
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted) {
            System.out.println("Path to AprilTag was interrupted!");
            if (drivetrain.getCurrentCommand() != null) {
                System.out.println("Current drivetrain command: " + drivetrain.getCurrentCommand().getName());
            }
            if (pathCommand != null) {
                System.out.println("Path command status: " + (pathCommand.isFinished() ? "finished" : "not finished"));
            }
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