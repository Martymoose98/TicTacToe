package com;

public class Time
{
	/**
	 *  A second in nanoseconds
	 */
	public static final long SECOND_NS = 1000000000L;
	
	/**
	 *  A second in milliseconds
	 */
	public static final long SECOND_MS = 1000L;
	
	/**
	 *  Time delta
	 */
	public static double delta;
	
	/**
	 * Returns System.nanoTime()
	 * 
	 * @return current time in nanoseconds
	 */
	public static long getTime()
	{
		return System.nanoTime();
	}
}
