/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.project.Project;


//TODO: There should be no need to re case this as a root...the system should be able to determine the root by some other means the a null return on this mehtod
public class TargetViabilityRoot extends TargetViabilityNode
{
	public TargetViabilityRoot(Project projectToUse, FactorId targetId) throws Exception
	{
		super(projectToUse, targetId);
	}

	public boolean isAlwaysExpanded()
	{
		return true;
	}
}
