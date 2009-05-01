/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.dialogs.base;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager2;

import javax.swing.JScrollPane;

import org.miradi.main.EAM;
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
		String panelDescription = getPanelDescription();
		if(panelDescription == null)
			EAM.logWarning("VerticalSplitPanel splitter description is null: " + getClass().getSimpleName());
		return panelDescription;
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
