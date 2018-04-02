/*
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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

package org.miradi.dialogs.threatrating.properties;

import org.miradi.actions.Actions;
import org.miradi.dialogfields.ObjectDataField;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.objectdata.BooleanData;
import org.miradi.objects.AbstractThreatRatingData;
import org.miradi.project.Project;

public class ThreatRatingDataNotApplicableSubPanel extends AbstractThreatRatingDataSubPanel
{
    public ThreatRatingDataNotApplicableSubPanel(Project projectToUse, Actions actions) throws Exception
    {
        super(projectToUse, actions);
    }

    @Override
    protected void addFields(int threatRatingDataObjectType) throws Exception
    {
        notApplicableField = createCheckBoxField(threatRatingDataObjectType, AbstractThreatRatingData.TAG_IS_THREAT_RATING_NOT_APPLICABLE, BooleanData.BOOLEAN_TRUE, BooleanData.BOOLEAN_FALSE);
        addFieldWithPopUpInformation((ObjectDataInputField) notApplicableField, "NotApplicableThreatRatingFieldDescription.html");
    }

    @Override
    public String getPanelDescription()
    {
        return "ThreatRatingDataNotApplicableSubPanel";
    }

    private ObjectDataField notApplicableField;
}
