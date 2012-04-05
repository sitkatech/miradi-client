/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.xmpz2;

import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Desire;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.xml.wcs.XmpzXmlConstants;

public class DesireExporter extends BaseObjectExporter
{
	public DesireExporter(Xmpz2XmlUnicodeWriter writerToUse)
	{
		super(writerToUse);
	}
	
	@Override
	public void writeBaseObjectDataSchemaElement(BaseObject baseObject)	throws Exception
	{
		super.writeBaseObjectDataSchemaElement(baseObject);

		final Desire desire = (Desire) baseObject;
		writeRelevantIndicatorIds(desire);
		writeRelevantStrategyIds(desire);
		writeRelevantActivityIds(desire);
	}
	
	@Override
	protected boolean isCustomExportedField(final String tag)
	{
		if (tag.equals(Desire.TAG_RELEVANT_INDICATOR_SET))
			return true;
		
		if (tag.equals(Desire.TAG_RELEVANT_STRATEGY_ACTIVITY_SET))
			return true;
		
		return super.isCustomExportedField(tag);
	}
	
	private void writeRelevantIndicatorIds(Desire desire) throws Exception
	{
		getWriter().writeReflist(RELEVANT_INDICATOR_IDS, INDICATOR, desire.getRelevantIndicatorRefList());
	}
	
	private void writeRelevantStrategyIds(Desire desire) throws Exception
	{
		ORefList relevantStrategyRefs = desire.getRelevantStrategyAndActivityRefs().getFilteredBy(StrategySchema.getObjectType());
		getWriter().writeReflist(RELEVANT_STRATEGY_IDS, STRATEGY, relevantStrategyRefs);
	}
	
	private void writeRelevantActivityIds(Desire desire) throws Exception
	{
		ORefList relevantActivityRefs = desire.getRelevantStrategyAndActivityRefs().getFilteredBy(TaskSchema.getObjectType());
		getWriter().writeReflist(XmpzXmlConstants.RELEVANT_ACTIVITY_IDS, XmpzXmlConstants.ACTIVITY, relevantActivityRefs);
	}
}
