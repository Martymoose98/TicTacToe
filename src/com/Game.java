package com;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.util.Random;

public class Game
{
	public static final byte GAME_PIECE_INVALID = 0;
	public static final byte GAME_PIECE_X = 1;
	public static final byte GAME_PIECE_O = 2;

	public static final int GAME_PIECE_FONT_SIZE = 96;
	public static final Font GAME_PIECE_FONT = new Font("Verdana", Font.ITALIC, GAME_PIECE_FONT_SIZE);
	public static final FontRenderContext GAME_PIECE_FONT_CTX = new FontRenderContext(GAME_PIECE_FONT.getTransform(), false, false);

	public static final TextLayout GAME_PIECE_X_LAYOUT = new TextLayout("X", GAME_PIECE_FONT, GAME_PIECE_FONT_CTX);
	public static final TextLayout GAME_PIECE_O_LAYOUT = new TextLayout("O", GAME_PIECE_FONT, GAME_PIECE_FONT_CTX);

	public static final int GAME_WIN_MSG_FONT_SIZE = 56;
	public static final Font GAME_WIN_MSG_FONT = new Font("Freestyle Script", Font.ITALIC, GAME_WIN_MSG_FONT_SIZE);

	private GameBoard board;
	private Random rand;
	private boolean turn;
	private byte singleplayer;
	private byte won;
	private float winMsgHue;

	/**
	 * Constructor
	 * 
	 * @param gameboardX
	 *            the x position of board
	 * 
	 * @param gameboardY
	 *            the y position of board
	 * 
	 * @param gameboardSize
	 *            the gameboard square size in pixels
	 * 
	 * @param randomizeFirstMove
	 *            randomize who has first move
	 * 
	 * @param singleplayer
	 *            singleplayer gamepiece if invalid the game is two players
	 */
	public Game(int gameboardX, int gameboardY, int gameboardSize, boolean randomizeFirstMove, byte singleplayer)
	{
		this.rand = new Random();
		this.board = new GameBoard(gameboardX, gameboardY, gameboardSize);
		this.singleplayer = singleplayer;
		this.turn = (randomizeFirstMove) ? rand.nextBoolean() : (this.singleplayer == GAME_PIECE_X);
	}

	/**
	 * This will place a game piece and only if the move is valid, cycles the turn
	 * 
	 * @param x
	 *            the x position of the piece to be placed relative to the board
	 * 
	 * @param y
	 *            the y position of the piece to be placed relative to the board
	 *
	 * @param type
	 *            the type of game piece to be placed
	 * 
	 * @return if the place succeeded
	 */
	public boolean placePiece(int x, int y, byte type)
	{
		boolean bValid = this.board.placePiece(x, y, type);

		if (bValid)
			this.turn = !this.turn;

		return bValid;
	}

	/**
	 * Render method
	 * 
	 * @param g
	 *            a valid graphics instance
	 */
	public void render(Graphics g)
	{
		this.board.draw(g, Color.GREEN);

		if (this.winMsgHue >= 1.0f)
			this.winMsgHue = 0.0f;

		this.winMsgHue += 0.001f;

		if (this.won == GAME_PIECE_X)
		{
			g.setColor(Color.getHSBColor(this.winMsgHue, 1.f, 1.f));
			g.setFont(GAME_WIN_MSG_FONT);
			g.drawString("X Wins!", 590, 150);
		}
		else if (this.won == GAME_PIECE_O)
		{
			g.setColor(Color.getHSBColor(this.winMsgHue, 1.f, 1.f));
			g.setFont(GAME_WIN_MSG_FONT);
			g.drawString("O Wins!", 590, 150);
		}
		else if (isCatsGame())
		{
			g.setColor(Color.getHSBColor(this.winMsgHue, 1.f, 1.f));
			g.setFont(GAME_WIN_MSG_FONT);
			g.drawString("Cat's Game!", 585, 150);
		}

		if (this.won != GAME_PIECE_INVALID)
		{
			g.setColor(Color.MAGENTA);
			g.drawPolygon(this.board.getWinLine());
		}
	}

	/**
	 * Update method
	 */
	public void update()
	{
		this.won = isWon();

		if (won != GAME_PIECE_INVALID)
			return;

		if (isCatsGame())
			return;

		if (turn)
			return;

		if (this.singleplayer == GAME_PIECE_X)
		{
			while (!placePiece(rand.nextInt(3), rand.nextInt(3), GAME_PIECE_O)); // spin until good spot found
		}
		else if (this.singleplayer == GAME_PIECE_O)
		{
			while (!placePiece(rand.nextInt(3), rand.nextInt(3), GAME_PIECE_X));
		}
	}

	/**
	 * Checks if the current game has a winner
	 * 
	 * @return the game piece id that won else invalid
	 */
	public byte isWon()
	{
		byte[][] board = this.board.getBoard();

		if ((board[0][0] == GAME_PIECE_X && board[0][1] == GAME_PIECE_X && board[0][2] == GAME_PIECE_X) ||
				(board[1][0] == GAME_PIECE_X && board[1][1] == GAME_PIECE_X && board[1][2] == GAME_PIECE_X) ||
				(board[2][0] == GAME_PIECE_X && board[2][1] == GAME_PIECE_X && board[2][2] == GAME_PIECE_X) ||
				(board[0][0] == GAME_PIECE_X && board[1][0] == GAME_PIECE_X && board[2][0] == GAME_PIECE_X) ||
				(board[0][1] == GAME_PIECE_X && board[1][1] == GAME_PIECE_X && board[2][1] == GAME_PIECE_X) ||
				(board[0][2] == GAME_PIECE_X && board[1][2] == GAME_PIECE_X && board[2][2] == GAME_PIECE_X) ||
				(board[0][0] == GAME_PIECE_X && board[1][1] == GAME_PIECE_X && board[2][2] == GAME_PIECE_X) ||
				(board[0][2] == GAME_PIECE_X && board[1][1] == GAME_PIECE_X && board[2][0] == GAME_PIECE_X))
		{
			return GAME_PIECE_X;
		}
		else if ((board[0][0] == GAME_PIECE_O && board[0][1] == GAME_PIECE_O && board[0][2] == GAME_PIECE_O) ||
				(board[1][0] == GAME_PIECE_O && board[1][1] == GAME_PIECE_O && board[1][2] == GAME_PIECE_O) ||
				(board[2][0] == GAME_PIECE_O && board[2][1] == GAME_PIECE_O && board[2][2] == GAME_PIECE_O) ||
				(board[0][0] == GAME_PIECE_O && board[1][0] == GAME_PIECE_O && board[2][0] == GAME_PIECE_O) ||
				(board[0][1] == GAME_PIECE_O && board[1][1] == GAME_PIECE_O && board[2][1] == GAME_PIECE_O) ||
				(board[0][2] == GAME_PIECE_O && board[1][2] == GAME_PIECE_O && board[2][2] == GAME_PIECE_O) ||
				(board[0][0] == GAME_PIECE_O && board[1][1] == GAME_PIECE_O && board[2][2] == GAME_PIECE_O) ||
				(board[0][2] == GAME_PIECE_O && board[1][1] == GAME_PIECE_O && board[2][0] == GAME_PIECE_O))
		{
			return GAME_PIECE_O;
		}
		else
		{
			return GAME_PIECE_INVALID;
		}
	}

	/**
	 * Checks if the current game is a cat's game
	 * 
	 * @return if the current game is a cat's game
	 */
	public boolean isCatsGame()
	{
		byte[][] board = this.board.getBoard();

		for (byte[] row : board)
			for (byte piece : row)
				if (piece == GAME_PIECE_INVALID)
					return false;

		return true;
	}

	/**
	 * Returns the current game board
	 * 
	 * @return the game board
	 */
	public GameBoard getBoard()
	{
		return this.board;
	}

	/**
	 * Returns the current turn taker
	 * 
	 * @return the current turn taker
	 */
	public boolean getTurn()
	{
		return this.turn;
	}

	/**
	 * Returns the current turn taker
	 * 
	 * @return the current turn taker
	 */
	public boolean getWon()
	{
		return (this.won != GAME_PIECE_INVALID);
	}

	/**
	 * Returns if the game is singleplayer or not
	 * 
	 * @return if the game is singleplayer or not
	 */
	public boolean getSingleplayer()
	{
		return (this.singleplayer != GAME_PIECE_INVALID);
	}

	/**
	 * Returns the current singleplayer piece
	 * 
	 * @return the current singleplayer piece
	 */
	public byte getSingleplayerPiece()
	{
		return this.singleplayer;
	}
}
