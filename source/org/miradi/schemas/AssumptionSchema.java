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

package org.miradi.schemas;

import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Assumption;
import org.miradi.questions.AssumptionEvidenceConfidenceQuestion;

public class AssumptionSchema extends EvidenceFactorSchema
{
    public AssumptionSchema()
    {
        super();
    }

    @Override
    protected void fillFieldSchemas()
    {
        super.fillFieldSchemas();

        createFieldSchemaChoice(Assumption.TAG_EVIDENCE_CONFIDENCE, AssumptionEvidenceConfidenceQuestion.class);
        createOwnedFieldSchemaIdList(Assumption.TAG_SUB_ASSUMPTION_IDS, AssumptionSchema.getObjectType());
		createOwnedFieldSchemaIdList(Assumption.TAG_DIAGRAM_FACTOR_IDS, DiagramFactorSchema.getObjectType());
        
        createTaxonomyClassificationSchemaField();
    }

    @Override
    protected void addDetailsField()
    {
        createFieldSchemaMultiLineUserText(Assumption.TAG_DETAILS);
    }

    public static int getObjectType()
    {
        return ObjectType.ASSUMPTION;
    }

    @Override
    public int getType()
    {
        return getObjectType();
    }

    @Override
    public String getObjectName()
    {
        return OBJECT_NAME;
    }

    public static final String OBJECT_NAME = "Assumption";
}
