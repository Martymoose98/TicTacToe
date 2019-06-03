package com;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

/**
 * 
 * @author Martin
 * @version 1.1
 */
public class GameBoard
{
	public int x;
	public int y;
	public int size;
	public int dividerLength;
	public int dividerThickness;
	private int winLineThickness;

	private Rectangle[][] obb;
	private byte[][] board;

	/**
	 * Constructor
	 * 
	 * @param x
	 *            the position of the board on the x axis
	 *            
	 * @param y
	 *            the position of the board on the y axis
	 * 
	 * @param size
	 *            square size in pixels
	 */
	public GameBoard(int x, int y, int size)
	{
		this.x = x;
		this.y = y;
		this.size = size;
		this.dividerThickness = size / 5;
		this.dividerLength = (size * 3) + (this.dividerThickness * 2);
		this.winLineThickness = this.dividerThickness / 10;
		this.obb = new Rectangle[3][3];
		this.board = new byte[3][3];
		initOBB();
		reset();
	}

	/**
	 *  Initializes the oriented bounding boxes
	 */
	private void initOBB()
	{
		for (int y = 0; y < this.obb.length; ++y)
			for (int x = 0; x < this.obb[y].length; ++x)
				this.obb[x][y] = new Rectangle(this.x + x * (this.size + this.dividerThickness), this.y + y * (this.size + this.dividerThickness), this.size, this.size);
	}

	/**
	 * Places a game piece on the board but fails if the coordinates are not valid
	 * 
	 * @param x
	 * 			the x position of the piece to be placed relative to the board
	 * 
	 * @param y
	 * 			the y position of the piece to be placed relative to the board
	 * 
	 * @param type
	 * 			the type of game piece to be placed
	 * 
	 * @return if the place succeeded
	 */
	public boolean placePiece(int x, int y, byte type)
	{
		if (type == Game.GAME_PIECE_INVALID || x > 2 || y > 2 || this.board[x][y] != Game.GAME_PIECE_INVALID)
			return false;

		this.board[x][y] = type;
		return true;
	}

	/**
	 * Resets the board to a neutral "new game" state
	 */
	public void reset()
	{
		for (int y = 0; y < this.board.length; ++y)
			for (int x = 0; x < this.board[y].length; ++x)
				this.board[x][y] = Game.GAME_PIECE_INVALID;
	}

	/**
	 *  Draw Method for the board and game pieces
	 * 
	 * @param g 
	 * 			a valid Graphics object
	 * 
	 * @param color
	 * 			the color of the board
	 */
	public void draw(Graphics g, Color color)
	{
		// set the color
		g.setColor(color);

		// draw the board
		g.drawRect(x, y + size, dividerLength, dividerThickness);
		g.drawRect(x, y + (2 * size) + dividerThickness, dividerLength, dividerThickness);

		g.drawRect(x + size, y, dividerThickness, dividerLength);
		g.drawRect(x + (2 * size) + dividerThickness, y, dividerThickness, dividerLength);

		// draw the game pieces
		for (int y = 0; y < this.board.length; ++y)
		{
			for (int x = 0; x < this.board[y].length; ++x)
			{
				switch (this.board[x][y])
				{
				default:
					continue;
				case Game.GAME_PIECE_X:
					g.setFont(Game.GAME_PIECE_FONT);
					g.setColor(Color.BLUE);
					g.drawString("X", this.x + calculateOffset(Game.GAME_PIECE_X_LAYOUT, false) + x * (this.size + this.dividerThickness),
							this.y + calculateOffset(Game.GAME_PIECE_X_LAYOUT, true) + y * (this.size + this.dividerThickness));
					break;
				case Game.GAME_PIECE_O:
					g.setFont(Game.GAME_PIECE_FONT);
					g.setColor(Color.RED);
					g.drawString("O", this.x + calculateOffset(Game.GAME_PIECE_O_LAYOUT, false) + x * (this.size + this.dividerThickness),
							this.y + calculateOffset(Game.GAME_PIECE_O_LAYOUT, true) + y * (this.size + this.dividerThickness));
					break;
				}
			}
		}
	}

	/**
	 * Returns the offset to center the text in a board square
	 * 
	 * @param layout
	 * 				a font's text layout
	 * 
	 * @param vertical
	 * 				is the font vertical or not
	 * 
	 * @return the offset to center the text in a board square
	 */
	private int calculateOffset(TextLayout layout, boolean vertical)
	{
		Rectangle2D bounds = layout.getBounds();
		return (int) ((vertical) ? this.size / 2 - bounds.getHeight() / 2 + bounds.getHeight() : this.size / 2 - bounds.getWidth() / 2);
	}

	/**
	 * Returns the win line as a renderable polygon
	 * 
	 * @return a polygon that represents the win line
	 */
	public Polygon getWinLine()
	{
		Polygon line = null;

		// horizontal
		for (int row = 0; row < this.board.length; ++row)
		{
			if (this.board[0][row] == this.board[1][row] && this.board[1][row] == this.board[2][row])
			{
				int x0 = this.obb[0][row].x;
				int y0 = (int) this.obb[0][row].getCenterY() - this.winLineThickness / 2;
				int x1 = x0 + ((this.obb[2][row].x + this.obb[2][row].width) - this.obb[0][row].x);
				int y1 = y0 + this.winLineThickness;

				line = new Polygon(new int[] { x0, x0, x1, x1 }, new int[] { y0, y1, y1, y0 }, 4);
				return line;
			}
		}

		// vertical
		for (int column = 0; column < this.board.length; ++column)
		{
			if (this.board[column][0] == this.board[column][1] && this.board[column][1] == this.board[column][2])
			{
				int x0 = (int) this.obb[column][0].getCenterX() - this.winLineThickness / 2;
				int y0 = this.obb[column][0].y;
				int x1 = x0 + this.winLineThickness;
				int y1 = y0 + ((this.obb[column][2].y + this.obb[column][2].height) - this.obb[column][0].y);

				line = new Polygon(new int[] { x0, x0, x1, x1 }, new int[] { y0, y1, y1, y0 }, 4);
				return line;
			}
		}

		// diagonal
		if (this.board[0][0] == this.board[1][1] && this.board[1][1] == this.board[2][2])
		{
			int x0 = (int) this.obb[0][0].x;
			int y0 = this.obb[0][0].y;
			int x1 = x0 + this.winLineThickness * 2;
			int y1 = y0 + ((this.obb[2][2].y + this.obb[2][0].height) - this.obb[0][0].y);
			int x2 = x0 + ((this.obb[2][0].x + this.obb[2][0].width) - this.obb[0][0].x);

			line = new Polygon(new int[] { x0, x2 - this.winLineThickness, x2 + this.winLineThickness, x1 }, new int[] { y0, y1, y1, y0 }, 4);
		}
		else if (this.board[0][2] == this.board[1][1] && this.board[1][1] == this.board[2][0])
		{
			int x0 = (int) this.obb[0][2].x;
			int y0 = this.obb[0][2].y + this.obb[0][2].height;
			int x1 = x0 + this.winLineThickness * 2;
			int y1 = y0 - ((this.obb[0][2].y + this.obb[0][2].height) - this.obb[2][0].y);
			int x2 = x0 + ((this.obb[2][0].x + this.obb[2][0].width) - this.obb[0][0].x);

			line = new Polygon(new int[] { x0, x2 - this.winLineThickness, x2 + this.winLineThickness, x1 }, new int[] { y0, y1, y1, y0 }, 4);
		}

		return line;
	}

	/**
	 * Getter for the board
	 * 
	 * @return the current board
	 */
	public byte[][] getBoard()
	{
		return this.board;
	}

	/**
	 * Getter for the board's oriented bounding boxes
	 * 
	 * @return the oriented bounding boxes
	 */
	public Rectangle[][] getOBB()
	{
		return this.obb;
	}
}
