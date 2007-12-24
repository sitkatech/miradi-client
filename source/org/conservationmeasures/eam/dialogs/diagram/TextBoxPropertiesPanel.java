/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.diagram;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.TextBox;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.DiagramFactorBackgroundQuestion;
import org.conservationmeasures.eam.questions.DiagramFactorFontColorQuestion;
import org.conservationmeasures.eam.questions.DiagramFactorFontSizeQuestion;
import org.conservationmeasures.eam.questions.DiagramFactorFontStyleQuestion;

public class TextBoxPropertiesPanel extends ObjectDataInputPanel
{
	public TextBoxPropertiesPanel(Project projectToUse, DiagramFactor diagramFactor)
	{
		super(projectToUse, diagramFactor.getWrappedORef());

		setObjectRefs(new ORef[] {diagramFactor.getWrappedORef(), diagramFactor.getRef()});

		addField(createStringField(TextBox.TAG_LABEL));
		addField(createChoiceField(DiagramFactor.getObjectType(), new DiagramFactorBackgroundQuestion(DiagramFactor.TAG_BACKGROUND_COLOR)));
		addField(createChoiceField(DiagramFactor.getObjectType(), new DiagramFactorFontSizeQuestion(DiagramFactor.TAG_FONT_SIZE)));
		addField(createChoiceField(DiagramFactor.getObjectType(), new DiagramFactorFontColorQuestion(DiagramFactor.TAG_FONT_COLOR)));
		addField(createChoiceField(DiagramFactor.getObjectType(), new DiagramFactorFontStyleQuestion(DiagramFactor.TAG_FONT_STYLE)));

		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Text Box Properties");
	}
}
