/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.CardLayout;
import java.util.Vector;

import org.conservationmeasures.eam.dialogs.ObjectDataInputPanel;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.Project;

public class PlanningTreePropertiesPanel extends ObjectDataInputPanel
{
	public PlanningTreePropertiesPanel(Project projectToUse, ORef orefToUse)
	{
		super(projectToUse, orefToUse);
		project = projectToUse;
		setLayout(new CardLayout());
		createPropertiesPanels();
	}

	private void createPropertiesPanels()
	{
		//FIXME create the properies panels here
	}
	
	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	protected Vector getFields()
	{
		return super.getFields();
	}

	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
		
	}
	
	public static final String PANEL_DESCRIPTION = "Planning Properties Panel";
	
	Project project;
}
