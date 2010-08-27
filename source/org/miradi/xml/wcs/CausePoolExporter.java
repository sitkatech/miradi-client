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

package org.miradi.xml.wcs;

import org.martus.util.UnicodeWriter;
import org.miradi.dialogs.threatrating.upperPanel.AbstractThreatPerRowTableModel;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ThreatRatingQuestion;

public class CausePoolExporter extends FactorPoolExporter
{	
	public CausePoolExporter(WcsXmlExporter wcsXmlExporterToUse)
	{
		super(wcsXmlExporterToUse, CAUSE, Cause.getObjectType());
	}
	
	@Override
	protected void exportFields(UnicodeWriter writer, BaseObject baseObject) throws Exception
	{
		super.exportFields(writer, baseObject);
		
		Cause cause = (Cause) baseObject;
		
		writeOptionalElementWithSameTag(cause, Cause.TAG_IS_DIRECT_THREAT);
		writeOptionalElementWithSameTag(cause, Cause.TAG_TAXONOMY_CODE);
		writeObjectiveIds(cause);
		writeIndicatorIds(cause);
		writeThreatThreatRatingCode(cause);
	}

	private void writeThreatThreatRatingCode(Cause cause) throws Exception
	{
		if (getProject().isStressBaseMode())
			exportStressBasedThreatRatingThreatThreatRating(cause);
		else
			exportSimpleThreatRatingThreatThreatRating(cause.getRef());
	}
	
	private void exportSimpleThreatRatingThreatThreatRating(ORef threatRef) throws Exception
	{
		ChoiceItem threatThreatRating = getProject().getSimpleThreatRatingFramework().getThreatThreatRatingValue(threatRef);
		writeOptionalCodeElement(THREAT_RATING, new ThreatRatingQuestion(), threatThreatRating.getCode());
	}
	
	private void exportStressBasedThreatRatingThreatThreatRating(Cause threat) throws Exception
	{
		int rawThreatRatingValue = getProject().getStressBasedThreatRatingFramework().get2PrimeSummaryRatingValue(threat);
		ChoiceItem threatThreatRating = AbstractThreatPerRowTableModel.convertThreatRatingCodeToChoiceItem(rawThreatRatingValue);
		writeOptionalCodeElement(THREAT_RATING, new ThreatRatingQuestion(), threatThreatRating.getCode());
	}
}
