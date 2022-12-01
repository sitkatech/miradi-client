/*
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.xml.xmpz2.objectImporters;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.objects.AbstractAssumption;
import org.miradi.project.Project;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;

abstract public class AbstractAssumptionImporter extends BaseObjectImporter
{
    public AbstractAssumptionImporter(Xmpz2XmlImporter importerToUse, BaseObjectSchema baseObjectSchemaToUse)
    {
        super(importerToUse, baseObjectSchemaToUse);
    }

	@Override
	public void importFields(Node baseObjectNode, ORef destinationRef) throws Exception
	{
		super.importFields(baseObjectNode, destinationRef);
		importRelevantIndicatorIds(baseObjectNode, destinationRef);
	}

	@Override
	protected boolean isCustomImportField(String tag)
	{
		if (tag.equals(AbstractAssumption.TAG_INDICATOR_IDS))
			return true;

		return super.isCustomImportField(tag);
	}

	private void importRelevantIndicatorIds(Node node, ORef destinationRef) throws Exception
	{
		ORefList importedRelevantRefs = getImporter().extractRefs(node, getXmpz2ElementName(), RELEVANT_INDICATOR_IDS, INDICATOR);
		AbstractAssumption assumptionOrSubAssumption = findAssumptionOrSubAssumption(getProject(), destinationRef);
		RelevancyOverrideSet set = assumptionOrSubAssumption.getCalculatedRelevantIndicatorOverrides(importedRelevantRefs);
		getImporter().setData(destinationRef, AbstractAssumption.TAG_INDICATOR_IDS, set.toString());
	}

	abstract AbstractAssumption findAssumptionOrSubAssumption(Project project, ORef destinationRef);
}
