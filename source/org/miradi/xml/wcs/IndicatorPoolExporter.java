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
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.questions.PriorityRatingQuestion;
import org.miradi.questions.StatusQuestion;

public class IndicatorPoolExporter extends BaseObjectPoolExporter
{
	public IndicatorPoolExporter(WcsXmlExporter wcsXmlExporterToUse)
	{
		super(wcsXmlExporterToUse, INDICATOR, Indicator.getObjectType());
	}
	
	@Override
	protected void exportFields(UnicodeWriter writer, BaseObject baseObject) throws Exception
	{
		super.exportFields(writer, baseObject);

		Indicator indicator = (Indicator) baseObject;
		writeOptionalElementWithSameTag(baseObject, Indicator.TAG_SHORT_LABEL);
		writeOptionalElementWithSameTag(baseObject, Indicator.TAG_DETAIL);
		writeOptionalElementWithSameTag(baseObject, Indicator.TAG_COMMENTS);
		writeCodeElementSameAsTag(indicator, Indicator.TAG_PRIORITY, new PriorityRatingQuestion());
		writeOptionalElementWithSameTag(baseObject, Indicator.TAG_FUTURE_STATUS_DATE);
		writeOptionalElementWithSameTag(baseObject, Indicator.TAG_FUTURE_STATUS_SUMMARY);
		writeCodeElementSameAsTag(indicator, Indicator.TAG_FUTURE_STATUS_RATING, new StatusQuestion());
		writeOptionalElementWithSameTag(baseObject, Indicator.TAG_FUTURE_STATUS_DETAIL);
		writeOptionalElementWithSameTag(baseObject, Indicator.TAG_FUTURE_STATUS_COMMENT);	
		writeProgressReportIds(indicator);
		writeExpenseAssignmentIds(indicator);
		writeResourceAssignmentIds(indicator);
		writeMeasurementIds(indicator.getMeasurementRefs());
	}
	
	private void writeMeasurementIds(ORefList measurementRefs) throws Exception
	{
		writeIds(WcsXmlConstants.MEASUREMENT_IDS, WcsXmlConstants.MEASUREMENT, measurementRefs);
	}
}
