/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;


public class ConceptualModelPageList extends DiagramPageList
{
	public ConceptualModelPageList(Project project)
	{
		super(project, ObjectType.CONCEPTUAL_MODEL_DIAGRAM);
	}

	public boolean isConceptualModelPageList()
	{
		return true;
	}

	public boolean isResultsChainPageList()
	{
		return false;
	}
}
