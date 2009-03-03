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

import java.util.Comparator;

import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Factor;
import org.miradi.project.Project;

public class ThreatSummaryColumnTableModel extends MainThreatTableModel
{
	public ThreatSummaryColumnTableModel(Project projectToUse)
	{
		super(projectToUse);
	}

	public String getColumnName(int column)
	{
		return EAM.text("Summary Threat Rating");
	}
	
	public String getColumnTag(int column)
	{
		return "";
	}

	public int getColumnCount()
	{
		return 1;
	}

	public Object getValueAt(int row, int column)
	{
		String valueToConvert = getCalculatedThreatSummaryRatingValue(row);
		return convertToChoiceItem(valueToConvert);
	}
	
	private String getCalculatedThreatSummaryRatingValue(int row)
	{
		try
		{
			int calculatedValue = calculateThreatSummaryRatingValue(threatRows[row]);
			return convertIntToString(calculatedValue);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return EAM.text("Error");
		}
	}
	
	public int calculateThreatSummaryRatingValue(Factor directThreat) throws Exception
	{
		return frameWork.get2PrimeSummaryRatingValue(directThreat);
	}
	
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return getDirectThreat(row);
	}
	
	public Comparator getComparator(int columnToSortOn)
	{
		return new TableModelChoiceItemComparator(this, columnToSortOn);
	}
}
