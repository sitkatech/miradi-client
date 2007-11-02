/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.awt.CardLayout;

import org.conservationmeasures.eam.dialogs.planning.MeasurementPropertiesPanel;
import org.conservationmeasures.eam.dialogs.planning.propertiesPanel.BlankPropertiesPanel;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Measurement;
import org.conservationmeasures.eam.project.Project;

public class DirectIndicatorPropertiesPanel extends ObjectDataInputPanel
{
	public DirectIndicatorPropertiesPanel(Project projectToUse, ORef orefToUse) throws Exception
	{
		super(projectToUse, orefToUse);
		cardLayout = new CardLayout();
		setLayout(cardLayout);
		createPropertiesPanels();
	}
	
	public void dispose()
	{
		super.dispose();
		indicatorPropertiesPanel.dispose();
		measurementPropertiesPanel.dispose();
	}
	
	private void createPropertiesPanels() throws Exception
	{
		indicatorPropertiesPanel = new IndicatorPropertiesPanel(getProject());
		blankPropertiesPanel = new BlankPropertiesPanel();
		measurementPropertiesPanel = new MeasurementPropertiesPanel(getProject());
		
		add(indicatorPropertiesPanel);
		add(blankPropertiesPanel);
		add(measurementPropertiesPanel);
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
		
		return blankPropertiesPanel;
	}
	
	public static final String PANEL_DESCRIPTION = "Planning Properties Panel";
	
	private CardLayout cardLayout;
	
	private IndicatorPropertiesPanel indicatorPropertiesPanel;
	private BlankPropertiesPanel blankPropertiesPanel;
	private MeasurementPropertiesPanel measurementPropertiesPanel;
}
