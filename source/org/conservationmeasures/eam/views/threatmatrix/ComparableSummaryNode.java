/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.util.Comparator;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ValueOption;
import org.conservationmeasures.eam.project.ThreatRatingFramework;

public class ComparableSummaryNode implements  Comparator
{
	
	public ComparableSummaryNode(int sortColumnToUse, NonEditableThreatMatrixTableModel modelToUse )
	{
		sortColumn = sortColumnToUse;
		model = modelToUse;
		framework = model.getFramework();
	}

	public int compare(Object object1, Object object2)
	{
		try
		{
			BaseId baseId1 = ((ConceptualModelNode) object1).getId();
			ValueOption valueOption1 = framework.getThreatThreatRatingValue(baseId1);

			BaseId baseId2 = ((ConceptualModelNode) object2).getId();
			ValueOption valueOption2 = framework.getTargetThreatRatingValue(baseId2);
			
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
	NonEditableThreatMatrixTableModel model;
	ThreatRatingFramework framework;

}
