/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.Icon;

import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.diagram.nodes.NodeTypeThreat;
import org.conservationmeasures.eam.diagram.nodes.RectangleRenderer;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionInsertThreat extends InsertNodeAction
{
	public ActionInsertThreat(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), new InsertThreatIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Threat");
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Threat");
	}

	public void doAction(ActionEvent event) throws CommandFailedException
	{
		doInsert(new CommandInsertNode(Node.TYPE_THREAT));
	}

	public boolean shouldBeEnabled()
	{
		return getMainWindow().isProjectOpen();
	}
	
}

class InsertThreatIcon implements Icon
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
		Rectangle rect = new Rectangle(x, y, getIconWidth(), getIconHeight() * 3 / 4);
		Color color = new NodeTypeThreat().getColor();
		RectangleRenderer renderer = new RectangleRenderer();
		renderer.fillShape(g, rect, color);
		renderer.drawBorder((Graphics2D)g, rect, Color.BLACK);
		
	}
	
}