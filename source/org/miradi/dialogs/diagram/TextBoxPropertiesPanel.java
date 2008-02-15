/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.diagram;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.TextBox;
import org.miradi.project.Project;
import org.miradi.questions.DiagramFactorBackgroundQuestion;
import org.miradi.questions.DiagramFactorFontColorQuestion;
import org.miradi.questions.DiagramFactorFontSizeQuestion;
import org.miradi.questions.DiagramFactorFontStyleQuestion;

public class TextBoxPropertiesPanel extends ObjectDataInputPanel
{
	public TextBoxPropertiesPanel(Project projectToUse, DiagramFactor diagramFactor)
	{
		super(projectToUse, diagramFactor.getWrappedORef());

		setObjectRefs(new ORef[] {diagramFactor.getWrappedORef(), diagramFactor.getRef()});

		addField(createExpandableField(TextBox.TAG_LABEL));
		addField(createChoiceField(DiagramFactor.getObjectType(), DiagramFactor.TAG_BACKGROUND_COLOR, new DiagramFactorBackgroundQuestion()));
		addField(createChoiceField(DiagramFactor.getObjectType(), DiagramFactor.TAG_FONT_SIZE, new DiagramFactorFontSizeQuestion()));
		addField(createChoiceField(DiagramFactor.getObjectType(), DiagramFactor.TAG_FOREGROUND_COLOR, new DiagramFactorFontColorQuestion()));
		addField(createChoiceField(DiagramFactor.getObjectType(), DiagramFactor.TAG_FONT_STYLE, new DiagramFactorFontStyleQuestion()));

		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Text Box Properties");
	}
}
