/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import org.martus.swing.Utilities;

public class EAMDialog extends JDialog
{
	public EAMDialog(JFrame parent)
	{
		super(parent);
		Utilities.centerDlg(this);
	}
	
	public EAMDialog(JFrame parent, String heading)
	{
		this(parent);
		setTitle(heading);
	}
	
	protected JRootPane createRootPane() 
	{
		rootPane = new JRootPane();
		KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
		Action actionListener = new AbstractAction() 
		{
			public void actionPerformed(ActionEvent actionEvent) 
			{
				dispose();
			} 
		};
		
		InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(stroke, "ESCAPE");
		rootPane.getActionMap().put("ESCAPE", actionListener);

		return rootPane;
	} 
}
