package com.josephbleau.cs311;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Application extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private Thread conwayUpdateThread;
	private ConwayGrid conwayGrid;
	private JButton startButton;
	private JButton clearButton;
	
	public Application(String appTitle)
	{
		super(appTitle);
	}
	
	public static void main( String[] args )
	{
		Application app = new Application("Demo");
		app.setSize( 640, 480 );
		app.setMinimumSize( new Dimension(640, 480) );
		app.conwayGrid = new ConwayGrid( 30, 30);
		app.startButton = new JButton("Play");
		app.clearButton = new JButton("Clear");
		
		app.setLayout(null);
		app.add(app.conwayGrid);
		app.add(app.startButton);
		app.add(app.clearButton);
		
		int h80p = (int) (app.getHeight() * 0.8);
		
		app.conwayGrid.setBounds(10, 10,
				                 app.getWidth() - 20, 
				                 h80p + 10 );
		
		app.startButton.setBounds( 10, h80p + 20, 100, 25 );
		
		app.clearButton.setBounds( 120, h80p + 20, 100, 25 );
		
		app.startButton.addActionListener( (ActionListener) app );
		app.clearButton.addActionListener( (ActionListener) app.conwayGrid );
		app.addMouseListener( app.conwayGrid );
		
		app.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		app.setVisible( true );
		
		app.conwayUpdateThread = new Thread(app.conwayGrid);
		app.conwayUpdateThread.start();
	}
	
	@Override
	public void validate()
	{
		super.validate();
		
		int h80p = (int) (this.getHeight() * 0.8);
		
		this.conwayGrid.setBounds(10, 10,
				                  this.getWidth() - 20, 
				                  h80p + 10 );
		
		this.startButton.setBounds( 10, h80p + 20, 100, 25 );

		this.clearButton.setBounds( 120, h80p + 20, 100, 25 );
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/* If the thread is paused, resume it, 
		 * if it's playing, pause it.*/
		this.conwayGrid.toggleThreadActive();
		if(this.startButton.getText().equals("Play"))
			this.startButton.setText("Pause");
		else
			this.startButton.setText("Play");
	}
}
