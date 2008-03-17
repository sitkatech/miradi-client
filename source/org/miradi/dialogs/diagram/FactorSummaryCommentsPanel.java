/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.diagram;

import org.miradi.actions.Actions;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objects.Factor;
import org.miradi.project.Project;

public class FactorSummaryCommentsPanel extends ObjectDataInputPanel
{
	public FactorSummaryCommentsPanel(Project project, Actions actions, int factorType)
	{
		super(project, factorType);

		addField(createMultilineField(factorType, Factor.TAG_COMMENT));		
	}

	@Override
	public String getPanelDescription()
	{
		// TODO Auto-generated method stub
		return EAM.text("");
	}

}
