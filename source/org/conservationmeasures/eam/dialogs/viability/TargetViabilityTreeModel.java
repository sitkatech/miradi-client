/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.GenericTreeTableModel;
import org.conservationmeasures.eam.views.TreeTableNode;

public class TargetViabilityTreeModel extends GenericTreeTableModel
{
	public TargetViabilityTreeModel(Project projectToUse, FactorId targetId)
	{
		super(new TargetViabilityRoot(projectToUse, targetId));
		project = projectToUse;
	}
	
	public Object getValueAt(Object rawNode, int column)
	{
		TreeTableNode node = (TreeTableNode)rawNode;
		if ((node.getType() == Target.getObjectType()) && (getColumnName(column).equals("Status")))
		{
			return ((Target)node.getObject()).computeTNCViability();
		}
		return super.getValueAt(rawNode, column);
	}

	public int getColumnCount()
	{
		return columnTags.length;
	}

	public String getColumnName(int column)
	{
		return EAM.fieldLabel(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, columnTags[column]);
	}
	
	public static String[] columnTags = {"Item", "Status"};
	Project project;

}
