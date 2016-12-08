
package org.usfirst.frc.team4908.robot;

import java.text.DecimalFormat;

import org.usfirst.frc.team4908.robot.drive.DuxDrive;
import org.usfirst.frc.team4908.robot.motion.ProfileGenerator;
import org.usfirst.frc.team4908.robot.motion.Setpoint;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends IterativeRobot 
{	
	public static double startTime;
	public static double currentTime;
	public static int count;
	
	public static DecimalFormat df;
	
	
	Encoder rightEncoder;
    Encoder leftEncoder;
    
    CANTalon frontLeft;
	CANTalon frontRight;
	CANTalon backLeft;
	CANTalon backRight;
		
	Joystick stick1;
    Joystick stick2;
    
    DuxDrive drive;
    
    boolean wasPressed = false;
    
    int driveCommand;
    
    double time;
    
    public double maxLeft = 8.6;
    public double maxRight = 9.1;
    public double currentLeft;
    public double currentRight;
    
    private static final double rearLeftDiameter = 7.202;
    private static final double rearRightDiameter = 7.2115;
    private static final double frontLeftDiameter = 0;
    private static final double frontRightDiameter = 0;
    
    ProfileGenerator pg;
    
    double kA;
    double kV;
    
    double kP;
    double kI;
    double kD;
    
    double kVD;
    double kAD;
	
    public void robotInit() 
    {
    	frontLeft = new CANTalon(1);
    	frontRight = new CANTalon(2);
    	backLeft = new CANTalon(4);
    	backRight = new CANTalon(3);        
        
        stick1 = new Joystick(0);
        stick2 = new Joystick(1);
        
        leftEncoder = new Encoder(3, 2, true);
        leftEncoder.setDistancePerPulse(((2*Math.PI) / 1440)*(rearLeftDiameter/2.0)); // radians per pulse
        leftEncoder.setSamplesToAverage(64);
        rightEncoder = new Encoder(7, 6, false);
        rightEncoder.setDistancePerPulse(((2*Math.PI) / 1440)*(rearRightDiameter/2.0));
        rightEncoder.setSamplesToAverage(64);
        
    	drive = new DuxDrive(frontLeft, backLeft, frontRight, backRight);

    	pg = new ProfileGenerator(3.0, 10.0);
    	
    	df = new DecimalFormat("0.000");
    	
    	kA = 1.0/pg.getAcceleration();
    	kV = 1.0/Math.min(maxLeft, maxRight);
    	
    	driveCommand = 0; 
    	
    	kVD = 0.5;
    	kAD = 0.5;

    }
    
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() 
    {
    	currentLeft = leftEncoder.getRate();
        currentRight = rightEncoder.getRate();

    	
        if(stick2.getRawButton(1) && !wasPressed)
        {
        	rightEncoder.reset();
        	leftEncoder.reset();
        	
        	wasPressed = true;
        	driveCommand = 1;
        	startTime = System.currentTimeMillis()/1000.0;
        }
        
        if(stick1.getRawButton(1))
        {
        	driveCommand = 0;
        	wasPressed = false;
        }
        
        switch(driveCommand)
        {
        	case 0:
        		drive.duxArcadeDrive(stick1.getRawAxis(1), stick2.getRawAxis(2));
        		break;
        	case 1:
        		mpDrive((System.currentTimeMillis()/1000.0) - startTime);
        		time = (System.currentTimeMillis()/1000.0) - startTime;
        		break;
        	default:
        		break;
        }
        
        
        if(currentLeft > maxLeft)
        {
        	maxLeft = currentLeft;
        }
        
        if(currentRight > maxRight)
        {
        	maxRight = currentRight;
        }

    }
    
    public void mpDrive(double time)
    {
    	if(time <= pg.getTotalTime())
    	{
    		drive.duxArcadeDrive(kV*pg.getValues(time).velocity + kA*pg.getValues(time).acceleration, 0);
    		if (count > 10)
    		{
    			debugTheStuff(pg.getValues(time));
    			count = 0;
    		}		
    		count ++;
    	}
    	else
    	{
    		driveCommand = 0;
    		wasPressed = false;
    	}
    }
    
    public void debugTheStuff(Setpoint currentSetpoint)
    {
        System.out.println("Time: " + df.format(time) + "\tTotal Time: " + df.format(pg.getTotalTime()) +

        "\nTarget Speed: " + df.format(currentSetpoint.velocity) + "\tAverage Speed: " + df.format((leftEncoder.getRate() + rightEncoder.getRate())/2.0)

        + "\nTarget Distance: " + df.format(currentSetpoint.position) + "\tLeft Distance: " + df.format(leftEncoder.getDistance()) + "\tRight Distance: " + df.format(rightEncoder.getDistance()));
    }
}
