/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.base;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager2;

import javax.swing.JScrollPane;

import org.miradi.utils.MiradiScrollPane;
import org.miradi.utils.SplitterPositionSaverAndGetter;
import org.miradi.views.umbrella.ManagementPanelSplitPane;
import org.miradi.views.umbrella.PersistentSplitPane;

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
		JScrollPane propertiesScroll = new MiradiScrollPane(bottom);
		
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
	
	public String getSplitterDescription()
	{
		return getPanelDescription();
	}
	
	public void setDividerThick()
	{
		splitter.setDividerSize(THICK_DIVIDER_SIZE);
	}
	
	private final static int THICK_DIVIDER_SIZE = 10;

	public final static String SPLITTER_TAG = "Splitter";
	private SplitterPositionSaverAndGetter splitPositionSaver;
	private PersistentSplitPane splitter;
}
