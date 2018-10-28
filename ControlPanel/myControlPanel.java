package ControlPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class myControlPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public myControlPanel () {		
		JButton btn = new JButton("undo");
		btn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("Clik undo!");
			}
		});
		this.add(btn);
	}
	
}
