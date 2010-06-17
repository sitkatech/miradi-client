/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.xmpz;

import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.objects.Task;
import org.miradi.questions.PriorityRatingQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.xml.wcs.WcsXmlConstants;
import org.w3c.dom.Node;

public class IndicatorPoolImporter extends AbstractBaseObjectPoolImporter
{
	public IndicatorPoolImporter(XmpzXmlImporter importerToUse)
	{
		super(importerToUse, WcsXmlConstants.INDICATOR, Indicator.getObjectType());
	}
	
	@Override
	protected void importFields(Node node, ORef destinationRef) throws Exception
	{
		super.importFields(node, destinationRef);

		importField(node, destinationRef, Indicator.TAG_SHORT_LABEL);
		importField(node, destinationRef, Indicator.TAG_DETAIL);
		importField(node, destinationRef, Indicator.TAG_COMMENTS);
		importCodeField(node, destinationRef, Indicator.TAG_PRIORITY, new PriorityRatingQuestion());
		importField(node, destinationRef, Indicator.TAG_FUTURE_STATUS_DATE);
		importField(node, destinationRef, Indicator.TAG_FUTURE_STATUS_SUMMARY);
		importCodeField(node, destinationRef, Indicator.TAG_FUTURE_STATUS_RATING, new StatusQuestion());
		importField(node, destinationRef, Indicator.TAG_FUTURE_STATUS_DETAIL);
		importField(node, destinationRef, Indicator.TAG_FUTURE_STATUS_COMMENT);
		importProgressReportRefs(node, destinationRef);
		importExpenseAssignmentRefs(node, destinationRef);
		importResourceAssignmentIds(node, destinationRef);
		importIds(node, destinationRef, Indicator.TAG_METHOD_IDS, Task.getObjectType(), WcsXmlConstants.METHOD);
		importRefs(node, WcsXmlConstants.MEASUREMENT_IDS, destinationRef, Indicator.TAG_MEASUREMENT_REFS, Measurement.getObjectType(), WcsXmlConstants.MEASUREMENT);
	}
}
