/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

import javax.swing.Icon;

import org.conservationmeasures.eam.commands.CommandLinkNodes;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.ConnectionPropertiesDialog;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionInsertConnection extends MainWindowAction
{
	public ActionInsertConnection(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), new InsertConnectionIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Connection...");
	}

	public void doAction(ActionEvent event) throws CommandFailedException
	{
		ConnectionPropertiesDialog dialog = new ConnectionPropertiesDialog(getMainWindow());
		dialog.setVisible(true);
		if(!dialog.getResult())
			return;
		
		DiagramModel model = getMainWindow().getProject().getDiagramModel();
		int fromIndex = dialog.getFrom().getId();
		int toIndex = dialog.getTo().getId();
		
		if(fromIndex == toIndex)
		{
			String[] body = {EAM.text("Can't link a node to itself"), };
			getMainWindow().okDialog(EAM.text("Can't Create Link"), body);
			return;
		}
		
		if(model.hasLinkage(dialog.getFrom(), dialog.getTo()))
		{
			String[] body = {EAM.text("Those nodes are already linked"), };
			getMainWindow().okDialog(EAM.text("Can't Create Link"), body);
			return;
		}
		
		try
		{
			CommandLinkNodes command = new CommandLinkNodes(fromIndex, toIndex);
			getMainWindow().getProject().executeCommand(command);
		}
		catch (CommandFailedException e)
		{
		}
	}

	public boolean shouldBeEnabled()
	{
		return getMainWindow().isProjectOpen();
	}
	
}

class InsertConnectionIcon implements Icon
{
	public int getIconHeight()
	{
		return 16;
	}

	public int getIconWidth()
	{
		return 16;
	}

	public void paintIcon(Component sample, Graphics g, int x, int y)
	{
		Color oldColor = g.getColor();
		g.setColor(Color.BLACK);
		g.drawLine(x+2, y+8, x+14, y+8);
		g.drawLine(x+14, y+8, x+12, y+6);
		g.drawLine(x+14, y+8, x+12, y+10);
		g.setColor(oldColor);
	}
}
