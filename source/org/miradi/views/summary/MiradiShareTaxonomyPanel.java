/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.views.summary;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.forms.objects.MiradiShareTaxonomyDataForm;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;

public class MiradiShareTaxonomyPanel extends ObjectDataInputPanel
{
	public MiradiShareTaxonomyPanel(Project projectToUse, ORef[] orefsToUse) throws Exception
	{
		super(projectToUse, orefsToUse, new MiradiShareTaxonomyDataForm());
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Basics");
	}
}
