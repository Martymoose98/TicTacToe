package com;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Display extends Canvas implements Runnable
{
	/**
	 *  Window Title 
	 */
	public static final String TITLE = "Tic Tac Toe";
	
	/**
	 *  Window Width
	 */
	public static final int WIDTH = 800;
	
	/**
	 *  Window Height
	 */
	public static final int HEIGHT = 600;
	
	/**
	 *  Frames per second cap
	 */
	public static final double FRAME_CAP = 5000.0;
	
	/**
	 *  FPS string font size
	 */
	public static final int FPS_FONT_SIZE = 48;
	
	/**
	 *  FPS string font
	 */
	public static final Font FPS_FONT = new Font("Freestyle Script", Font.BOLD, FPS_FONT_SIZE);
	
	private Screen screen;
	private Input input;
	private Game game;
	private BufferedImage frame;
	private Thread mainThread;

	private boolean isRunning;
	private int[] pixels;
	private int fps;
	private float fpsHue;
	
	/**
	 *  Serial version unique identifier for serialization
	 */
	private static final long serialVersionUID = 7548736370451879895L;

	/**
	 *  Constructor
	 */
	public Display()
	{
		isRunning = false;		
		screen = new Screen(this, TITLE, WIDTH, HEIGHT);
		game = new Game(50, 50, 140, false, Game.GAME_PIECE_X);
		frame = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) frame.getRaster().getDataBuffer()).getData();
		input = new Input(game);
		addMouseListener(input);
		start();
	}

	/**
	 *  Main game loop
	 */
	@Override
	public void run()
	{	
		boolean ticked = false;
		int frames = 0;
		long frameCounter = 0L;
		final double frameTime = 1.0 / FRAME_CAP;
		long previousTime = Time.getTime();
		double unprocessedTime = 0.0;
		
		while (isRunning)
		{
			long startTime = Time.getTime();
			long elapsedTime = startTime - previousTime;
			previousTime = startTime;
			ticked = false;
			
			unprocessedTime += elapsedTime / (double) Time.SECOND_NS;
			frameCounter += elapsedTime;
				
			// while there is time to be processed and update the 
			// game accordingly
			while (unprocessedTime > frameTime)
			{
				ticked = true; // the game is updated
				unprocessedTime -= frameTime;
				Time.delta = frameTime;
				tick(); // update the game

				// at least every second reset the frame counter
				if (frameCounter >= Time.SECOND_NS)
				{
					fps = frames;
					frames = 0;
					frameCounter = 0L;
				}
			}
			
			// if the game updated render the new frame
			// and add one to the frame count
			if (ticked)
			{
				render();
				frames++;
			}
		}
	}
	
	/**
	 *	This function gets called to the constructor to start 
	 *	the game calls run method since we implement runnable
	 */
	public void start()
	{
		if (isRunning)
			return;
		
		isRunning = true;
		mainThread = new Thread(this, "Game Thread");
		mainThread.start();
	}
	
	/**
	 *  Update method 
	 */
	public void tick()
	{
		game.update();
	}
	
	/**
	 *  Render method
	 */
	public void render()
	{
		BufferStrategy buffer = getBufferStrategy();
		
		// only create, if buffer is invalid
		if (buffer == null)
		{
			createBufferStrategy(2); //2d, so 2 buffers
			return;
		}
		
		// get the screen dimension
		int width = screen.getWidth();
		int height = screen.getHeight();
		
		// clear the screen
		for (int i = 0; i < width * height; ++i)
			pixels[i] = 0;
		
		// obtain a Graphics object to paint with
		Graphics g = buffer.getDrawGraphics();
		
		// draw the frame
		g.drawImage(frame, 0, 0, width, height, null);
		
		if (fpsHue >= 1.0f)
			fpsHue = 0.0f;
		
		fpsHue += 0.001f;
		
		g.setColor(Color.getHSBColor(fpsHue, 1.f, 1.f));	
		g.setFont(FPS_FONT);
		g.drawString("FPS " + fps, width - 170, 50);	
	
		game.render(g); // render the game
		
		g.dispose(); // destroy the Graphics object
		buffer.show(); // present the buffer
	}
	
	/**
	 * Main Function  (Entry Point)
	 * 
	 * @param args
	 * 			command line arguments (if any)
	 */
	public static void main(String[] args)
	{
		new Display(); // create a new display
	}
}
