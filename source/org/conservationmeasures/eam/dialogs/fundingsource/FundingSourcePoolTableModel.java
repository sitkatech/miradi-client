/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.fundingsource;

import org.conservationmeasures.eam.dialogs.base.ObjectPoolTableModel;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.FundingSource;
import org.conservationmeasures.eam.project.Project;

public class FundingSourcePoolTableModel extends ObjectPoolTableModel
{
	public FundingSourcePoolTableModel(Project project)
	{
		super(project, ObjectType.FUNDING_SOURCE, COLUMN_TAGS);
	}

	private static final String[] COLUMN_TAGS = new String[] {
		FundingSource.TAG_CODE,
		FundingSource.TAG_LABEL,
	};
}
