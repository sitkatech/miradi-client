/*
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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
import org.miradi.objects.Output;
import org.miradi.schemas.OutputSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;

public class OutputImporter extends BaseObjectImporter
{
    public OutputImporter(Xmpz2XmlImporter importerToUse)
    {
        super(importerToUse, new OutputSchema());
    }

    @Override
    public void importFields(Node baseObjectNode, ORef destinationRef) throws Exception
    {
        super.importFields(baseObjectNode, destinationRef);
        importRelevantGoalIds(baseObjectNode, destinationRef);
        importRelevantObjectiveIds(baseObjectNode, destinationRef);
        importRelevantIndicatorIds(baseObjectNode, destinationRef);
    }

    @Override
    protected boolean isCustomImportField(String tag)
    {
        if (tag.equals(Output.TAG_GOAL_IDS))
            return true;

        if (tag.equals(Output.TAG_OBJECTIVE_IDS))
            return true;

        if (tag.equals(Output.TAG_INDICATOR_IDS))
            return true;

        return super.isCustomImportField(tag);
    }

    private void importRelevantGoalIds(Node node, ORef destinationRef) throws Exception
    {
        ORefList importedRelevantRefs = getImporter().extractRefs(node, getXmpz2ElementName(), RELEVANT_GOAL_IDS, GOAL);
        Output output = Output.find(getProject(), destinationRef);
        RelevancyOverrideSet set = output.getCalculatedRelevantGoalOverrides(importedRelevantRefs);
        getImporter().setData(destinationRef, Output.TAG_GOAL_IDS, set.toString());
    }

    private void importRelevantObjectiveIds(Node node, ORef destinationRef) throws Exception
    {
        ORefList importedRelevantRefs = getImporter().extractRefs(node, getXmpz2ElementName(), RELEVANT_OBJECTIVE_IDS, OBJECTIVE);
        Output output = Output.find(getProject(), destinationRef);
        RelevancyOverrideSet set = output.getCalculatedRelevantObjectiveOverrides(importedRelevantRefs);
        getImporter().setData(destinationRef, Output.TAG_OBJECTIVE_IDS, set.toString());
    }

    private void importRelevantIndicatorIds(Node node, ORef destinationRef) throws Exception
    {
        ORefList importedRelevantRefs = getImporter().extractRefs(node, getXmpz2ElementName(), RELEVANT_INDICATOR_IDS, INDICATOR);
        Output output = Output.find(getProject(), destinationRef);
        RelevancyOverrideSet set = output.getCalculatedRelevantIndicatorOverrides(importedRelevantRefs);
        getImporter().setData(destinationRef, Output.TAG_INDICATOR_IDS, set.toString());
    }
}
