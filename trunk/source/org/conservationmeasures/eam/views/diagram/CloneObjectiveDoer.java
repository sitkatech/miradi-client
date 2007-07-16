/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.dialogs.ObjectivePoolTablePanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.BaseObject;

public class CloneObjectiveDoer extends CreateObjective
{
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
	
		BaseObject cloneAnnotation = displayAnnotationList(EAM.text("Choose Objective to Clone"), new ObjectivePoolTablePanel(getProject()));	
		
		if (cloneAnnotation == null)
			return;
		
		setAnnotationToClone(cloneAnnotation);
		
		super.doIt();
	}

}
