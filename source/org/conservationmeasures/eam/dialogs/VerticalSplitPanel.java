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

import org.conservationmeasures.eam.utils.SplitterPositionSaver;
import org.conservationmeasures.eam.views.umbrella.ViewSplitPane;

abstract public class VerticalSplitPanel extends ModelessDialogPanel
{
	public VerticalSplitPanel(SplitterPositionSaver splitPositionSaverToUse, String splitterNameToUse, Component top, Component bottom)
	{
		this(splitPositionSaverToUse, new BorderLayout());
		createVerticalSplitPane(top, bottom, splitterNameToUse);
	}
	
	public VerticalSplitPanel(SplitterPositionSaver splitPositionSaverToUse)
	{
		this(splitPositionSaverToUse, new BorderLayout());
	}
	
	public VerticalSplitPanel(SplitterPositionSaver splitPositionSaverToUse, LayoutManager2 layoutToUse)
	{
		super(layoutToUse);
		splitPositionSaver = splitPositionSaverToUse;
	}

	public void createVerticalSplitPane(Component top, Component bottom, String splitterName)
	{
		JScrollPane treeComponentScroll = new JScrollPane(top);
		JScrollPane propertiesScroll = new JScrollPane(bottom);
			
		ViewSplitPane splitter = new ViewSplitPane(splitPositionSaver, splitterName, treeComponentScroll, propertiesScroll );
		add(splitter, BorderLayout.CENTER);
	}
	
	protected void setComponentPreferredSize(JComponent component)
	{
		Dimension dimension = new Dimension(0,200);
		component.setPreferredSize(dimension);
	}

	private SplitterPositionSaver splitPositionSaver;	 
}
