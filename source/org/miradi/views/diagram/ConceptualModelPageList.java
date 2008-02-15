/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import org.miradi.actions.ActionCreateConceptualModel;
import org.miradi.actions.ActionDeleteConceptualModel;
import org.miradi.actions.ActionDiagramProperties;
import org.miradi.actions.ActionRenameConceptualModel;
import org.miradi.dialogs.diagram.ConceptualModelPoolTableModel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.ConceptualModelDiagram;


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
				ActionDiagramProperties.class,
				null,
				ActionCreateConceptualModel.class,
				ActionRenameConceptualModel.class,
				ActionDeleteConceptualModel.class,
		};
	}
}
