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
import org.conservationmeasures.eam.diagram.nodes.EllipseRenderer;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.diagram.nodes.NodeTypeGoal;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionInsertGoal extends InsertNodeAction
{
	public ActionInsertGoal(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), new InsertGoalIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Goal");
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Goal");
	}

	public void doAction(ActionEvent event) throws CommandFailedException
	{
		doInsert(new CommandInsertNode(Node.TYPE_GOAL));
	}

	public boolean shouldBeEnabled()
	{
		return getMainWindow().isProjectOpen();
	}
	
}

class InsertGoalIcon implements Icon
{
	public int getIconHeight()
	{
		return 12;
	}

	public int getIconWidth()
	{
		return 16;
	}

	public void paintIcon(Component sample, Graphics g, int x, int y)
	{
		Rectangle rect = new Rectangle(x, y, getIconWidth(), getIconHeight());
		Color color = new NodeTypeGoal().getColor();
		EllipseRenderer.fill(g, rect, color);
		EllipseRenderer ellipse = new EllipseRenderer();
		ellipse.drawBorder((Graphics2D)g, rect, Color.BLACK);
	}
	
}