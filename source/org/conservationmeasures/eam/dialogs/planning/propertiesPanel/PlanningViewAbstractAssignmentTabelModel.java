/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.project.Project;

abstract public class PlanningViewAbstractAssignmentTabelModel extends AbstractTableModel
{

	public PlanningViewAbstractAssignmentTabelModel(Project projectToUse)
	{
		project = projectToUse;
	}

	public Project getProject()
	{
		return project;
	}

	private Project project;
}
