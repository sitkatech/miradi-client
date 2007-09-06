/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.utils.TableWithHelperMethods;

public class PlanningViewResourceTable extends TableWithHelperMethods
{
	public PlanningViewResourceTable(PlanningViewResourceTableModel modelToUse)
	{
		super(modelToUse);
		model = modelToUse;
	}
	
	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
		model.setObjectRefs(hierarchyToSelectedRef);
	}
	
	private PlanningViewResourceTableModel model;
}
