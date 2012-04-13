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

package org.miradi.xml.xmpz2.objectExporters;

import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Desire;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.xml.wcs.XmpzXmlConstants;
import org.miradi.xml.xmpz2.BaseObjectExporter;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;

public class DesireExporter extends BaseObjectExporter
{
	public DesireExporter(Xmpz2XmlWriter writerToUse, int objectTypeToUse)
	{
		super(writerToUse, objectTypeToUse);
	}
	
	@Override
	protected void writeFields(final BaseObject baseObject,	BaseObjectSchema baseObjectSchema) throws Exception
	{
		super.writeFields(baseObject, baseObjectSchema);

		final Desire desire = (Desire) baseObject;
		final String objectName = baseObjectSchema.getObjectName();
		writeRelevantIndicatorIds(objectName, desire);
		writeRelevantStrategyIds(objectName, desire);
		writeRelevantActivityIds(objectName, desire);
		writeProgressPercentIds(objectName, desire.getProgressPercentRefs());
	}
	
	private void writeProgressPercentIds(final String parentName, ORefList progressPercentRefs) throws Exception
	{
		getWriter().writeReflist(parentName + XmpzXmlConstants.PROGRESS_PERCENT_IDS, XmpzXmlConstants.PROGRESS_PERCENT, progressPercentRefs);
	}

	@Override
	protected boolean doesFieldRequireSpecialHandling(final String tag)
	{
		if (tag.equals(Desire.TAG_RELEVANT_INDICATOR_SET))
			return true;
		
		if (tag.equals(Desire.TAG_RELEVANT_STRATEGY_ACTIVITY_SET))
			return true;
		
		if (tag.equals(Desire.TAG_PROGRESS_PERCENT_REFS))
			return true;
		
		return super.doesFieldRequireSpecialHandling(tag);
	}
	
	private void writeRelevantIndicatorIds(final String objectName, Desire desire) throws Exception
	{
		getWriter().writeReflist(objectName, RELEVANT_INDICATOR_IDS, INDICATOR, desire.getRelevantIndicatorRefList());
	}
	
	private void writeRelevantStrategyIds(final String objectName, Desire desire) throws Exception
	{
		ORefList relevantStrategyRefs = desire.getRelevantStrategyAndActivityRefs().getFilteredBy(StrategySchema.getObjectType());
		getWriter().writeReflist(objectName, RELEVANT_STRATEGY_IDS, STRATEGY, relevantStrategyRefs);
	}
	
	private void writeRelevantActivityIds(final String objectName, Desire desire) throws Exception
	{
		ORefList relevantActivityRefs = desire.getRelevantStrategyAndActivityRefs().getFilteredBy(TaskSchema.getObjectType());
		getWriter().writeReflist(objectName, XmpzXmlConstants.RELEVANT_ACTIVITY_IDS, XmpzXmlConstants.ACTIVITY, relevantActivityRefs);
	}
}
