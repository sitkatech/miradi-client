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
import org.miradi.objects.AbstractTarget;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Target;

public class BiodiversityTargetContainerExporter extends AbstractTargetContainerExporter
{
	public BiodiversityTargetContainerExporter(WcsXmlExporter wcsXmlExporterToUse)
	{
		super(wcsXmlExporterToUse, BIODIVERSITY_TARGET, Target.getObjectType());
	}
	
	@Override
	protected void exportFields(UnicodeWriter writer, BaseObject baseObject) throws Exception
	{
		super.exportFields(writer, baseObject);
		
		Target target = (Target) baseObject;
		writeOptionalElementWithSameTag(baseObject, Target.TAG_LABEL);					
		writeOptionalElementWithSameTag(baseObject, Target.TAG_SHORT_LABEL);
		writeOptionalElementWithSameTag(baseObject, Target.TAG_TEXT);
		writeOptionalElementWithSameTag(baseObject, Target.TAG_COMMENTS);
		writeElementWithSameTag(baseObject, AbstractTarget.TAG_TARGET_STATUS);
		writeElementWithSameTag(baseObject, Target.TAG_VIABILITY_MODE);
		writeOptionalElementWithSameTag(baseObject, Target.TAG_CURRENT_STATUS_JUSTIFICATION);
		writeIds("SubTargetIds", "SubTargetId", target.getSubTargetRefs());
		writeIds("GoalIds", "GoalId", target.getGoalRefs());
		writeIds("KeyEcologicalAttributeIds", "KeyEcologicalAttributeId", target.getKeyEcologicalAttributeRefs());
		writeIds("IndicatorIds", "IndicatorId", target.getDirectOrIndirectIndicatorRefs());
		writeIds("StressIds", "StressId", target.getStressRefs());
		writeCodeListElement("HabitatAssociation", baseObject, Target.TAG_HABITAT_ASSOCIATION);
		writeOptionalElementWithSameTag(baseObject, Target.TAG_SPECIES_LATIN_NAME);
	}
}
