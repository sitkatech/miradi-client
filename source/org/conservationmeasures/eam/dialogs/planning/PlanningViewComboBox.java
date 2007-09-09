/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.UiComboBoxWithSaneActionFiring;

abstract public class PlanningViewComboBox extends UiComboBoxWithSaneActionFiring
{
	public PlanningViewComboBox(Project projectToUse)
	{
		project = projectToUse;
	}
	
	abstract public String[] getRowList() throws Exception;
	abstract public String[] getColumnList() throws Exception;
	abstract public String getPropertyName();

	Project project;
}
