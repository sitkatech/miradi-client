/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
import org.miradi.objects.FactorLink;
import org.miradi.project.Project;

public class TargetThreatLinkTableModel extends MainThreatTableModel
{
	public TargetThreatLinkTableModel(Project projectToUse)
	{
		super(projectToUse);
	}
	
	public int getColumnCount()
	{
		return targetColumns.length;
	}
	
	public String getTableColumnSequenceKey(int column)
	{
		return targetColumns[column].getRef().toString();
	}

	public String getColumnName(int column)
	{
		return targetColumns[column].toString();
	}
	
	public Object getValueAt(int row, int column)
	{
		String valueToConvert = getFactorLinkThreatRatingBundle(row, column);
		return convertToChoiceItem(FactorLink.PSEUDO_TAG_THREAT_RATING_BUNDLE_VALUE, valueToConvert);	
	}

	private String getFactorLinkThreatRatingBundle(int row, int column)
	{
		try
		{
			if (!areLinked(row, column))
				return null;
			
			FactorLink factorLink = getFactorLink(row, column);
			int calculatedValue = factorLink.calculateThreatRatingBundleValue();
			return convertIntToString(calculatedValue);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return "ERROR";
		}
	}

	private FactorLink getFactorLink(int row, int column)
	{
		return FactorLink.find(getProject(), getLinkRef(getDirectThreat(row), getTarget(column)));
	}

	private boolean areLinked(int row, int column)
	{
		return areLinked(getDirectThreat(row), getTarget(column));
	}
	
	public boolean areLinked(Factor from, Factor to)
	{
		return getProject().getFactorLinkPool().areLinked(from, to);
	}
	
	public String getColumnTag(int column)
	{
		return "";
	}

	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		if (areLinked(row, column))
			return getFactorLink(row, column);
			
		return null;
	}
	
	public Comparator getComparator(int columnToSortOn)
	{
		return new TableModelChoiceItemComparator(this, columnToSortOn);
	}
}
