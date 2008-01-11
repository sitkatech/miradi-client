/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.actions.ActionCreateConceptualModel;
import org.conservationmeasures.eam.actions.ActionDeleteConceptualModel;
import org.conservationmeasures.eam.actions.ActionDiagramProperties;
import org.conservationmeasures.eam.dialogs.diagram.ConceptualModelPoolTableModel;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;


public class ConceptualModelPageList extends DiagramPageList
{
	public ConceptualModelPageList(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, new ConceptualModelPoolTableModel(mainWindowToUse.getProject(), ObjectType.CONCEPTUAL_MODEL_DIAGRAM, getTags()));
	}

	private static String[] getTags()
	{
		return new String[] {ConceptualModelDiagram.PSEUDO_COMBINED_LABEL};
	}
	
	public boolean isConceptualModelPageList()
	{
		return true;
	}

	public boolean isResultsChainPageList()
	{
		return false;
	}

	public int getManagedDiagramType()
	{
		return ObjectType.CONCEPTUAL_MODEL_DIAGRAM;
	}

	public Class[] getPopUpMenuActions()
	{
		return  new Class[] {
				ActionCreateConceptualModel.class,
				ActionDeleteConceptualModel.class,
				ActionDiagramProperties.class,
		};
	}
}
