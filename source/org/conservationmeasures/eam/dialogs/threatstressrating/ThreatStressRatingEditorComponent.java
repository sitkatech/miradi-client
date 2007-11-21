/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating;

import org.conservationmeasures.eam.dialogs.base.AbstractEditorComponent;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.Project;

public class ThreatStressRatingEditorComponent extends AbstractEditorComponent
{
	public ThreatStressRatingEditorComponent(Project projectToUse) throws Exception
	{
		super(projectToUse);
		
		createTables();
	}
	
	private void createTables() throws Exception
	{
		threatStressRatingTableModel = new ThreatStressRatingTableModel(getProject());
		threatStressRatingTable = new ThreatStressRatingTable(threatStressRatingTableModel);
	}

	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
		//udate model here
	}
	
	public void dataWasChanged()
	{
		//udate model and table(repaint) here
	}
	
	ThreatStressRatingTableModel threatStressRatingTableModel;
	ThreatStressRatingTable threatStressRatingTable;
}
