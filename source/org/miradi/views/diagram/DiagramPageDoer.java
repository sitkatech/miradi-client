/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.views.ObjectsDoer;

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
