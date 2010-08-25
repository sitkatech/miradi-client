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

import javax.swing.JScrollPane;

import org.miradi.main.EAM;
import org.miradi.utils.MiradiScrollPane;
import org.miradi.utils.SplitterPositionSaverAndGetter;
import org.miradi.views.umbrella.ManagementPanelSplitPane;
import org.miradi.views.umbrella.PersistentSplitPane;

abstract public class VerticalSplitPanel extends ModelessDialogPanel
{
	public VerticalSplitPanel(SplitterPositionSaverAndGetter splitPositionSaverToUse, Component top, Component bottom, String uniqueIdentifier)
	{
		super(new BorderLayout());
		
		initializeSplitter(splitPositionSaverToUse, top, bottom, uniqueIdentifier);
	}
	
	public VerticalSplitPanel(SplitterPositionSaverAndGetter splitPositionSaverToUse, Component top, Component bottom)
	{
		super(new BorderLayout());

		initializeSplitter(splitPositionSaverToUse, top, bottom, getPanelDescription());
	}

	private void initializeSplitter(SplitterPositionSaverAndGetter splitPositionSaverToUse, Component top, Component bottom, String splitterDescriptionToUse)
	{
		setSplitterDescription(splitterDescriptionToUse);
		splitPositionSaver = splitPositionSaverToUse;
		createVerticalSplitPane(top, bottom);
	}
	
	private void createVerticalSplitPane(Component top, Component bottom)
	{
		JScrollPane propertiesScroll = new MiradiScrollPane(bottom);
		
		splitter = new ManagementPanelSplitPane(this, splitPositionSaver, getSplitterDescription(), top, propertiesScroll );
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
		if(splitterDescription == null)
			EAM.logWarning("VerticalSplitPanel splitter description is null: " + getClass().getSimpleName());
	
		return splitterDescription;
	}

	private void setSplitterDescription(String splitterDescriptionToUse)
	{
		splitterDescription = splitterDescriptionToUse;
	}
	
	public void setDividerThick()
	{
		splitter.setDividerSize(THICK_DIVIDER_SIZE);
	}
	
	private final static int THICK_DIVIDER_SIZE = 10;

	public final static String SPLITTER_TAG = "Splitter";
	private SplitterPositionSaverAndGetter splitPositionSaver;
	private PersistentSplitPane splitter;
	private String splitterDescription;
}
