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
import org.miradi.objects.Desire;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;

abstract public class DesirePoolExporter extends BaseObjectPoolExporter
{
	public DesirePoolExporter(XmpzXmlExporter wcsXmlExporterToUse, String containerNameToUse, int objectTypeToUse)
	{
		super(wcsXmlExporterToUse, containerNameToUse, objectTypeToUse);
	}
	
	@Override
	protected void exportFields(UnicodeWriter writer, BaseObject baseObject) throws Exception
	{
		super.exportFields(writer, baseObject);
		
		Desire desire = (Desire) baseObject;
		writeOptionalElementWithSameTag(baseObject, Desire.TAG_SHORT_LABEL);
		writeOptionalElementWithSameTag(baseObject, Desire.TAG_FULL_TEXT);
		writeRelevantIndicatorIds(desire);
		writeRelevantStrategyIds(desire);
		writeRelevantActivityIds(desire);
		writeProgressPercetIds(desire.getProgressPercentRefs());
		writeOptionalElementWithSameTag(baseObject, Desire.TAG_COMMENTS);
	}
	
	protected void writeRelevantIndicatorIds(Desire desire) throws Exception
	{
		writeOptionalIndicatorIds(XmpzXmlConstants.RELEVANT_INDICATOR_IDS, desire.getRelevantIndicatorRefList());
	}
	
	protected void writeRelevantStrategyIds(Desire desire) throws Exception
	{
		ORefList relevantStrategyRefs = desire.getRelevantStrategyAndActivityRefs().getFilteredBy(Strategy.getObjectType());
		writeOptionalIds(XmpzXmlConstants.RELEVANT_STRATEGY_IDS, XmpzXmlConstants.STRATEGY, relevantStrategyRefs);
	}
	
	protected void writeRelevantActivityIds(Desire desire) throws Exception
	{
		ORefList relevantActivityRefs = desire.getRelevantStrategyAndActivityRefs().getFilteredBy(Task.getObjectType());
		writeOptionalIds(XmpzXmlConstants.RELEVANT_ACTIVITY_IDS, XmpzXmlConstants.ACTIVITY, relevantActivityRefs);
	}
}
