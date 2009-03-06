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

import java.awt.Color;

import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Factor;
import org.miradi.project.Project;
import org.miradi.project.threatrating.StressBasedThreatRatingFramework;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ThreatRatingQuestion;
import org.miradi.utils.ColumnTagProvider;

abstract public class MainThreatTableModel extends AbstractThreatTargetTableModel implements ColumnTagProvider, RowColumnBaseObjectProvider
{
	public MainThreatTableModel(Project projectToUse)
	{
		super(projectToUse);
		
		emptyChoiceItem = new ChoiceItem("Not Specified", "", Color.WHITE);
		frameWork = new StressBasedThreatRatingFramework(getProject());
	}

	public int getRowCount()
	{
		return threatRows.length;
	}
	
	public int getProportionShares(int row)
	{
		return 1;
	}

	public boolean areBudgetValuesAllocated(int row)
	{
		return false;
	}

	public ORef getLinkRef(Factor from, Factor to)
	{
		return getProject().getFactorLinkPool().getLinkedRef(from, to);
	}
	
	protected String convertIntToString(int calculatedValue)
	{
		if (calculatedValue == 0)
			return "";
		
		return Integer.toString(calculatedValue);
	}
	
	protected ChoiceItem convertThreatRatingCodeToChoiceItem(String valueToConvert)
	{
		ChoiceItem foundChoiceItem = new ThreatRatingQuestion().findChoiceByCode(valueToConvert);
		if (foundChoiceItem == null)
			return emptyChoiceItem;
		
		return foundChoiceItem;
	}
	
	private ChoiceItem emptyChoiceItem;
	protected StressBasedThreatRatingFramework frameWork;
}
