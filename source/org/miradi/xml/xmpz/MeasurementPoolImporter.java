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
import org.miradi.objects.Measurement;
import org.miradi.questions.StatusConfidenceQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.TrendQuestion;
import org.miradi.xml.wcs.WcsXmlConstants;
import org.w3c.dom.Node;

public class MeasurementPoolImporter extends AbstractBaseObjectPoolImporter
{
	public MeasurementPoolImporter(XmpzXmlImporter importerToUse)
	{
		super(importerToUse, WcsXmlConstants.MEASUREMENT, Measurement.getObjectType());
	}
	
	@Override
	protected void importFields(Node node, ORef destinationRef)	throws Exception
	{
		super.importFields(node, destinationRef);
		
		importCodeField(node, destinationRef, Measurement.TAG_TREND, new TrendQuestion());
		importCodeField(node, destinationRef, Measurement.TAG_STATUS, new StatusQuestion());
		importField(node, destinationRef,Measurement.TAG_DATE);
		importField(node, destinationRef,Measurement.TAG_SUMMARY);
		importField(node, destinationRef,Measurement.TAG_DETAIL);
		importCodeField(node, destinationRef, Measurement.TAG_STATUS_CONFIDENCE, new StatusConfidenceQuestion());
		importField(node, destinationRef,Measurement.TAG_COMMENTS);
	}
}
