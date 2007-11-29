/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.properties;

import org.conservationmeasures.eam.dialogs.base.EditableObjectTable;
import org.conservationmeasures.eam.dialogs.threatstressrating.ThreatStressRatingTableModel;
import org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel.TargetSummaryRowTable;

public class ThreatStressRatingTable extends EditableObjectTable
{
	public ThreatStressRatingTable(ThreatStressRatingTableModel threatStressRatingTableModel)
	{
		super(threatStressRatingTableModel);
		rebuildColumnEditorsAndRenderers();
		//TODO shouldn't set row height to constant value
		setRowHeight(25);
		setForcedPreferredScrollableViewportHeight(100);
		setForcedPreferredScrollableViewportWidth(TargetSummaryRowTable.PREFERRED_VIEWPORT_WIDTH);
	}
	
	public ThreatStressRatingTableModel getThreatStressRatingTableModel()
	{
		return (ThreatStressRatingTableModel) getModel();
	}
	
	public void rebuildColumnEditorsAndRenderers()
	{
		for (int tableColumn = 0; tableColumn < getThreatStressRatingTableModel().getColumnCount(); ++tableColumn)
		{
			if (getThreatStressRatingTableModel().isStressRatingColumn(tableColumn))
				createReadonlyStatusRatingColumn(tableColumn);
			if (getThreatStressRatingTableModel().isContributionColumn(tableColumn))
				createComboStatusRatingColumn(tableColumn);
			if (getThreatStressRatingTableModel().isIrreversibilityColumn(tableColumn))
				createComboStatusRatingColumn(tableColumn);
			if (getThreatStressRatingTableModel().isThreatRatingColumn(tableColumn))
				createReadonlyStatusRatingColumn(tableColumn);
		}
	}

	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}

	public static final String UNIQUE_IDENTIFIER = "ThreatStressRatingTable";
}
