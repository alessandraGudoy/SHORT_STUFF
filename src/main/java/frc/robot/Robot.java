package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;
  private Command lockSwerveCommand;

  private RobotContainer m_robotContainer;

  private Compressor compressor;
  // private Camera wideview;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    compressor = new Compressor(PneumaticsModuleType.REVPH);
    compressor.enableDigital();
    m_robotContainer = new RobotContainer();
    lockSwerveCommand =  m_robotContainer.getSwerveLock();

    // wideview = new Camera();
    // wideview.init();
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {
    LimelightHelpers.setPipelineIndex("limelight", 7);
    RobotContainer.lights.off();
  }

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    RobotContainer.swerveSubsystem.resetNavx();
    RobotContainer.swerveSubsystem.straightenWheels();
    RobotContainer.swerveSubsystem.stopModules();
    LimelightHelpers.setCameraMode_Processor("limelight");
    LimelightHelpers.setPipelineIndex("limelight", 7);
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    if(Timer.getMatchTime() > 0 && Timer.getMatchTime() < 0.75){
      m_autonomousCommand.cancel();
      lockSwerveCommand.schedule();
    }
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    LimelightHelpers.setCameraMode_Driver("limelight");
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    lockSwerveCommand.cancel();
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    if(0 <= Timer.getMatchTime() && Timer.getMatchTime() <= 15){
      RobotContainer.lights.poppy();
    }
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
    
  }

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
