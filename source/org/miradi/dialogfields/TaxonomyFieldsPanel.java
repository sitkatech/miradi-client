/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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

import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.dialogs.base.ReadonlyPanelWithPopupEditor;
import org.miradi.dialogs.base.ReadonlyPanelWithPopupEditorWithoutMainScrollPane;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.AppPreferences;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.TaxonomyHelper;
import org.miradi.objectpools.AbstractTaxonomyAssociationPool;
import org.miradi.objects.AbstractTaxonomyAssociation;
import org.miradi.objects.BaseObject;
import org.miradi.objects.MiradiShareTaxonomy;
import org.miradi.project.Project;
import org.miradi.questions.MiradiShareTaxonomyQuestion;
import org.miradi.utils.FillerLabel;

import java.awt.*;
import java.util.Collections;
import java.util.Vector;

public class TaxonomyFieldsPanel extends MiradiPanel 
{
	public TaxonomyFieldsPanel(Project projectToUse, String tagToUse, AbstractTaxonomyAssociationPool taxonomyAssociationPoolToUse)
	{
		super(new OneColumnGridLayout());
		
		project = projectToUse;
		tag = tagToUse;
		taxonomyAssociationPool = taxonomyAssociationPoolToUse;
		clearFieldsToLabelMap();
	}

	public void setText(String newValue)
	{
		for(ReadonlyPanelWithPopupEditor field : taxonomyReadonlyWithPopupEditorPanels)
		{
			field.setText(newValue);
		}
	}

	public void setObjectRef(ORef refToUse) throws Exception
	{
		clearFieldsToLabelMap();
		rebuildFields(refToUse);
	}

	private void rebuildFields(ORef refToUse) throws Exception
	{
		removeAll();
		if (refToUse.isInvalid())
			return;
		
		BaseObject baseObject = BaseObject.find(getProject(), refToUse);
		Vector<AbstractTaxonomyAssociation> sortedTaxonomyAssociationsForType = taxonomyAssociationPool.findTaxonomyAssociationsForBaseObject(baseObject);
		Collections.sort(sortedTaxonomyAssociationsForType, taxonomyAssociationPool.getSorter());
		for(AbstractTaxonomyAssociation taxonomyAssociation : sortedTaxonomyAssociationsForType)
		{
			MiradiShareTaxonomy miradiShareTaxonomy = TaxonomyHelper.getTaxonomyElementList(taxonomyAssociation);
			final MiradiShareTaxonomyQuestion miradiShareTaxonomyQuestion = getProject().getMiradiShareTaxonomyQuestionCache().getMiradiShareTaxonomyQuestion(miradiShareTaxonomy, taxonomyAssociation);
			final String taxonomyAssociationCode = taxonomyAssociation.getTaxonomyAssociationCode();
			
			TaxonomyReadonlyPanelWithPopupEditorProvider provider = new TaxonomyReadonlyPanelWithPopupEditorProvider(getProject(), refToUse, miradiShareTaxonomyQuestion, taxonomyAssociationCode, tag, taxonomyAssociation);
			ReadonlyPanelWithPopupEditor readonlyPanelPopupEditor = new ReadonlyPanelWithPopupEditorWithoutMainScrollPane(provider, taxonomyAssociation.getLabel(), miradiShareTaxonomyQuestion);
			taxonomyReadonlyWithPopupEditorPanels.add(readonlyPanelPopupEditor);
			
			add(new PanelTitleLabel(taxonomyAssociation.getLabel()));
			add(readonlyPanelPopupEditor);
			add(new FillerLabel());
		}

		validate();
		repaint();
	}		
	
	@Override
	public Color getBackground() 
	{
		return AppPreferences.getDataPanelBackgroundColor(); 
	}	
	
	Project getProject()
	{
		return project;
	}
	
	private void clearFieldsToLabelMap()
	{
		taxonomyReadonlyWithPopupEditorPanels = new Vector<ReadonlyPanelWithPopupEditor>();
	}
	
	private Project project;
	private String tag;
	private AbstractTaxonomyAssociationPool taxonomyAssociationPool;
	private Vector<ReadonlyPanelWithPopupEditor> taxonomyReadonlyWithPopupEditorPanels;
}
