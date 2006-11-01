/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.util.Comparator;

import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ValueOption;
import org.conservationmeasures.eam.project.ThreatRatingBundle;
import org.conservationmeasures.eam.project.ThreatRatingFramework;

public class ComparableNode implements  Comparator
{
	
	public ComparableNode(int sortColumnToUse, NonEditableThreatMatrixTableModel modelToUse )
	{
		sortColumn = sortColumnToUse;
		model = modelToUse;
		targetList = model.getTargets();
		framework = model.getFramework();
	}

	public int compare(Object object1, Object object2)
	{
		try
		{
			ModelNodeId nodeIdColumn = targetList[sortColumn].getModelNodeId();

			ModelNodeId nodeIdRow1 = ((ConceptualModelNode) object1).getModelNodeId();
			ModelNodeId nodeIdRow2 = ((ConceptualModelNode) object2).getModelNodeId();

			ThreatRatingBundle bundle1 = model.getBundle(nodeIdRow1,nodeIdColumn);
			ThreatRatingBundle bundle2 = model.getBundle(nodeIdRow2,nodeIdColumn);

			if (bundle1==null && bundle2==null) return 0;
			if (bundle1==null) return -1;
			if (bundle2==null) return 1;
			
			ValueOption valueOption1 = framework.getBundleValue(bundle1);
			ValueOption valueOption2 = framework.getBundleValue(bundle2);

			Integer value1 = new Integer(valueOption1.getNumericValue());
			Integer value2 = new Integer(valueOption2.getNumericValue());
			
			return value1.compareTo(value2);
		}
		catch(Exception e)
		{
			return -1;
		}
	}

	int sortColumn = 0;
	ConceptualModelNode[] targetList;
	NonEditableThreatMatrixTableModel model;
	ThreatRatingFramework framework;

}
