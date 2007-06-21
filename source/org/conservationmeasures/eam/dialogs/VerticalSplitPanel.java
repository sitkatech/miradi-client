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

import org.conservationmeasures.eam.utils.FastScrollPane;
import org.conservationmeasures.eam.utils.SplitterPositionSaverAndGetter;
import org.conservationmeasures.eam.views.umbrella.ManagementPanelSplitPane;
import org.conservationmeasures.eam.views.umbrella.PersistentSplitPane;

abstract public class VerticalSplitPanel extends ModelessDialogPanel
{
	public VerticalSplitPanel(SplitterPositionSaverAndGetter splitPositionSaverToUse, Component top, Component bottom)
	{
		this(splitPositionSaverToUse, new BorderLayout());
		createVerticalSplitPane(top, bottom, getSplitterDescription());
	}
	
	public VerticalSplitPanel(SplitterPositionSaverAndGetter splitPositionSaverToUse)
	{
		this(splitPositionSaverToUse, new BorderLayout());
	}
	
	public VerticalSplitPanel(SplitterPositionSaverAndGetter splitPositionSaverToUse, LayoutManager2 layoutToUse)
	{
		super(layoutToUse);
		splitPositionSaver = splitPositionSaverToUse;
	}

	public void createVerticalSplitPane(Component top, Component bottom, String splitterName)
	{
		JScrollPane propertiesScroll = new FastScrollPane(bottom);
		
		splitter = new ManagementPanelSplitPane(this, splitPositionSaver, splitterName, top, propertiesScroll );
		add(splitter, BorderLayout.CENTER);
	}
	
	public void updateSplitterLocation()
	{
		splitter.updateSplitterLocation(getSplitterDescription());
	}
	
	public void updateSplitterLocationToMiddle()
	{
		splitter.setSplitterLocationToMiddle(getSplitterDescription());
	}
	
	protected void setComponentPreferredSize(JComponent component)
	{
		Dimension dimension = new Dimension(0,200);
		component.setPreferredSize(dimension);
	}
	
	public String getSplitterDescription()
	{
		return getPanelDescription();
	}

	public final static String SPLITTER_TAG = "Splitter";
	private SplitterPositionSaverAndGetter splitPositionSaver;
	private PersistentSplitPane splitter;
}
