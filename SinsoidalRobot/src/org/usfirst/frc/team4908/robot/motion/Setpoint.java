package org.usfirst.frc.team4908.robot.motion;

public class Setpoint 
{
	public double acceleration, velocity, position;
	
	public String toString()
	{
		return acceleration + " " + velocity + " " + position; 
	}
}
