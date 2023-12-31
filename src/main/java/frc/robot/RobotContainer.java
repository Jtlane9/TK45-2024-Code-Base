package frc.robot;

import java.util.HashMap;

import edu.wpi.first.wpilibj2.command.*;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

import frc.robot.autos.AutoChooser;
import frc.robot.autos.AutoTrajectories;
import frc.robot.autos.eventMap;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */

public class RobotContainer 
{
    /* Subsystems */
    public final Swerve s_Swerve = new Swerve();

    /* Autonomous Selection */
    private final SendableChooser<Command> autoChooser;
    public Command autoCode = Commands.sequence(new PrintCommand("no auto selected"));

    /* Controllers */
    private final Joystick driver = new Joystick(0);
    private final Joystick operator = new Joystick(1);

    /* Drive Controls */  // For Swerve
    private final int translationAxis = XboxController.Axis.kLeftY.value;
    private final int strafeAxis = XboxController.Axis.kLeftX.value;
    private final int rotationAxis = XboxController.Axis.kRightX.value;

    /* Driver Buttons */
    private final JoystickButton zeroGyro = new JoystickButton(driver, XboxController.Button.kB.value);
    private final JoystickButton robotCentric = new JoystickButton(driver, XboxController.Button.kLeftBumper.value);

    /* Variables */
    boolean driveStatus = false;
    double setPoint;

    // TODO - Add Pathplanner Imports JSON
    // TODO - Replace with actual commands
    /* Register Names Commands */
    NamedCommands.registerCommand("autoBalance", swerve.autoBalanceCommand());
    NamedCommands.registerCommand("exampleCommand", exampleSubsystem.exampleCommand());
    NamedCommands.registerCommand("someOtherCommand", new SomeOtherCommand());
    
    /* PathPlanner Setup */
    public static final HashMap<String, Command> AUTO_EVENT_MAP = new HashMap<>();

    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() 
    {
      s_Swerve.setDefaultCommand
      (
        new TeleopSwerve
        (
          s_Swerve, 
          () -> -driver.getRawAxis(translationAxis), 
          () -> -driver.getRawAxis(strafeAxis), 
          () -> -driver.getRawAxis(rotationAxis), 
          () -> robotCentric.getAsBoolean()
        )
      );

      // Configure the button bindings
      configureButtonBindings();

      autoChooser = AutoBuilder.buildAutoChooser();
      SmartDashboard.putData("Auto Chooser", autoChooser);
    }

    // TODO - Should be fixed once imports are added
    public Command getAutonomousCommand() 
    {
      return autoChooser.getSelected();
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */

    private void configureButtonBindings() 
    {
        //Driver Buttons (and op buttons) 
        zeroGyro.onTrue(new InstantCommand(() -> s_Swerve.zeroGyro()));
    }

    public void printValues()
    {
        SmartDashboard.putNumber("yaw", s_Swerve.gyro.getYaw());
        SmartDashboard.putNumber("pitch", s_Swerve.gyro.getPitch());
        SmartDashboard.putNumber("roll", s_Swerve.gyro.getRoll());
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() 
    {
      Constants.gyroOffset = s_Swerve.gyro.getPitch();
      //s_Swerve.zeroGyro();
      s_Swerve.gyro.setYaw(180);
      return chooser.getCommand();
    }
}