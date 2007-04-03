/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class StrategyPoolTablePanel extends ObjectPoolTablePanel
{
	public StrategyPoolTablePanel(Project projectToUse)
	{
		super(projectToUse, ObjectType.STRATEGY, new StrategyPoolTableModel(projectToUse));
	}
}
