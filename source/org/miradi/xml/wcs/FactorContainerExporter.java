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

public abstract class FactorContainerExporter extends BaseObjectContainerExporter
{
	public FactorContainerExporter(WcsXmlExporter wcsXmlExporterToUse, String containerNameToUse, int objectTypeToUse)
	{
		super(wcsXmlExporterToUse, containerNameToUse, objectTypeToUse);
	}

	protected void exportFields(UnicodeWriter writer, BaseObject baseObject) throws Exception
	{
		super.exportFields(writer, baseObject);
		
		writeOptionalElementWithSameTag(baseObject, Factor.TAG_SHORT_LABEL);
		writeOptionalElementWithSameTag(baseObject, Factor.TAG_TEXT);
		writeOptionalElementWithSameTag(baseObject, Factor.TAG_COMMENTS);
	}
	
	protected void writeIndicatorIds(Factor factor) throws Exception
	{
		writeIndicatorIds(factor.getDirectOrIndirectIndicatorRefs());
	}

	protected void writeObjectiveIds(Factor factor) throws Exception
	{
		writeIds("ObjectiveIds", WcsXmlConstants.OBJECTIVE, factor.getObjectiveRefs());
	}
	
	protected DiagramFactor getCoveringDiagramFactor(BaseObject baseObject)
	{
		ORefList diagramFactorReferrers = baseObject.findObjectsThatReferToUs(DiagramFactor.getObjectType());
		if(diagramFactorReferrers.size() != 1)
			throw new RuntimeException(baseObject.getTypeName() + " object does not have a diagram factor.");
		
		return DiagramFactor.find(getProject(), diagramFactorReferrers.get(0));
	}
}
