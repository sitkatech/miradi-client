/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating;

import org.conservationmeasures.eam.dialogs.base.EditableObjectTableModel;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.ThreatStressRating;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.ColumnTagProvider;

public class ThreatStressRatingTableModel extends EditableObjectTableModel implements ColumnTagProvider
{
	public ThreatStressRatingTableModel(Project projectToUse, ORef refToUse)
	{
		super(projectToUse);
	}
	
	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
	}

	public String getColumnTag(int column)
	{
		return getColumnTags()[column];
	}

	public int getColumnCount()
	{
		return getColumnTags().length;
	}

	public int getRowCount()
	{
		return 0;
	}

	public Object getValueAt(int arg0, int arg1)
	{
		return null;
	}
	
	public void setValueAt(Object value, int row, int column)
	{
		super.setValueAt(value, row, column);
	}
	
	public BaseObject getBaseObjectForRow(int row)
	{
		return null;
	}

	public static String[] getColumnTags()
	{
		return new String[] {
				ThreatStressRating.TAG_CONTRIBUTION,
				ThreatStressRating.TAG_IRREVERSIBILITY,
		};
	}
}
