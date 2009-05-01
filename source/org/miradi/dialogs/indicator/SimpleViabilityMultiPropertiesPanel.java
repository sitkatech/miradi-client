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
package org.miradi.dialogs.indicator;

import java.awt.BorderLayout;

import org.miradi.dialogs.base.AbstractObjectDataInputPanel;
import org.miradi.dialogs.base.OverlaidObjectDataInputPanel;
import org.miradi.dialogs.planning.MeasurementPropertiesPanel;
import org.miradi.dialogs.planning.propertiesPanel.BlankPropertiesPanel;
import org.miradi.dialogs.viability.IndicatorFutureStatusSubPanel;
import org.miradi.dialogs.viability.IndicatorPropertiesPanel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;

public class SimpleViabilityMultiPropertiesPanel extends OverlaidObjectDataInputPanel
{
	public SimpleViabilityMultiPropertiesPanel(MainWindow mainWindowToUse, ORef orefToUse) throws Exception
	{
		super(mainWindowToUse.getProject(), orefToUse);
		
		mainWindow = mainWindowToUse;
		setLayout(new BorderLayout());
		createPropertiesPanels();
	}
	
	// TODO: Why not create and add all the panels instead of only adding one?
	// If we changed it to work more like the other Overlaid panels,
	// a lot of this code would disappear
	public void dispose()
	{
		super.dispose();
		indicatorPropertiesPanel.dispose();
		measurementPropertiesPanel.dispose();
		futureStatusPropertiesPanel.dispose();
		blankPropertiesPanel.dispose();
	}
	
	private void createPropertiesPanels() throws Exception
	{
		indicatorPropertiesPanel = new IndicatorPropertiesPanel(getMainWindow(), getPicker());
		measurementPropertiesPanel = new MeasurementPropertiesPanel(getProject());
		futureStatusPropertiesPanel = new IndicatorFutureStatusSubPanel(getProject());
		blankPropertiesPanel = new BlankPropertiesPanel(getProject());
		
	}
	
	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}

	public void setObjectRefs(ORef[] orefsToUse)
	{
		if(currentPanel != null)
			currentPanel.becomeInactive();
		
		super.setObjectRefs(orefsToUse);
		currentPanel = findPanel(orefsToUse);
		removeAll();
		add(currentPanel, BorderLayout.CENTER);

		if(currentPanel != null)
			currentPanel.becomeActive();

		indicatorPropertiesPanel.setObjectRefs(orefsToUse);
		measurementPropertiesPanel.setObjectRefs(orefsToUse);
		futureStatusPropertiesPanel.setObjectRefs(orefsToUse);
		
		if(getTopLevelAncestor() != null)
			getTopLevelAncestor().validate();
		
		// NOTE: The following are an attempt to fix a reported problem 
		// where the screen was not fully repainted when switching objects
		// This code is duplicated in PlanningTreePropertiesPanel.java
		// and in TargetViabilityTreePropertiesPanel.java
		validate();
		repaint();
	}
	
	private AbstractObjectDataInputPanel findPanel(ORef[] orefsToUse)
	{
		if(orefsToUse.length == 0)
			return blankPropertiesPanel;
		
		ORef firstRef = orefsToUse[0];
		int objectType = firstRef.getObjectType();
		
		if (Indicator.getObjectType() == objectType)
			return indicatorPropertiesPanel;
		
		if (Measurement.getObjectType() == objectType)
			return measurementPropertiesPanel;
		
		if (Goal.is(objectType))
			return futureStatusPropertiesPanel;
		
		return blankPropertiesPanel;
	}
	
	@Override
	public void setFocusOnFirstField()
	{
		currentPanel.setFocusOnFirstField();
	} 
	
	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	public static final String PANEL_DESCRIPTION = "Planning Properties Panel";
	
	private MainWindow mainWindow;
	
	private AbstractObjectDataInputPanel currentPanel;
	private IndicatorPropertiesPanel indicatorPropertiesPanel;
	private BlankPropertiesPanel blankPropertiesPanel;
	private MeasurementPropertiesPanel measurementPropertiesPanel;
	private IndicatorFutureStatusSubPanel futureStatusPropertiesPanel; 
}
