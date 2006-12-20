/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.conservationmeasures.eam.main.EAM;
import org.martus.swing.UiButton;
import org.martus.swing.Utilities;

public class ModelessDialogWithClose extends EAMDialog
{
	public ModelessDialogWithClose(JFrame parent, DisposablePanel panel, String headingText)
	{
		super(parent);
		setModal(false);
		setTitle(headingText);
		
		wrappedPanel = panel;
	
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(wrappedPanel, BorderLayout.CENTER);
		mainPanel.add(createButtonBar(), BorderLayout.AFTER_LAST_LINE);
		getContentPane().add(new JScrollPane(mainPanel));
		pack();
		Utilities.fitInScreen(this);
	}
	
	public JPanel getWrappedPanel()
	{
		return wrappedPanel;
	}
	
	private Box createButtonBar()
	{
		UiButton closeButton = new FocuseButton(EAM.text("Button|Close"));
		closeButton.setSelected(true);
		closeButton.addActionListener(new DialogCloseListener());
		
		getRootPane().setDefaultButton(closeButton);
		Box buttonBar = Box.createHorizontalBox();
		Component[] components = new Component[] {Box.createHorizontalGlue(), closeButton};
		Utilities.addComponentsRespectingOrientation(buttonBar, components);
		return buttonBar;
	}
	
	class FocuseButton extends UiButton implements FocusListener
	{
		public FocuseButton(String text)
		{
			super(text);
			addFocusListener(this);
		}
		

		public void focusGained(FocusEvent e)
		{
			setBackground(Color.BLUE);
			
		}

		public void focusLost(FocusEvent e)
		{
			setBackground(Color.WHITE);
			
		}
		
	}
	
	private final class DialogCloseListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			dispose();
		}
	}

	public void dispose()
	{
		if(wrappedPanel == null)
			return;
		
		wrappedPanel.dispose();
		wrappedPanel = null;
		super.dispose();
	}


	DisposablePanel wrappedPanel;
}
