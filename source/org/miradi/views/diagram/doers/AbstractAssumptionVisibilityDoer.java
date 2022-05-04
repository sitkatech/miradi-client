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
package org.miradi.views.diagram.doers;

import org.miradi.objecthelpers.ORef;
import org.miradi.objects.DiagramObject;
import org.miradi.questions.DiagramFactorFontColorQuestion;
import org.miradi.schemas.AnalyticalQuestionSchema;
import org.miradi.schemas.AssumptionSchema;


abstract public class AbstractAssumptionVisibilityDoer extends AbstractVisibilityDoer
{
    @Override
    public boolean isAvailable()
    {
        boolean superIsAvailable = super.isAvailable();
        if (!superIsAvailable)
            return false;

        DiagramObject currentDiagramObject = getDiagramView().getDiagramModel().getDiagramObject();

        ORef selectedAssumptionRef = getSelectedAssumptionRef();
        if (selectedAssumptionRef.isInvalid())
            return false;

        return isAvailable(selectedAssumptionRef);
    }

    @Override
	protected String getDiagramFactorDefaultFontColor()
	{
		return DiagramFactorFontColorQuestion.WHITE_HEX;
	}

    protected ORef getSelectedAssumptionRef()
    {
        return getSelectedRefOfType(AssumptionSchema.getObjectType());
    }

    protected ORef getSelectedAnalyticalQuestionRef()
    {
        return getSelectedRefOfType(AnalyticalQuestionSchema.getObjectType());
    }
}
