package org.usfirst.frc.team4908.robot.drive;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;

public class DuxDrive extends RobotDrive
{
	private double maxLeft = 8;
	private double maxRight = 9;
	
	
	public DuxDrive(SpeedController left, SpeedController right)
	{
		super(left, right);
	}

	public DuxDrive(SpeedController frontLeft, SpeedController frontRight, SpeedController rearLeft, SpeedController rearRight)
	{
		super(frontLeft, frontRight, rearLeft, rearLeft);
	}
	
	public void duxArcadeDrive(double moveValue, double rotateValue)
	{
		double leftSpeed;
		double rightSpeed;

		if(moveValue > 1.0)
			moveValue = 1.0;
		if(moveValue < -1.0)
			moveValue = -1.0;
		
		if(rotateValue > 1.0)
			rotateValue = 1.0;
		if(rotateValue < -1.0)
			rotateValue = -1.0;
		
		if (moveValue > 0.0) 
		{
		    if (rotateValue > 0.0) 
		    {
		    	leftSpeed = moveValue - rotateValue;
		    	rightSpeed = Math.max(moveValue, rotateValue);
		    }
		    else 
		    {
		    	leftSpeed = Math.max(moveValue, -rotateValue);
		    	rightSpeed = moveValue + rotateValue;
		    }
		}
		else 
		{
		    if (rotateValue > 0.0) 
		    {
		    	leftSpeed = -Math.max(-moveValue, rotateValue);
		    	rightSpeed = moveValue + rotateValue;
		    } 
		    else 
		    {
		    	leftSpeed = moveValue - rotateValue;
		    	rightSpeed = -Math.max(-moveValue, -rotateValue);
		    }
		}
		
		if(maxLeft < maxRight)
		{
			rightSpeed *= (maxLeft/maxRight);
		}
		
		if(maxRight < maxLeft)
		{
			leftSpeed *= (maxRight/maxLeft);
		}
		
		setLeftRightMotorOutputs(-leftSpeed, -rightSpeed);
	}

	public void setMaxLeft(double max)
	{
		maxLeft = max;
	}
	
	public void setMaxRight(double max)
	{
		maxRight = max;
	}
}
