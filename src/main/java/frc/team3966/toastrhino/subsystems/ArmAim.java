package frc.team3966.toastrhino.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team3966.toastrhino.RobotMap;
import frc.team3966.toastrhino.RobotModule;

/**
 *
 */
public class ArmAim extends Subsystem {

  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  Encoder Aenc;
  
  private boolean PIDenabled = true;

  PIDController armHeight;

  class armMotor extends VictorSP {
    public armMotor(int channel) {
      super(channel);
    }
    @Override
    public void pidWrite(double output) {
      super.set(output / 300.0);
    }
  }

  VictorSP Amotor = new armMotor(RobotMap.Amotor);

  public ArmAim() {
    try {
      Aenc = new Encoder(RobotMap.AencH, RobotMap.AencL);
      armHeight = new PIDController(0.1, 0.0, 0.0, Aenc, Amotor);
      Aenc.setPIDSourceType(PIDSourceType.kRate);
      armHeight.setOutputRange(-400, 400);
      armHeight.setContinuous(false);
      Aenc.reset();
      Aenc.setReverseDirection(false);
      armHeight.enable();
    } catch (UnsatisfiedLinkError ex) {
      RobotModule.logger.error("Arm encoder no link.");
    }
    Amotor.setInverted(true);

  }

  public void dash_all() {
    SmartDashboard.putData("Amotor", Amotor);
    if (Aenc != null) SmartDashboard.putData("Arm Encoder", Aenc);
    if (armHeight != null) SmartDashboard.putData("ArmPID", armHeight);
  }

  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    //setDefaultCommand(new MySpecialCommand());
  }

  protected void setAmotor(double speed) {
    Amotor.set(speed);
  }

  public void setHeightRelative(double speed) {
    if (armHeight != null && PIDenabled) armHeight.setSetpoint(speed * 200);
    else if (PIDenabled == false) {
      setAmotor(speed);
    }
  }
  
  public void enablePID(boolean enabled) {
    PIDenabled = enabled;
    if (PIDenabled) {
      if (armHeight != null) armHeight.enable();
    } else {
      if (armHeight != null) armHeight.disable();
    }
  }
}
