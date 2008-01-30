/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.project.Project;

public class ThreatMatrixCommentPanel extends ObjectDataInputPanel
{
	public ThreatMatrixCommentPanel(Project projectToUse)
	{
		super(projectToUse, FactorLink.getObjectType(), BaseId.INVALID);
		
		addField(createMultilineField(FactorLink.getObjectType(), FactorLink.TAG_SIMPLE_THREAT_RATING_COMMENT, 30));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return "ThreatMatrixCommentPanel";
	}
}
