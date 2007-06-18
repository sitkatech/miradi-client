/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.main.MainWindow;

public class ResultsChainDiagramSplitPane extends DiagramSplitPane
{
	public ResultsChainDiagramSplitPane(MainWindow mainWindow, DiagramComponent diagramComponent)
	{
		super(mainWindow, diagramComponent);
	}

	public DiagramLegendPanel createLegendPanel(MainWindow mainWindow)
	{
		return new ResultsChainDiagramLegendPanel(mainWindow);
	}
	
	public JComponent createSelectionPanel()
	{
		return new ResultsChainSelectionPanel();
	}
	
	protected JPanel createLeftPanel()
	{
		JPanel leftPanel = new JPanel(new GridLayout(1, 1));
		leftPanel.add(legendPanel);
		
		return leftPanel;
	}
}
