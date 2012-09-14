/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.xslTemplate;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.main.Miradi;
import org.miradi.objects.XslTemplate;
import org.miradi.project.Project;
import org.miradi.schemas.XslTemplateSchema;

public class XslTemplatePropertiesPanel extends ObjectDataInputPanel
{
	public XslTemplatePropertiesPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, XslTemplateSchema.getObjectType());
			
		addField(createExpandableField(XslTemplate.TAG_LABEL));
		addField(createShortStringField(XslTemplate.TAG_FILE_EXTENSION));
		if (Miradi.isAlphaTesterMode())
			addField(createMultilineField(XslTemplate.TAG_TEMPLATE_CONTENTS));
		
		updateFieldsFromProject();
	}
	
	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|XSL Template Properties");
	}
}