package frc.robot.subsystems;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.LimelightTarget_Retro;

public class Limelight extends SubsystemBase{
    private String name;
    private JSONObject map;
    private JSONArray fiducials;
    public Limelight(String name){
        this.name = name;
          String fileLocation =  String.format("%s%s", Filesystem.getDeployDirectory(), "/frc2025r2.json");
        SmartDashboard.putString("print", fileLocation);
        try (FileReader reader = new FileReader(fileLocation)) {
            JSONParser jsonParser = new JSONParser();
            map = (JSONObject) jsonParser.parse(reader);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        fiducials = (JSONArray) map.get("fiducials");
    }

    public double get_ty(){
        return LimelightHelpers.getTY(this.name);
    }

    public double get_tx(){
        return LimelightHelpers.getTX(this.name);
    }
    public Pose2d get_Pose2d(){
        double ID = LimelightHelpers.getFiducialID(this.name);
        JSONObject fiducial = (JSONObject) fiducials.get((int)ID);
        double[] transform = (double [])fiducial.get("transform");
        double yaw = Math.atan2(transform[1], transform[0]);
        Pose2d pose = new Pose2d(transform[3], transform[7], new Rotation2d(yaw));
        SmartDashboard.putString("pose", pose.toString());
        return pose;
    

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
