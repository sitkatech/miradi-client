/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.views.ObjectsDoer;

abstract public class DiagramPageDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
		
		if (! isInDiagram())
			return false;
		
		if (!isCorrectTab())
			return false;
		
		if (isInvalidSelection())
			return false;
		
		return true;
	}
	
	private boolean isInvalidSelection()
	{
		try
		{
			String currentObjectTag = getDiagramObjectTag();
			String oRefAsString = getProject().getCurrentViewData().getData(currentObjectTag);
			ORef currentConceptualModelRef = ORef.createFromString(oRefAsString);
			return currentConceptualModelRef.isInvalid();			
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		return true;
	}
	
	abstract public String getDiagramObjectTag();
	
	abstract public boolean isCorrectTab();
}
