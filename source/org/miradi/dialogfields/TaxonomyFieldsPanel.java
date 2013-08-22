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

import java.util.Collections;
import java.util.Vector;

import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.dialogs.base.ReadonlyPanelWithPopupEditor;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.objecthelpers.BaseObjectByNameSorter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.TaxonomyHelper;
import org.miradi.objectpools.TaxonomyAssociationPool;
import org.miradi.objects.BaseObject;
import org.miradi.objects.MiradiShareTaxonomy;
import org.miradi.objects.TaxonomyAssociation;
import org.miradi.project.Project;
import org.miradi.questions.MiradiShareTaxonomyQuestion;
import org.miradi.utils.FillerLabel;

public class TaxonomyFieldsPanel extends MiradiPanel 
{
	public TaxonomyFieldsPanel(Project projectToUse)
	{
		super(new OneColumnGridLayout());
		
		project = projectToUse;
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
		TaxonomyAssociationPool taxonomyAssociationPool = getProject().getTaxonomyAssociationPool();
		Vector<TaxonomyAssociation> sortedTaxonomyAssociationsForType = taxonomyAssociationPool.findTaxonomyAssociationsForBaseObject(baseObject);
		Collections.sort(sortedTaxonomyAssociationsForType, new BaseObjectByNameSorter());
		for(TaxonomyAssociation taxonomyAssociation : sortedTaxonomyAssociationsForType)
		{
			MiradiShareTaxonomy miradiShareTaxonomy = TaxonomyHelper.getTaxonomyElementList(taxonomyAssociation);
			final MiradiShareTaxonomyQuestion miradiShareTaxonomyQuestion = new MiradiShareTaxonomyQuestion(miradiShareTaxonomy, taxonomyAssociation);
			final String taxonomyAssociationCode = taxonomyAssociation.getTaxonomyAssociationCode();
			
			TaxonomyReadonlyPanelWithPopupEditorProvider provider = new TaxonomyReadonlyPanelWithPopupEditorProvider(getProject(), refToUse, miradiShareTaxonomyQuestion, taxonomyAssociationCode);
			ReadonlyPanelWithPopupEditor readonlyPanelPopupEditor = new ReadonlyPanelWithPopupEditor(provider, taxonomyAssociation.getLabel(), miradiShareTaxonomyQuestion);
			taxonomyReadonlyWithPopupEditorPanels.add(readonlyPanelPopupEditor);
			
			add(new PanelTitleLabel(taxonomyAssociation.getLabel()));
			add(readonlyPanelPopupEditor);
			add(new FillerLabel());
		}
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
	private Vector<ReadonlyPanelWithPopupEditor> taxonomyReadonlyWithPopupEditorPanels;
}
