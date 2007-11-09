/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.treetables;

import javax.swing.JLabel;

import org.conservationmeasures.eam.dialogs.planning.treenodes.PlanningTreeTaskNode;
import org.conservationmeasures.eam.objects.Task;

abstract public class RendererWithHelperMethods extends RendererWithNodeForRow
{
	public void addAstrickToTaskRows(TreeTableNode node, JLabel label)
	{
		 if (label.getText().length() == 0)
			 return;
		 
		if (node.getType() != Task.getObjectType())
			return;
		
		PlanningTreeTaskNode taskNode = (PlanningTreeTaskNode) node;
		double nodeCostAlloctionProportion = taskNode.getCostAllocationProportion();
		if (nodeCostAlloctionProportion < 1.0)
			label.setText(label.getText() + "*");
	}
}
