/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.martus.swing.UiScrollPane;

public class DiagramView extends JPanel
{
	public DiagramView(MainWindow mainWindowToUse)
	{
		Project project = mainWindowToUse.getProject();
		Actions actions = mainWindowToUse.getActions();
		diagram = new DiagramComponent(project, actions);
		project.setSelectionModel(diagram.getSelectionModel());

		setLayout(new BorderLayout());
		add(new UiScrollPane(diagram), BorderLayout.CENTER);
	}
	
	public DiagramComponent getDiagramComponent()
	{
		return diagram;
	}

	public static String cardName()
	{
		return "Diagram";
	}

	DiagramComponent diagram;
}
