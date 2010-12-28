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
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Factor;

public abstract class FactorPoolExporter extends BaseObjectPoolExporter
{
	public FactorPoolExporter(WcsXmlExporter wcsXmlExporterToUse, String containerNameToUse, int objectTypeToUse)
	{
		super(wcsXmlExporterToUse, containerNameToUse, objectTypeToUse);
	}

	@Override
	protected void exportFields(UnicodeWriter writer, BaseObject baseObject) throws Exception
	{
		super.exportFields(writer, baseObject);
		
		writeOptionalElementWithSameTag(baseObject, Factor.TAG_SHORT_LABEL);
		writeOptionalElementWithSameTag(baseObject, getDetailsTag());
		writeOptionalElementWithSameTag(baseObject, Factor.TAG_COMMENTS);
	}

	protected String getDetailsTag()
	{
		return Factor.TAG_TEXT;
	}
	
	protected void writeIndicatorIds(Factor factor) throws Exception
	{
		writeOptionalIndicatorIds(factor.getDirectOrIndirectIndicatorRefs());
	}

	protected void writeObjectiveIds(Factor factor) throws Exception
	{
		writeOptionalIds("ObjectiveIds", XmpzXmlConstants.OBJECTIVE, factor.getObjectiveRefs());
	}
	
	protected DiagramFactor getCoveringDiagramFactor(BaseObject baseObject)
	{
		ORefList diagramFactorReferrers = baseObject.findObjectsThatReferToUs(DiagramFactor.getObjectType());
		if(diagramFactorReferrers.size() != 1)
			throw new RuntimeException(baseObject.getTypeName() + " object does not have a diagram factor.");
		
		return DiagramFactor.find(getProject(), diagramFactorReferrers.get(0));
	}
}
