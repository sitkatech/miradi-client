/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.threatmatrix;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.ids.BaseId;
import org.miradi.objects.FactorLink;
import org.miradi.project.Project;

public class ThreatMatrixCommentPanel extends ObjectDataInputPanel
{
	public ThreatMatrixCommentPanel(Project projectToUse)
	{
		super(projectToUse, FactorLink.getObjectType(), BaseId.INVALID);
		
		addField(createMultilineField(FactorLink.getObjectType(), FactorLink.TAG_SIMPLE_THREAT_RATING_COMMENT, 15));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return "ThreatMatrixCommentPanel";
	}
}
