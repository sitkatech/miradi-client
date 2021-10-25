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
package org.miradi.dialogs.diagram;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objects.AbstractTarget;
import org.miradi.objects.Factor;
import org.miradi.project.Project;
import org.miradi.questions.StaticQuestionManager;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.ViabilityModeQuestion;
import org.miradi.schemas.BaseObjectSchema;

public class TargetStatusPanel extends ObjectDataInputPanel
{
    public TargetStatusPanel(Project project, Factor factor, BaseObjectSchema factorSchema) throws Exception
    {
        super(project, factorSchema.getType());

        boolean isKeaViabilityMode = (AbstractTarget.isAbstractTarget(factor) && factor.getData(AbstractTarget.TAG_VIABILITY_MODE).equals(ViabilityModeQuestion.TNC_STYLE_CODE));

        if (isKeaViabilityMode)
            addField(createReadOnlyChoiceField(factorSchema.getType(), AbstractTarget.PSEUDO_TAG_TARGET_VIABILITY, StaticQuestionManager.getQuestion(StatusQuestion.class)));
        else
            addField(createRatingChoiceField(factorSchema.getType(), AbstractTarget.TAG_TARGET_STATUS, StaticQuestionManager.getQuestion(StatusQuestion.class)));

        addField(createMultilineField(AbstractTarget.TAG_CURRENT_STATUS_JUSTIFICATION));

        if (isKeaViabilityMode)
            addField(createReadOnlyChoiceField(factorSchema.getType(), AbstractTarget.PSEUDO_TAG_TARGET_FUTURE_VIABILITY, StaticQuestionManager.getQuestion(StatusQuestion.class)));
        else
            addField(createRatingChoiceField(factorSchema.getType(), AbstractTarget.TAG_TARGET_FUTURE_STATUS, StaticQuestionManager.getQuestion(StatusQuestion.class)));

        addField(createMultilineField(AbstractTarget.TAG_FUTURE_STATUS_JUSTIFICATION));
    }

    @Override
    public String getPanelDescription()
    {
        return EAM.text("Status");
    }
}
