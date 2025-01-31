package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.LimelightTarget_Retro;

public class Limelight extends SubsystemBase{
    private String name;
    public Limelight(String name){
        this.name = name;
    }

    public double get_ty(){
        return LimelightHelpers.getTY(this.name);
    }

    public double get_tx(){
        return LimelightHelpers.getTX(this.name);
    }
    
    // public double get_yaw(){
    //     LimelightHelpers.PoseEstimate mt1 = LimelightHelpers.getBotPoseEstimate_wpiBlue(this.name);
        
    //     return mt1.pose.getRotation().getDegrees();
    // }


    @Override
    public void periodic(){
        // SmartDashboard.putNumber("get yaw", get_yaw());
    }

}
