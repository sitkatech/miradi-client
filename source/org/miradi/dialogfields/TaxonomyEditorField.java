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

import javax.swing.JComponent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.TaxonomyClassificationMap;
import org.miradi.objecthelpers.TaxonomyHelper;
import org.miradi.objects.BaseObject;
import org.miradi.objects.TaxonomyAssociation;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;

public class TaxonomyEditorField extends ObjectDataInputField implements ListSelectionListener
{
	public TaxonomyEditorField(Project projectToUse, ORef refToUse, String tagToUse, ChoiceQuestion questionToUse, String taxonomyAssociationCodeToUse) throws Exception
	{
		super(projectToUse, refToUse, tagToUse);
		
		taxonomyAssociation = TaxonomyHelper.findTaxonomyAssociation(getProject(), taxonomyAssociationCodeToUse);
		createEditorComponent(questionToUse);
		taxonomyLeftSideEditorComponent.addListSelectionListener(this);
	}
	
	public void becomeActive()
	{
		taxonomyLeftSideEditorComponent.becomeActive();
	}
	
	public void becomeInactive()
	{
		taxonomyLeftSideEditorComponent.becomeInactive();
	}

	private void createEditorComponent(ChoiceQuestion questionToUse) throws Exception
	{
		taxonomyLeftSideEditorComponent = createTaxonomyEditorComponent(questionToUse);
	}
	
	private AbstractEditorComponentWithHiearchies createTaxonomyEditorComponent(ChoiceQuestion questionToUse)
	{
		if (getTaxonomyAssociation().isMultiSelectionTaxonomy())
			return new MultiSelectionEditorComponentWithHierarchies(questionToUse);
		
		return new SingleSelectionEditorComponentWithHierarchies(questionToUse);
	}

	@Override
	public String getText()
	{
		try
		{
			BaseObject baseObject = BaseObject.find(getProject(), getORef());
			TaxonomyClassificationMap taxonomyClassificationList = new TaxonomyClassificationMap(baseObject.getData(BaseObject.TAG_TAXONOMY_CLASSIFICATION_CONTAINER));
			CodeList selectedTaxonomyElementCodes = new CodeList(taxonomyLeftSideEditorComponent.getText());
			taxonomyClassificationList.putCodeList(getTaxonomyAssociation().getTaxonomyCode(), selectedTaxonomyElementCodes);

			return taxonomyClassificationList.toJsonString();
		}
		catch (Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
			return "";
		}
	}

	@Override
	public void setText(String newValue)
	{
		try
		{
			CodeList taxonomyElementCodes = TaxonomyClassificationMap.getTaxonomyElementCodes(getProject(), newValue, getTaxonomyAssociation().getTaxonomyAssociationCode());
			taxonomyLeftSideEditorComponent.setText(taxonomyElementCodes.toString());
		}
		catch (Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
		}
	}

	@Override
	public JComponent getComponent()
	{
		return taxonomyLeftSideEditorComponent;
	}
	
	@Override
	protected boolean shouldBeEditable()
	{
		return isValidObject();
	}
	
	public void valueChanged(ListSelectionEvent arg0)
	{
		forceSave();
	}
	
	private TaxonomyAssociation getTaxonomyAssociation()
	{
		return taxonomyAssociation;
	}
	
	private TaxonomyAssociation taxonomyAssociation;
	private AbstractEditorComponentWithHiearchies taxonomyLeftSideEditorComponent;
}
