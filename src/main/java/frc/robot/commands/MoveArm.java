package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Arm;

public class MoveArm extends Command {
    private final Arm arm;

    public MoveArm(Arm arm) {
        this.arm = arm;
        addRequirements(arm);
    }
    @Override 
    public void execute() {
        arm.setVoltage(2);
    }

    @Override 
    public void end(boolean interrupted) {
        arm.stop();
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
