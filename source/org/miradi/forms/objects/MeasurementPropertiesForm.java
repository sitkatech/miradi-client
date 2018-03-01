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
package org.miradi.forms.objects;

import org.miradi.forms.FieldPanelSpec;
import org.miradi.icons.MeasurementIcon;
import org.miradi.main.EAM;
import org.miradi.objects.Measurement;
import org.miradi.schemas.MeasurementSchema;

public class MeasurementPropertiesForm extends FieldPanelSpec
{
	public MeasurementPropertiesForm()
	{
		int type = MeasurementSchema.getObjectType();
		addStandardNameRow(new MeasurementIcon(), EAM.text("Measurement"), MeasurementSchema.getObjectType(), new String[]{Measurement.TAG_DATE, Measurement.TAG_SUMMARY});
		addLabelAndField(type, Measurement.TAG_DETAIL);
		
		addLabelAndFieldsWithLabels(EAM.text("Current Status"), MeasurementSchema.getObjectType(), new String[]{Measurement.TAG_STATUS, Measurement.TAG_TREND, });
		addLabelAndField(type, Measurement.TAG_EVIDENCE_CONFIDENCE);
		addLabelAndField(type, Measurement.TAG_COMMENTS);
		addLabelAndField(type, Measurement.TAG_EVIDENCE_CONFIDENCE);
	}
}
