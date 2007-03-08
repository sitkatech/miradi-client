/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.GenericTreeTableModel;

public class TargetViabilityTreeModel extends GenericTreeTableModel
{
	public TargetViabilityTreeModel(Project projectToUse, FactorId targetId)
	{
		super(new TargetViabilityRoot(projectToUse, targetId));
		project = projectToUse;
	}

	public int getColumnCount()
	{
		return columnTags.length;
	}

	public String getColumnName(int column)
	{
		return EAM.fieldLabel(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, columnTags[column]);
	}
	
	public static String[] columnTags = {"Item", };
	Project project;

}
