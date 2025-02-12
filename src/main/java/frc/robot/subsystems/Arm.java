package frc.robot.subsystems;
import com.revrobotics.AbsoluteEncoder;

import edu.wpi.first.wpilibj.DutyCycle;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Constants;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.VoltageOut;

public class Arm extends SubsystemBase{
    /**
     * The motor controller for the robot's arm subsystem.
     */
    private com.ctre.phoenix6.hardware.TalonFX armMotor;
    private final VoltageOut voltageCtrlReq = new VoltageOut(2);
   private DigitalInput input;
    private DutyCycleEncoder encoder;
    
     public Arm() {
        armMotor = new TalonFX(Constants.armMotorPort);
        input = new DigitalInput(5);
        encoder = new DutyCycleEncoder(input);
        armMotor.setNeutralMode(NeutralModeValue.Coast);
        armMotor.setInverted(false);
    }

    public class Constants {

        public static final int armMotorPort = 50; // Add this line with the appropriate port number

    }

    @Override 
    public void periodic() {
        // This method will be called once per scheduler run
        SmartDashboard.putNumber("Arm Encoder", encoder.get());
        SmartDashboard.putBoolean("Arm Moving", false);
        SmartDashboard.putNumber("voltage", armMotor.getMotorVoltage().getValueAsDouble());

    }

    public void move() {
        SmartDashboard.putBoolean("Arm Moving", true);
        DutyCycleOut dCycleOut = new DutyCycleOut(3);
        dCycleOut.Output = 3;
        armMotor.setControl(dCycleOut.withOutput(1));
        SmartDashboard.putNumber("voltage", armMotor.getMotorVoltage().getValueAsDouble());
    }

    public void stop() {
        SmartDashboard.putBoolean("Arm Moving", false);
        armMotor.stopMotor();

    }

    
    public void setVoltage(double volts) {
        armMotor.setControl(voltageCtrlReq.withOutput(volts));
    }
}