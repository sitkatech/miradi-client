/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.MainWindowAction;

public class DiagramToolBar extends JToolBar
{
	public DiagramToolBar(Actions actions)
	{
		setFloatable(false);
		addButtonForAction(actions.undo);
		addButtonForAction(actions.redo);
		addSeparator();
		addButtonForAction(actions.cut);
		addButtonForAction(actions.copy);
		addButtonForAction(actions.paste);
		addButtonForAction(actions.delete);
	}
	
	void addButtonForAction(MainWindowAction action)
	{
		ToolBarButton button = new ToolBarButton(action);
		add(button);
	}
	
}

class ActionStub extends AbstractAction
{
	public ActionStub(String imagePath)
	{
		super(imagePath, new ImageIcon(imagePath));
	}

	public void actionPerformed(ActionEvent ae)
	{
		EAM.logWarning("ActionStub: " + getClass());
	}
}

class ToolBarButton extends JButton
{
	public ToolBarButton(MainWindowAction action)
	{
		super(action);
		setText("");
		setToolTipText(action.getToolTipText());
	}
	
}
