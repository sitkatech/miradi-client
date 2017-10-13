/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.xml.xmpz2;

import org.miradi.dialogs.threatrating.upperPanel.AbstractThreatPerRowTableModel;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Factor;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ThreatRatingQuestion;
import org.miradi.schemas.BaseObjectSchema;

abstract public class BaseObjectWithThreatRatingExporter extends BaseObjectExporter
{
	public BaseObjectWithThreatRatingExporter(Xmpz2XmlWriter writerToUse, int objectTypeToUse)
	{
		super(writerToUse, objectTypeToUse);
	}
	
	@Override
	protected void writeFields(final BaseObject baseObject, final BaseObjectSchema baseObjectSchema) throws Exception
	{
		super.writeFields(baseObject, baseObjectSchema);
		
		if (hasThreatRatingToExport())
			exportThreatTargetRating((Factor) baseObject);
	}
	
	protected boolean hasThreatRatingToExport()
	{
		return true;
	}

	private void exportThreatTargetRating(Factor factor) throws Exception
	{
		if (getProject().isStressBaseMode())
			exportStressBasedThreatRatingThreatTargetRating(factor);
		else
			exportSimpleThreatRatingThreatTargetRating(factor);
	}
	
	private void exportStressBasedThreatRatingThreatTargetRating(Factor factor) throws Exception
	{
		int rawTargetRatingValue = getProject().getStressBasedThreatRatingFramework().get2PrimeSummaryRatingValue(factor);
		ChoiceItem targetThreatRating = AbstractThreatPerRowTableModel.convertThreatRatingCodeToChoiceItem(rawTargetRatingValue);
		getWriter().writeNonOptionalCodeElement(getContainerElementName(), CALCULATED_THREAT_RATING, new ThreatRatingQuestion(), targetThreatRating.getCode());
	}

	private void exportSimpleThreatRatingThreatTargetRating(Factor factor) throws Exception
	{
		ChoiceItem threatTargetRating = getSimpleModeThreatRating(factor);
		getWriter().writeNonOptionalCodeElement(getContainerElementName(), CALCULATED_THREAT_RATING, new ThreatRatingQuestion(), threatTargetRating.getCode());
	}

	abstract protected ChoiceItem getSimpleModeThreatRating(Factor factor) throws Exception;
	
	abstract protected String getContainerElementName();
}
