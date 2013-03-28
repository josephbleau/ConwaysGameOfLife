package com.josephbleau.cs311;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class ConwayGrid extends Component implements ActionListener, MouseListener, Runnable{
	private static final long serialVersionUID = 1L;

	private float tickEveryInSeconds;
	private int cols;
	private int rows;
	private ArrayList<ArrayList<Boolean>> cells;
	private Boolean paused;
	
	public ConwayGrid( int numCols, int numRows )
	{
		super();
		
		this.paused = true;
		this.cols = numCols;
		this.rows = numRows;
		this.tickEveryInSeconds = 0.25f;
		
		this.cells = new ArrayList<ArrayList<Boolean>>();
		for(int y = 0; y < this.rows; ++y)
		{
			this.cells.add( new ArrayList<Boolean>() );
			for(int x = 0; x < this.cols; ++x)
			{
				this.cells.get( y ).add( false );
			}
		}
	}
	
	private int wrapDecrement(int n, int max)
	{
		if(n == 0)
			return max-1;
		else
			return n-1;
	}
	
	private int wrapIncrement(int n, int max)
	{
		if(n == max-1)
			return 0;
		else
			return n+1;
	}
	
	public void toggleThreadActive()
	{
		this.paused = !this.paused;
	}
	
	@Override
	public void run()
	{
		while(true)
		{		
			if(this.paused)
			{
				try {
					Thread.sleep((long)(tickEveryInSeconds * 1000));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				continue;
			}
			
			/* New state, must maintain new and old state simultaneously while 
			 * constructing the new state, as changes early in the tick should not
			 * influence changes later in the tick. */
			@SuppressWarnings("unchecked")
			ArrayList<ArrayList<Boolean>> newCells = new ArrayList<ArrayList<Boolean>>();
			for(int y = 0; y < rows; ++y)
			{
				newCells.add(new ArrayList<Boolean>());
				for(int x = 0; x < cols; ++x)
				{
					newCells.get(y).add(false);
					newCells.get(y).set(x, this.cells.get(y).get(x));
				}
			}
			
			for(int y = 0; y < rows; ++y)
			{
				for(int x = 0; x < cols; ++x)
				{
					ArrayList<Point> neighbors = new ArrayList<Point>();
	
					neighbors.add(new Point(wrapDecrement(x, this.cols), y));
					neighbors.add(new Point(wrapIncrement(x, this.cols), y));
					neighbors.add(new Point(x, wrapDecrement(y, this.rows)));
					neighbors.add(new Point(x, wrapIncrement(y, this.rows)));
					neighbors.add(new Point(wrapDecrement(x, this.cols), wrapDecrement(y, this.rows)));
					neighbors.add(new Point(wrapDecrement(x, this.cols), wrapIncrement(y, this.rows)));
					neighbors.add(new Point(wrapIncrement(x, this.cols), wrapDecrement(y, this.rows)));
					neighbors.add(new Point(wrapIncrement(x, this.cols), wrapIncrement(y, this.rows)));
					
					int aliveNeighbors = 0;
					for(int n = 0; n < neighbors.size(); ++n)				
					{
						int neighborX = neighbors.get(n).x;
						int neighborY = neighbors.get(n).y;
						Boolean alive = this.cells.get(neighborY).get(neighborX);
						
						if( alive == true )
							aliveNeighbors++;
					}
					
					if( this.cells.get(y).get(x) == true )
					{
						if( aliveNeighbors < 2)
						{
							newCells.get(y).set(x,  false);
						}
						else if( aliveNeighbors == 2 || aliveNeighbors == 3)
						{
							/* You don't die, good job! */
						}
						else
						{
							newCells.get(y).set(x, false);
						}
					}
					else
					{
						if( aliveNeighbors == 3 )
						{
							newCells.get(y).set(x, true);
						}
					}
				}
			}
			
			this.cells = newCells;
			this.repaint();
			
			try {
				Thread.sleep((long)(tickEveryInSeconds * 1000));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void paint( Graphics g )
	{
		super.paint(g);

		int cellWidth = this.getWidth() / this.cols;
		int cellHeight = this.getHeight() / this.rows;
		
		for(int x = 0; x < this.cols; ++x)
		{
			for(int y = 0; y < this.rows; y++)
			{		
				int width = cellWidth;
				int height = cellHeight;

				if( this.cells.get(y).get(x) == true )
					g.setColor( Color.black );
				else
					g.setColor( Color.white );
				
				g.fillRect( x*cellWidth, y*cellHeight, width, height );
				
				g.setColor( Color.gray );
				g.drawRect( x*cellWidth, y*cellHeight,  width,  height);
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		try 
		{
			int x = this.getMousePosition().x / (this.getWidth() / this.cols);
			int y = this.getMousePosition().y / (this.getHeight() / this.rows);
			this.cells.get(y).set(x,  arg0.getButton() == MouseEvent.BUTTON1);
			this.repaint();
		} 
		catch ( Exception e )
		{
			/* Clicked outside of the grid. */
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.cells = new ArrayList<ArrayList<Boolean>>();
		for(int y = 0; y < this.rows; ++y)
		{
			this.cells.add( new ArrayList<Boolean>() );
			for(int x = 0; x < this.cols; ++x)
			{
				this.cells.get( y ).add( false );
			}
		}
		
		this.repaint();
	}

}