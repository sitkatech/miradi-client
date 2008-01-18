/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.indicator;

import java.awt.CardLayout;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.base.DisposablePanelWithDescription;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.dialogs.planning.MeasurementPropertiesPanel;
import org.conservationmeasures.eam.dialogs.planning.propertiesPanel.BlankPropertiesPanel;
import org.conservationmeasures.eam.dialogs.viability.IndicatorFutureStatusSubPanel;
import org.conservationmeasures.eam.dialogs.viability.IndicatorPropertiesPanel;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Measurement;
import org.conservationmeasures.eam.project.Project;

public class DirectIndicatorPropertiesPanel extends ObjectDataInputPanel
{
	public DirectIndicatorPropertiesPanel(Project projectToUse, Actions actionsToUse, ORef orefToUse) throws Exception
	{
		super(projectToUse, orefToUse);
		
		actions = actionsToUse;
		cardLayout = new CardLayout();
		setLayout(cardLayout);
		createPropertiesPanels();
	}
	
	public void dispose()
	{
		super.dispose();
		indicatorPropertiesPanel.dispose();
		measurementPropertiesPanel.dispose();
		futureStatusPropertiesPanel.dispose();
	}
	
	private void createPropertiesPanels() throws Exception
	{
		indicatorPropertiesPanel = new IndicatorPropertiesPanel(getProject(), actions);
		measurementPropertiesPanel = new MeasurementPropertiesPanel(getProject());
		futureStatusPropertiesPanel = new IndicatorFutureStatusSubPanel(getProject());
		blankPropertiesPanel = new BlankPropertiesPanel();
		
		add(indicatorPropertiesPanel);
		add(measurementPropertiesPanel);
		add(futureStatusPropertiesPanel);
		add(blankPropertiesPanel);
	}
	
	private void add(DisposablePanelWithDescription panelToAdd)
	{
		add(panelToAdd, panelToAdd.getPanelDescription());
	}
	
	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}

	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
		cardLayout.show(this, findPanel(orefsToUse).getPanelDescription());
	
		indicatorPropertiesPanel.setObjectRefs(orefsToUse);
		measurementPropertiesPanel.setObjectRefs(orefsToUse);
		futureStatusPropertiesPanel.setObjectRefs(orefsToUse);
	}
	
	private DisposablePanelWithDescription findPanel(ORef[] orefsToUse)
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
	
	public static final String PANEL_DESCRIPTION = "Planning Properties Panel";
	
	private Actions actions;
	private CardLayout cardLayout;
	
	private IndicatorPropertiesPanel indicatorPropertiesPanel;
	private BlankPropertiesPanel blankPropertiesPanel;
	private MeasurementPropertiesPanel measurementPropertiesPanel;
	private IndicatorFutureStatusSubPanel futureStatusPropertiesPanel; 
}
