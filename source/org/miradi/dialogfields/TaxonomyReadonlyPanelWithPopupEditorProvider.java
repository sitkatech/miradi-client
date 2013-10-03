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

import org.miradi.dialogfields.editors.SplitterPanelWithStaticRightSideTextPanel;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.TaxonomyHelper;
import org.miradi.objects.BaseObject;
import org.miradi.objects.TaxonomyAssociation;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;

public class TaxonomyReadonlyPanelWithPopupEditorProvider implements ReadonlyPanelAndPopupEditorProvider
{
	public TaxonomyReadonlyPanelWithPopupEditorProvider(Project projectToUse, ORef refToUse, ChoiceQuestion questionToUse, String taxonomyAssociationCodeToUse)
	{
		project = projectToUse;
		ref = refToUse;
		question = questionToUse;
		taxonomyAssociationCode = taxonomyAssociationCodeToUse;
		taxonomyAssociation = TaxonomyHelper.findTaxonomyAssociation(getProject(), taxonomyAssociationCodeToUse);
	}
	
	public AbstractReadonlyChoiceComponent createReadOnlyComponent(ChoiceQuestion questionToUse, int columnCount)
	{
		return new ReadonlyTaxonomyMultiChoiceComponent(getProject(), question, taxonomyAssociationCode);
	}

	public DisposablePanel createEditorPanel() throws Exception
	{
		AbstractEditorComponentWithHiearchies taxonomyLeftSideEditorComponent = createTaxonomyEditorComponent(question);
		TaxonomyOneFieldObjectDataInputPanelWithListenerDelegator leftPanel = new TaxonomyOneFieldObjectDataInputPanelWithListenerDelegator(getProject(), getRef(), BaseObject.TAG_TAXONOMY_CLASSIFICATION_CONTAINER, taxonomyAssociation, taxonomyLeftSideEditorComponent);
		
		//FIXME urgent : only display splitter if right side description exists
		return new SplitterPanelWithStaticRightSideTextPanel(EAM.getMainWindow(), leftPanel);
	}
	
	private AbstractEditorComponentWithHiearchies createTaxonomyEditorComponent(ChoiceQuestion questionToUse)
	{
		if (getTaxonomyAssociation().isMultiSelectionTaxonomy())
			return new MultiSelectionEditorComponentWithHierarchies(questionToUse);
		
		return new SingleSelectionEditorComponentWithHierarchies(questionToUse);
	}

	private Project getProject()
	{
		return project;
	}
	
	private TaxonomyAssociation getTaxonomyAssociation()
	{
		return taxonomyAssociation;
	}
	
	private ORef getRef()
	{
		return ref;
	}
	
	private ORef ref; 
	private ChoiceQuestion question; 
	private String taxonomyAssociationCode;
	private TaxonomyAssociation taxonomyAssociation;
	private Project project;
}