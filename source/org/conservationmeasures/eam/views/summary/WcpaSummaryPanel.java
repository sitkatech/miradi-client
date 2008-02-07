/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.WcpaProjectData;
import org.conservationmeasures.eam.project.Project;

public class WcpaSummaryPanel extends ObjectDataInputPanel
{
	public WcpaSummaryPanel(Project projectToUse)
	{
		super(projectToUse, projectToUse.getSingletonObjectRef(WcpaProjectData.getObjectType()));
		
		addField(createMultilineField(WcpaProjectData.TAG_LEGAL_STATUS));
		addField(createMultilineField(WcpaProjectData.TAG_LEGISLATIVE));
		addField(createMultilineField(WcpaProjectData.TAG_PHYSICAL_DESCRIPTION));
		addField(createMultilineField(WcpaProjectData.TAG_BIOLOGICAL_DESCRIPTION));
		addField(createMultilineField(WcpaProjectData.TAG_SOCIO_ECONOMIC_INFORMATION));
		addField(createMultilineField(WcpaProjectData.TAG_HISTORICAL_DESCRIPTION));
		addField(createMultilineField(WcpaProjectData.TAG_CULTURAL_DESCRIPTION));
		addField(createMultilineField(WcpaProjectData.TAG_ACCESS_INFORMATION));
		addField(createMultilineField(WcpaProjectData.TAG_VISITATION_INFORMATION));
		addField(createMultilineField(WcpaProjectData.TAG_CURRENT_LAND_USES));
		addField(createMultilineField(WcpaProjectData.TAG_MANAGEMENT_RESOURCES));				
						
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Label|WCPA");
	}
}
