package org.conservationmeasures.eam.dialogs.viability;

import javax.swing.Box;
import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogs.ObjectDataInputPanelSpecial;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.KeyEcologicalAttributeTypeQuestion;

public class TargetViabilityKeaPropertiesPanel extends ObjectDataInputPanelSpecial
{
	public TargetViabilityKeaPropertiesPanel(Project projectToUse, Actions actions) throws Exception
	{
		super(projectToUse, new ORef(ObjectType.TARGET, new FactorId(BaseId.INVALID.asInt())));		
	
		JPanel mainPropertiesPanel = new JPanel();
		createKeaPropertiesPanel(mainPropertiesPanel);
		addFieldComponent(mainPropertiesPanel);
		updateFieldsFromProject();
	}
	
	private void createKeaPropertiesPanel(JPanel mainPropertiesPanelPanel)
	{
		ObjectDataInputField keaLabel = addField(createStringField(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, KeyEcologicalAttribute.TAG_LABEL));
		ObjectDataInputField keaDescription = addField(createMultilineField(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, KeyEcologicalAttribute.TAG_DESCRIPTION));
		ObjectDataInputField keaType = addField(createChoiceField(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, new KeyEcologicalAttributeTypeQuestion(KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE)));

		JPanel keaPanel = createGridLayoutPanel(1,2);
		keaPanel.add(createColumnJPanel(keaLabel));
		keaPanel.add(Box.createHorizontalStrut(STD_SPACE_20));
		keaPanel.add(createColumnJPanel(keaType));
		
		JPanel keaDescPanel = createGridLayoutPanel(1,2);
		keaDescPanel.add(createColumnJPanel(keaDescription));
		
		JPanel mainKeaPanel = createGridLayoutPanel(3, 1);
		addBoldedTextBorder(mainKeaPanel, "KEA");
		mainKeaPanel.add(keaPanel);
		mainKeaPanel.add(keaDescPanel);
		mainPropertiesPanelPanel.add(mainKeaPanel);
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Key Ecological Attribute Properties");
	}
}
