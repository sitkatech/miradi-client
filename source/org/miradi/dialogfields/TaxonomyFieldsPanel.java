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
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Vector;

import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.dialogs.base.ReadonlyPanelWithPopupEditor;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.TwoColumnGridLayout;
import org.miradi.objecthelpers.BaseObjectByNameSorter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.TaxonomyHelper;
import org.miradi.objectpools.TaxonomyAssociationPool;
import org.miradi.objects.BaseObject;
import org.miradi.objects.MiradiShareTaxonomy;
import org.miradi.objects.TaxonomyAssociation;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.MiradiShareTaxonomyQuestion;

public class TaxonomyFieldsPanel extends MiradiPanel 
{
	public TaxonomyFieldsPanel(Project projectToUse)
	{
		super(new TwoColumnGridLayout());
		
		project = projectToUse;
		clearFieldsToLabelMap();
	}

	public void setText(String newValue)
	{
		Set<ReadonlyPanelWithPopupEditor> fields = fieldsToLabelMapForType.keySet();
		for(ReadonlyPanelWithPopupEditor field : fields)
		{
			field.setText(newValue);
		}
	}

	public void setObjectRef(ORef refToUse) throws Exception
	{
		clearFieldsToLabelMap();
		createFields(refToUse);
		addFields();
	}

	private void addFields()
	{
		removeAll();
		Set<ReadonlyPanelWithPopupEditor> fields = fieldsToLabelMapForType.keySet();
		for(ReadonlyPanelWithPopupEditor field : fields)
		{
			String label = fieldsToLabelMapForType.get(field);
			add(new PanelTitleLabel(label));
			add(field);
		}
	}

	private void createFields(ORef refToUse) throws Exception
	{
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
			
			TaxonomyReadonlyPanelWithPopupEditorProvider provider = new TaxonomyReadonlyPanelWithPopupEditorProvider(refToUse, miradiShareTaxonomyQuestion, taxonomyAssociationCode);
			ReadonlyPanelWithPopupEditor readonlyPanelPopupEditor = new ReadonlyPanelWithPopupEditor(provider, "", miradiShareTaxonomyQuestion);
			fieldsToLabelMapForType.put(readonlyPanelPopupEditor, taxonomyAssociation.getLabel());
		}
	}		
	
	private Project getProject()
	{
		return project;
	}
	
	private void clearFieldsToLabelMap()
	{
		fieldsToLabelMapForType = new LinkedHashMap<ReadonlyPanelWithPopupEditor, String>();
	}
	
	private class TaxonomyReadonlyPanelWithPopupEditorProvider implements ReadonlyPanelAndPopupEditorProvider
	{
		public TaxonomyReadonlyPanelWithPopupEditorProvider(ORef refToUse, ChoiceQuestion questionToUse, String taxonomyAssociationCodeToUse)
		{
			ref = refToUse;
			question = questionToUse;
			taxonomyAssociationCode = taxonomyAssociationCodeToUse;
		}
		
		public DisposablePanel createEditorPanel() throws Exception
		{
			return new TaxonomyEditorPanel(getProject(), ref, BaseObject.TAG_TAXONOMY_CLASSIFICATION_CONTAINER, question, taxonomyAssociationCode);
		}

		public AbstractReadonlyChoiceComponent createReadOnlyComponent(ChoiceQuestion questionToUse, int columnCount)
		{
			return new ReadonlyTaxonomyMultiChoiceComponent(getProject(), question, taxonomyAssociationCode);
		}
		
		private ORef ref; 
		private ChoiceQuestion question; 
		private String taxonomyAssociationCode;
	}

	private Project project;
	private LinkedHashMap<ReadonlyPanelWithPopupEditor, String> fieldsToLabelMapForType;
}
