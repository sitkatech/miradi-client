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

import java.awt.CardLayout;
import java.awt.Rectangle;

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
		super(mainWindowToUse, orefToUse);
		
		cardLayout = new CardLayout();
		setLayout(cardLayout);
		createPropertiesPanels();
	}
	
	@Override
	public void becomeActive()
	{
		super.becomeActive();
		isActive = true;
		activateCurrentPanel();
	}
	
	@Override
	public void becomeInactive()
	{
		deactivateCurrentPanel();
		isActive = false;
		super.becomeInactive();
	}
	
	private void activateCurrentPanel()
	{
		if(currentPanel != null)
			currentPanel.becomeActive();
	}

	private void deactivateCurrentPanel()
	{
		if(currentPanel != null)
			currentPanel.becomeInactive();
	}
	
	private boolean isMultiPropertiesPanelActive()
	{
		return isActive;
	}
	
	private void createPropertiesPanels() throws Exception
	{
		indicatorPropertiesPanel = new IndicatorPropertiesPanel(getMainWindow(), getPicker());
		measurementPropertiesPanel = new MeasurementPropertiesPanel(getProject());
		futureStatusPropertiesPanel = new IndicatorFutureStatusSubPanel(getProject());
		blankPropertiesPanel = new BlankPropertiesPanel(getProject());
		
		addPanel(indicatorPropertiesPanel);
		addPanel(measurementPropertiesPanel);
		addPanel(futureStatusPropertiesPanel);
		addPanel(blankPropertiesPanel);
		
	}
	
	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}

	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);

		deactivateCurrentPanel();
		
		currentPanel = findPanel(orefsToUse);
		cardLayout.show(this, currentPanel.getPanelDescription());
		if (isMultiPropertiesPanelActive())
			activateCurrentPanel();
		
		scrollRectToVisible(new Rectangle(0,0,0,0));
		
		// NOTE: The following are an attempt to fix a reported problem 
		// where the screen was not fully repainted when switching objects
		// This code is duplicated in PlanningTreePropertiesPanel.java
		// and in TargetViabilityTreePropertiesPanel.java
		validate();
		repaint();
	}

	@Override
	public void selectSectionForTag(String tag)
	{
		currentPanel.selectSectionForTag(tag);
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
	
	public static final String PANEL_DESCRIPTION = "Planning Properties Panel";
	
	private CardLayout cardLayout;
	private AbstractObjectDataInputPanel currentPanel;
	private IndicatorPropertiesPanel indicatorPropertiesPanel;
	private BlankPropertiesPanel blankPropertiesPanel;
	private MeasurementPropertiesPanel measurementPropertiesPanel;
	private IndicatorFutureStatusSubPanel futureStatusPropertiesPanel; 
}
