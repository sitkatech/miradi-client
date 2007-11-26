/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.properties;

import org.conservationmeasures.eam.dialogs.base.EditableObjectTable;
import org.conservationmeasures.eam.dialogs.threatstressrating.ThreatStressRatingTableModel;
import org.conservationmeasures.eam.objects.ThreatStressRating;

public class ThreatStressRatingTable extends EditableObjectTable
{
	public ThreatStressRatingTable(ThreatStressRatingTableModel threatStressRatingTableModel)
	{
		super(threatStressRatingTableModel);
		model = threatStressRatingTableModel;
		rebuildColumnEditorsAndRenderers();
	}
	
	public ThreatStressRatingTableModel getThreatStressRatingTableModel()
	{
		return model;
	}
	
	public void rebuildColumnEditorsAndRenderers()
	{
		for (int i = 0; i < getThreatStressRatingTableModel().getColumnCount(); ++i)
		{
			createContributionColumn(i);
		}
	}

	private void createContributionColumn(int column)
	{
		if (! getThreatStressRatingTableModel().getColumnTag(column).equals(ThreatStressRating.TAG_CONTRIBUTION))
			return;
		//still in development
		//FIXME use question instead of the threat list
		//ThreatStressRating[] threatStressRatings = getObjectManager().getThreatStressRatingPool().getAllThreatStressRatings();
		//ThreatStressRating invalidThreatStressRating = new ThreatStressRating(getObjectManager(), BaseId.INVALID);
		//createComboColumn(threatStressRatings, column, invalidThreatStressRating);
	}
		
	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}

	public static final String UNIQUE_IDENTIFIER = "ThreatStressRatingTable";
	private ThreatStressRatingTableModel model;
}
