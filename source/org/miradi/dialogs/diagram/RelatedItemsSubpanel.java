/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.diagram;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objects.Strategy;
import org.miradi.project.Project;

public class RelatedItemsSubpanel extends ObjectDataInputPanel
{
	public RelatedItemsSubpanel(Project projectToUse, int objectType)
	{
		super(projectToUse, objectType);
		
		addField(createReadonlyTextField(Strategy.PSEUDO_TAG_TARGETS));
		addField(createReadonlyTextField(Strategy.PSEUDO_TAG_DIRECT_THREATS));
		addField(createReadonlyTextField(Strategy.PSEUDO_TAG_GOALS));
		addField(createReadonlyTextField(Strategy.PSEUDO_TAG_OBJECTIVES));		
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Related Items");
	}

}
