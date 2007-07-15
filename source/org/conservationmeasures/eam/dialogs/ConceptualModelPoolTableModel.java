/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.project.Project;

public class ConceptualModelPoolTableModel extends ObjectPoolTableModel
{
	public ConceptualModelPoolTableModel(Project projectToUse, int listedItemType, String[] columnTagsToUse)
	{
		super(projectToUse, listedItemType, columnTagsToUse);
	}

	public String getValueToDisplay(ORef rowObjectRef, String tag)
	{
		String valueToDisplay = super.getValueToDisplay(rowObjectRef, tag);
		if (valueToDisplay.trim().length() != 0)
			return valueToDisplay;
		
		ConceptualModelDiagram conceptualModelDiagram = (ConceptualModelDiagram) getProject().findObject(rowObjectRef);
		return conceptualModelDiagram.toString();
	}
}
