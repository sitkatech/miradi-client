/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.KeyEcologicalAttributeTypeQuestion;
import org.conservationmeasures.eam.questions.MeasurementStatusQuestion;
import org.conservationmeasures.eam.questions.TrendQuestion;

import com.jhlabs.awt.Alignment;
import com.jhlabs.awt.GridLayoutPlus;

public class TargetViabilityTreePropertiesPanel extends ObjectDataInputPanelSpecial
{
	public TargetViabilityTreePropertiesPanel(Project projectToUse, Actions actions) throws Exception
	{
		this(projectToUse, actions, new FactorId(BaseId.INVALID.asInt()));
	}
	
	public TargetViabilityTreePropertiesPanel(Project projectToUse, Actions actions, Factor factor) throws Exception
	{
		this(projectToUse, actions, factor.getId());
	}
	
	public TargetViabilityTreePropertiesPanel(Project projectToUse, Actions actions, BaseId idToShow) throws Exception
	{
		super(projectToUse, new ORef(ObjectType.FACTOR, idToShow));
		ObjectDataInputField aa1 = addField(createStringField(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, KeyEcologicalAttribute.TAG_LABEL));
		ObjectDataInputField aa2 = addField(createMultilineField(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, KeyEcologicalAttribute.TAG_DESCRIPTION));
		ObjectDataInputField aa3 = addField(createObjectChoiceField(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, new KeyEcologicalAttributeTypeQuestion(KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE)));
		ObjectDataInputField aa4 = addField(createStringField(ObjectType.INDICATOR, Indicator.TAG_LABEL));
		ObjectDataInputField aa5 = addField(createObjectStringMapTableField(ObjectType.INDICATOR,  new MeasurementStatusQuestion(Indicator.TAG_INDICATOR_THRESHOLDS)));
		ObjectDataInputField aa6 = addField(createRatingChoiceField(ObjectType.INDICATOR, new MeasurementStatusQuestion(Indicator.TAG_MEASUREMENT_STATUS)));  
		ObjectDataInputField aa7 = addField(createObjectChoiceField(ObjectType.INDICATOR, new TrendQuestion(Indicator.TAG_MEASUREMENT_TREND)));
		ObjectDataInputField aa8 = addField(createDateChooserField(ObjectType.INDICATOR, Indicator.TAG_MEASUREMENT_DATE));
		ObjectDataInputField aa9 = addField(createStringField(ObjectType.INDICATOR, Indicator.TAG_MEASUREMENT_SUMMARY));
		ObjectDataInputField aa0 = addField(createMultilineField(ObjectType.INDICATOR, Indicator.TAG_MEASUREMENT_DETAIL));
		ObjectDataInputField aa11 = addField(createRatingChoiceField(ObjectType.GOAL, new MeasurementStatusQuestion(Goal.TAG_DESIRED_STATUS)));
		ObjectDataInputField aa22 = addField(createStringField(ObjectType.GOAL, Goal.TAG_BY_WHEN));
		ObjectDataInputField aa33 = addField(createMultilineField(ObjectType.GOAL, Goal.TAG_DESIRED_SUMMARY));
		
		JPanel panel = new JPanel();
		GridLayoutPlus layout = new GridLayoutPlus(0, 2);
		layout.setColAlignment(0, Alignment.NORTHEAST);
		panel.setLayout(layout);
		
		createRow(panel, aa1);
		createRow(panel,aa2);
		createRow(panel,aa3);
		createRow(panel,aa4);
		createRow(panel,aa5);
		createRow(panel,aa6);
		createRow(panel,aa7);
		createRow(panel,aa8);
		createRow(panel,aa9);
		createRow(panel,aa0);
		createRow(panel,aa11);
		createRow(panel,aa22);
		createRow(panel,aa33);
		
		addFieldComponent(panel);
		
		updateFieldsFromProject();
	}
	
	public void createRow(JPanel box, ObjectDataInputField field)
	{
		JPanel panel = new JPanel(new BorderLayout());
		box.add(createLabel(field));
		panel.add(field.getComponent(), BorderLayout.BEFORE_LINE_BEGINS);
		box.add(panel);
	}

	public void setObjectRefs(Vector orefsToUse)
	{
		BaseId objectId = getObjectIdForTypeInThisList(ObjectType.INDICATOR, orefsToUse);
		if (!objectId.isInvalid())
			orefsToUse = insertIndicatorsGoal(orefsToUse, objectId);
		super.setObjectRefs(orefsToUse);
	}

	private Vector insertIndicatorsGoal(Vector orefsToUse, BaseId objectId)
	{
		Indicator indicator = (Indicator)getProject().findObject(ObjectType.INDICATOR, objectId);
		try
		{
			IdList list = new IdList(indicator.getData(Indicator.TAG_GOAL_IDS));
			if (list.size()!=0)
				orefsToUse.insertElementAt(new ORef(ObjectType.GOAL,list.get(0)),0);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		
		return orefsToUse;
	}
	
	
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Key Ecological Attribute Properties");
	}

}
