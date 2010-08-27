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

package org.miradi.forms.objects;

import org.miradi.forms.FieldPanelSpec;
import org.miradi.icons.AbstractMiradiIcon;
import org.miradi.objects.AbstractTarget;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.ViabilityModeQuestion;

abstract public class AbstractTargetPropertiesForm extends FieldPanelSpec
{
	public AbstractTargetPropertiesForm()
	{
		int targetType = getTargetType();
		addStandardNameRow(createTargetIcon(), getTargetLabel(), targetType, new String[]{AbstractTarget.TAG_SHORT_LABEL, AbstractTarget.TAG_LABEL});
		addChoiceField(targetType, AbstractTarget.TAG_VIABILITY_MODE, new ViabilityModeQuestion());
		addChoiceField(targetType, AbstractTarget.TAG_TARGET_STATUS, new StatusQuestion());
		addLabelAndField(targetType, AbstractTarget.TAG_CURRENT_STATUS_JUSTIFICATION);
		addCustomFields();
		addLabelAndField(getTargetType(), AbstractTarget.TAG_TEXT);
		addLabelAndField(getTargetType(), AbstractTarget.TAG_COMMENTS);
	}

	abstract protected  void addCustomFields();
	
	abstract protected String getTargetLabel();

	abstract protected AbstractMiradiIcon createTargetIcon();

	abstract protected int getTargetType();
}
