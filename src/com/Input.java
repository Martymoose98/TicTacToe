package com;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Input implements MouseListener
{
	/**
	 * The game to provide input for
	 */
	private Game game;
	
	/**
	 * Constructor
	 * 
	 * @param game 
	 * 			the game instance to provide input for
	 */
	public Input(Game game)
	{
		this.game = game;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{

	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub
	}

	/**
	 *  This function executes when the user presses the mouse
	 *  @param the current MouseEvent
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{
		Rectangle[][] obb = this.game.getBoard().getOBB();

		for (int y = 0; y < obb.length; ++y)
		{
			for (int x = 0; x < obb[y].length; ++x)
			{
				if (!game.getWon() && e.getButton() == MouseEvent.BUTTON1 && obb[x][y].contains(e.getX(), e.getY()))
				{
					if (game.getSingleplayer())
						game.placePiece(x, y, game.getSingleplayerPiece());
					else
						game.placePiece(x, y, (game.getTurn()) ? Game.GAME_PIECE_X : Game.GAME_PIECE_O);
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

}
