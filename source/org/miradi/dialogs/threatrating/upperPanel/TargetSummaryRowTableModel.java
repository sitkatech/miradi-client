/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.dialogs.threatrating.upperPanel;

import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.project.Project;

public class TargetSummaryRowTableModel extends MainThreatTableModel
{
	public TargetSummaryRowTableModel(Project projectToUse)
	{
		super(projectToUse);
	}

	public String getColumnTag(int column)
	{
		return "";
	}
	
	public int getRowCount()
	{
		return 1;
	}

	public int getColumnCount()
	{
		return targetColumns.length;
	}

	public Object getValueAt(int row, int column)
	{
		String valueToConvert = getCalculatedTargetSummaryRatingValue(column);
		return convertToChoiceItem(FactorLink.PSEUDO_TAG_THREAT_RATING_BUNDLE_VALUE, valueToConvert);
	}
	
	private String getCalculatedTargetSummaryRatingValue(int column)
	{
		try
		{
			int calculatedValue = calculateThreatSummaryRatingValue(targetColumns[column]);
			return convertIntToString(calculatedValue);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return EAM.text("Error");
		}
	}

	public int calculateThreatSummaryRatingValue(Factor target) throws Exception
	{
		return frameWork.get2PrimeSummaryRatingValue(target);
	}

	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return targetColumns[column];
	}
}
