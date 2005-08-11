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

import org.conservationmeasures.eam.actions.ActionCopy;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.actions.ActionInsertConnection;
import org.conservationmeasures.eam.actions.ActionInsertGoal;
import org.conservationmeasures.eam.actions.ActionInsertIntervention;
import org.conservationmeasures.eam.actions.ActionInsertThreat;
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.ActionRedo;
import org.conservationmeasures.eam.actions.ActionUndo;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.MainWindowAction;

public class DiagramToolBar extends JToolBar
{
	public DiagramToolBar(Actions actions)
	{
		setFloatable(false);
		addButtonForAction(actions.get(ActionInsertGoal.class));
		addButtonForAction(actions.get(ActionInsertThreat.class));
		addButtonForAction(actions.get(ActionInsertIntervention.class));
		addButtonForAction(actions.get(ActionInsertConnection.class));
		addSeparator();
		addButtonForAction(actions.get(ActionUndo.class));
		addButtonForAction(actions.get(ActionRedo.class));
		addSeparator();
		addButtonForAction(actions.get(ActionCut.class));
		addButtonForAction(actions.get(ActionCopy.class));
		addButtonForAction(actions.get(ActionPaste.class));
		addButtonForAction(actions.get(ActionDelete.class));
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
