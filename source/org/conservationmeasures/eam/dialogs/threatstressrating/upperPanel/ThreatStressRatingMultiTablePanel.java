/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import org.conservationmeasures.eam.dialogs.base.MultiTablePanel;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

public class ThreatStressRatingMultiTablePanel extends MultiTablePanel
{
	public ThreatStressRatingMultiTablePanel(Project projectToUse)
	{
		super(projectToUse);
		
		threatStressRatintListModel = new ThreatStressRatingListTableModel(projectToUse);
		threatStressRatingListTable = new ThreatStressRatingListTable(threatStressRatintListModel);
	}
	
	public ObjectPicker getObjectPicker()
	{
		return threatStressRatingListTable;
	}
	
	private ThreatStressRatingListTableModel threatStressRatintListModel;
	private ThreatStressRatingListTable threatStressRatingListTable;
}
