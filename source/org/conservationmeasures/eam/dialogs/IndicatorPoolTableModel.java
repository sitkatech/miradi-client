/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;


public class IndicatorPoolTableModel extends ObjectPoolTableModel
{
	public IndicatorPoolTableModel(Project projectToUse)
	{
		super(projectToUse, ObjectType.INDICATOR, COLUMN_TAGS);
	}
	
	private static final String[] COLUMN_TAGS = new String[] {
		Indicator.TAG_SHORT_LABEL,
		Indicator.TAG_LABEL,
		Indicator.PSEUDO_TAG_FACTOR,
	};

}
