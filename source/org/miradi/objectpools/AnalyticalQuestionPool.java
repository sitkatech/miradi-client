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
package org.miradi.objectpools;

import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.AnalyticalQuestion;
import org.miradi.objects.BaseObject;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.BaseObjectSchema;

public class AnalyticalQuestionPool extends FactorPool
{
    public AnalyticalQuestionPool(IdAssigner idAssignerToUse)
    {
        super(idAssignerToUse, ObjectType.ANALYTICAL_QUESTION);
    }

    public void put(AnalyticalQuestion analyticalQuestion) throws Exception
    {
        put(analyticalQuestion.getId(), analyticalQuestion);
    }

    public AnalyticalQuestion find(BaseId id)
    {
        return (AnalyticalQuestion)getRawObject(id);
    }

    @Override
    BaseObject createRawObject(ObjectManager objectManager, BaseId actualId) throws Exception
    {
        return new AnalyticalQuestion(objectManager ,new FactorId(actualId.asInt()));
    }

    @Override
    public BaseObjectSchema createBaseObjectSchema(Project projectToUse)
    {
        return AnalyticalQuestion.createSchema(projectToUse);
    }
}
