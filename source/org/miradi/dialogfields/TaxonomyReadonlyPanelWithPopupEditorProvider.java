/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

package org.miradi.dialogfields;

import org.miradi.dialogfields.editors.SplitterPanelWithStaticRightSideTextPanel;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.AbstractTaxonomyAssociation;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;

public class TaxonomyReadonlyPanelWithPopupEditorProvider implements ReadonlyPanelAndPopupEditorProvider
{
	public TaxonomyReadonlyPanelWithPopupEditorProvider(Project projectToUse, ORef refToUse, ChoiceQuestion questionToUse, String taxonomyAssociationCodeToUse, String tagToUse, AbstractTaxonomyAssociation taxonomyAssociationToUse)
	{
		project = projectToUse;
		ref = refToUse;
		question = questionToUse;
		taxonomyAssociationCode = taxonomyAssociationCodeToUse;
		tag = tagToUse;
		taxonomyAssociation = taxonomyAssociationToUse;
	}
	
	public AbstractReadonlyChoiceComponent createReadOnlyComponent(ChoiceQuestion questionToUse, int columnCount)
	{
		return new ReadonlyTaxonomyMultiChoiceComponent(getProject(), question, taxonomyAssociationCode);
	}

	public DisposablePanel createEditorPanel() throws Exception
	{
		AbstractEditorComponentWithHierarchies taxonomyLeftSideEditorComponent = createTaxonomyEditorComponent(question);
		TaxonomyOneFieldObjectDataInputPanelWithListenerDelegator leftPanel = new TaxonomyOneFieldObjectDataInputPanelWithListenerDelegator(getProject(), getRef(), tag, taxonomyAssociation, taxonomyLeftSideEditorComponent);
		
		//FIXME urgent : only display splitter if right side description exists
		return new SplitterPanelWithStaticRightSideTextPanel(EAM.getMainWindow(), leftPanel);
	}
	
	private AbstractEditorComponentWithHierarchies createTaxonomyEditorComponent(ChoiceQuestion questionToUse)
	{
		if (getTaxonomyAssociation().isMultiSelectionTaxonomy())
			return new MultiSelectionEditorComponentWithHierarchies(questionToUse);
		
		return new SingleSelectionEditorComponentWithHierarchies(questionToUse);
	}

	private Project getProject()
	{
		return project;
	}
	
	private AbstractTaxonomyAssociation getTaxonomyAssociation()
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
	private AbstractTaxonomyAssociation taxonomyAssociation;
	private String tag;
	private Project project;
}