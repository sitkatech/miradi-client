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

package org.miradi.dialogs.tablerenderers;

import java.awt.Component;

import javax.swing.JTable;

import org.miradi.dialogs.threatrating.properties.ThreatStressRatingTable;
import org.miradi.dialogs.threatrating.properties.ThreatStressRatingTableModel;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Stress;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.StressBasedThreatRatingQuestionPopupEditorComponent;

public class StressBasedThreatRatingQuestionPopupCellEditorAndRendererFactory extends PopupEditableCellEditorAndRendererFactory
{
	public StressBasedThreatRatingQuestionPopupCellEditorAndRendererFactory(Project projectToUse, ChoiceQuestion questionToUse, RowColumnBaseObjectProvider objectProvider, FontForObjectProvider fontProvider) throws Exception 
	{
	    super();
	    
	    questionEditor = new StressBasedThreatRatingQuestionPopupEditorComponent(projectToUse, questionToUse);
	    questionRenderer = new StressBasedThreatRatingQuestionPopupEditorComponent(projectToUse, questionToUse);
	}
	
	public Object getCellEditorValue()
	{
		return questionEditor.getText();
	}
	
	@Override
	protected Component getEditorComponent()
	{
		return questionEditor;
	}

	@Override
	protected Component getRendererComponent()
	{
		return questionRenderer;
	}

	@Override
	protected void configureComponent(JTable table, Object value, int row, int column, Component rawComponent)
	{
		StressBasedThreatRatingQuestionPopupEditorComponent component = (StressBasedThreatRatingQuestionPopupEditorComponent) rawComponent;
		ChoiceItem choiceItem = (ChoiceItem) value;
		component.setText(choiceItem.getCode());
		
		ThreatStressRatingTableModel model = ((ThreatStressRatingTable) table).getThreatStressRatingTableModel();
		ORef threatRef = model.getThreatRef();
		ORef targetRef = model.getTargetRef();
		component.setThreatRef(threatRef);
		component.setTargetRef(targetRef);
		Stress stressRef = model.getStress(row, column);
		component.setStressRef(stressRef);
	}
	
	private StressBasedThreatRatingQuestionPopupEditorComponent questionEditor;
	private StressBasedThreatRatingQuestionPopupEditorComponent questionRenderer;
}
