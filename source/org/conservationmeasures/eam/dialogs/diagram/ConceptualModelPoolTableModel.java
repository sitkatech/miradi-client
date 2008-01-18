/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.diagram;

import org.conservationmeasures.eam.dialogs.base.ObjectPoolTableModel;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
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
		ORefList diagramPageRefs = getProject().getConceptualModelDiagramPool().getORefList();
		if (conceptualModelDiagram.toString().trim().length() == 0 && diagramPageRefs.size() == 1)
			return ConceptualModelDiagram.DEFAULT_MAIN_NAME;
		
		return ConceptualModelDiagram.DEFAULT_BLANK_NAME;
	}
}
