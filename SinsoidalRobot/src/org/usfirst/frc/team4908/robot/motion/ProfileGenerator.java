package org.usfirst.frc.team4908.robot.motion;

public class ProfileGenerator 
{
	
	private double kT;
	private double k1;
	private double k2;
	private double k3;
	
	private double velocity;
	private double acceleration;
	
	double maxStep;
	
	Setpoint setpoint;
	
	public ProfileGenerator(double velocity, double target)
	{
		this.velocity = velocity;
		setpoint = new Setpoint();
		generate(velocity, target);
	}
	
	public void generate(double velocity, double target)
	{
		acceleration = ((Math.pow(Math.PI*velocity, 2))/(2.0*Math.PI*target));
		
		kT = Math.sqrt((2.0*Math.PI*target)/acceleration);
		k1 = ((2.0*Math.PI)/kT);
		k2 = (acceleration/k1);
		k3 = (1.0/k1);
		
		setpoint.acceleration = 0;
		setpoint.velocity = 0;
		setpoint.position = 0;
		
		System.out.println(kT + "\t" + k1 + "\t"  + k2  + "\t"+ k3);
	}
	
	public Setpoint getValues(double time)
	{
		setpoint.acceleration = (acceleration*Math.sin(k1*time));
		setpoint.velocity = (k2*(1-(Math.cos(k1*time))));
		setpoint.position = (k2*(time-(k3*Math.sin(k1*time))));
		
		return setpoint;
	}
	
	public double getTotalTime()
	{
		return kT;
	}
	
	public double getAcceleration()
	{
		return acceleration;
	}
}
