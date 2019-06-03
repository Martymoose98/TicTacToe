package com;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Screen extends JFrame
{
	/**
	 *  Width of the JFrame
	 */
	private int width;
	
	/**
	 * 	Height of the JFrame
	 */
	private int height;
	
	/**
	 *  Serial version unique identifier for serialization
	 */
	private static final long serialVersionUID = 2530250542144261582L;

	/**
	 * Constructor	
	 * 
	 * @param display
	 * 			a valid display object
	 * 
	 * @param title
	 * 			a title
	 * 
	 * @param width
	 * 			the width of the window
	 * 
	 * @param height
	 *			the height of the window 
	 */
	public Screen(Display display, String title, int width, int height)
	{	
		this.width = width;
		this.height = height;
		Dimension size = new Dimension(width, height);
		
		add(display); // add the display to the JFrame (Screen)
		pack();	// pack the JFrame
		setTitle(title); // set the title
		setResizable(false); // set resizable to false so no one can resize the JFrame with the mouse
		setSize(size); // set the size
		setPreferredSize(size);	// set the preferred size
		setMaximumSize(size); // set the maximum size
		setMinimumSize(size); // set the minimum size
		setLocationRelativeTo(null); // set the spawn location to center screen
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // so the X button actually terminates the program

		setVisible(true); // make the JFrame visible
		
		requestFocus(); // request focus
	}
	
	/**
	 * Getter for the width
	 * 
	 * @return the JFrame's width
	 */
	public int getWidth()
	{
		return this.width;
	}
	
	/**
	 *  Getter for the height
	 *  
	 * @return the JFrame's height
	 */
	public int getHeight()
	{
		return this.height;
	}
}
