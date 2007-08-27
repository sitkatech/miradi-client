/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.CardLayout;
import java.util.Vector;

import org.conservationmeasures.eam.dialogs.IndicatorPropertiesPanel;
import org.conservationmeasures.eam.dialogs.ObjectDataInputPanel;
import org.conservationmeasures.eam.dialogs.StrategyPropertiesPanel;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.project.Project;

public class PlanningTreePropertiesPanel extends ObjectDataInputPanel
{
	public PlanningTreePropertiesPanel(Project projectToUse, ORef orefToUse) throws Exception
	{
		super(projectToUse, orefToUse);
		project = projectToUse;
		cardLayout = new CardLayout();
		setLayout(cardLayout);
		createPropertiesPanels();
	}

	private void createPropertiesPanels() throws Exception
	{
		indicatorPropertiesPanel = new IndicatorPropertiesPanel(project);
		strategyPropertiesPanel = new StrategyPropertiesPanel(project);
		
		add(indicatorPropertiesPanel, indicatorPropertiesPanel.getPanelDescription());
		add(strategyPropertiesPanel, strategyPropertiesPanel.getPanelDescription());
	}
	
	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	public Vector getFields()
	{
		//FIXME Planning - is this correct to get the first ref
		ORef firstRef = getORef(0);
		if (Indicator.getObjectType() == firstRef.getObjectType())
			return indicatorPropertiesPanel.getFields();
		
		if (Strategy.getObjectType() == firstRef.getObjectType())
			return strategyPropertiesPanel.getFields();
		
		return super.getFields();
	
	}

	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
		cardLayout.show(this, getDescription(orefsToUse));
	}
	
	private String getDescription(ORef[] orefsToUse)
	{
		//FIXME planning - should not use first ref
		ORef firstRef = orefsToUse[0];
		if (Indicator.getObjectType() == firstRef.getObjectType())
			return indicatorPropertiesPanel.getPanelDescription();
		
		if (Strategy.getObjectType() == firstRef.getObjectType())
			return strategyPropertiesPanel.getPanelDescription();
		
		return "";
	}

	public static final String PANEL_DESCRIPTION = "Planning Properties Panel";
	
	Project project;
	CardLayout cardLayout;
	
	IndicatorPropertiesPanel indicatorPropertiesPanel;
	StrategyPropertiesPanel strategyPropertiesPanel;
}
