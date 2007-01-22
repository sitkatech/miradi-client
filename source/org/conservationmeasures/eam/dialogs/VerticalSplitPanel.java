/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager2;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.ViewSplitPane;

abstract public class VerticalSplitPanel extends ModelessDialogPanel
{
	public VerticalSplitPanel(MainWindow mainWindowToUse)
	{
		this(mainWindowToUse, new BorderLayout());
	}
	
	public VerticalSplitPanel(MainWindow mainWindowToUse, LayoutManager2 layoutToUse)
	{
		super(layoutToUse);
		mainWindow = mainWindowToUse;
	}

	public void createVerticalSplitPane(Component top, Component bottom)
	{
		JScrollPane treeComponentScroll = new JScrollPane(top);
		JScrollPane propertiesScroll = new JScrollPane(bottom);
			
		String splitterName = mainWindow.getProject().getCurrentView() + PROPERTIES_SPLITTER;
		ViewSplitPane splitter = new ViewSplitPane(mainWindow, splitterName, treeComponentScroll, propertiesScroll );
		add(splitter, BorderLayout.CENTER);
	}
	
	protected void setComponentPreferredSize(JComponent component)
	{
		Dimension dimension = new Dimension(0,200);
		component.setPreferredSize(dimension);
	}

	private MainWindow mainWindow;
	
	private static final String PROPERTIES_SPLITTER = "PropertiesSplitter"; 
}
