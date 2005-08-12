/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views;

import java.awt.BorderLayout;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.main.MainWindow;
import org.martus.swing.UiScrollPane;

public class DiagramView extends UmbrellaView
{
	public DiagramView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		diagram = new DiagramComponent(getProject(), getActions());
		getProject().setSelectionModel(diagram.getSelectionModel());

		setLayout(new BorderLayout());
		add(new UiScrollPane(diagram), BorderLayout.CENTER);
	}
	
	public DiagramComponent getDiagramComponent()
	{
		return diagram;
	}

	public String cardName()
	{
		return "Diagram";
	}

	DiagramComponent diagram;
}
