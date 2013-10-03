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

package org.miradi.dialogfields;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;

//FIXME urgent - remove this class and its references, its no longer used
public class TaxonomyEditorPanel extends ObjectDataInputPanel
{
	public TaxonomyEditorPanel(Project projectToUse, ORef orefToUse, String tagToUse, ChoiceQuestion question, String taxonomyAssociationCodeToUse) throws Exception
	{
		super(projectToUse, orefToUse);
		
		taxonomyEditorField = createTaxonomyEditorField(orefToUse, tagToUse, question, taxonomyAssociationCodeToUse);
		addFieldWithoutLabel(taxonomyEditorField);
		
		updateFieldsFromProject();
	}
	
	@Override
	public void becomeActive()
	{
		super.becomeActive();

		taxonomyEditorField.becomeActive();
	}
	
	@Override
	public void becomeInactive()
	{
		super.becomeInactive();
		
		taxonomyEditorField.becomeInactive();
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Editor");
	}
	
	private TaxonomyEditorField taxonomyEditorField;
}
