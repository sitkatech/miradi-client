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
package org.miradi.dialogfields;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.martus.swing.UiLabel;
import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.RatingIcon;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ThreatTargetVirtualLink;
import org.miradi.objects.Cause;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.ThreatRatingQuestion;

public class ThreatStressRatingValueReadonlyComponent extends MiradiPanel
{
	public ThreatStressRatingValueReadonlyComponent(Project projectToUse)
	{
		super();
		
		project = projectToUse;
		question = getProject().getQuestion(ThreatRatingQuestion.class);
		component = new PanelTitleLabel("");
		Border lineBorder = new LineBorder(Color.BLACK);
		Border emptyBorder = new EmptyBorder(3, 3, 3, 3);
		CompoundBorder border = new CompoundBorder(lineBorder, emptyBorder);
		component.setBorder(border);
		component.setForeground(EAM.READONLY_FOREGROUND_COLOR);
		component.setBackground(EAM.READONLY_BACKGROUND_COLOR);
		getComponent().setFocusable(false);
	}
	
	public void setObjectRefs(ORefList selectedHeirearchy)
	{
		ORef threatRef = selectedHeirearchy.getRefForType(Cause.getObjectType());
		ORef targetRef = selectedHeirearchy.getRefForType(Target.getObjectType());
		String threatRatingBundleValue = "";
		if (!threatRef.isInvalid() && !targetRef.isInvalid())
			threatRatingBundleValue = new ThreatTargetVirtualLink(getProject()).getCalculatedThreatRatingBundleValue(threatRef, targetRef);
		
		setText(threatRatingBundleValue);
	}
	
	public void setText(String code)
	{
		ChoiceItem choice = getRatingChoice(code);
		String text = "";
		Icon icon = null;
		if(choice != null)
		{
			text = choice.getLabel();
			icon = RatingIcon.createFromChoice(choice);
		}
		
		component.setText(text);
		component.setIcon(icon);
		component.invalidate();
	}
	
	private ChoiceItem getRatingChoice(String text)
	{
		return question.findChoiceByCode(text);
	}

	public JComponent getComponent()
	{
		return component;
	}
	
	public Project getProject()
	{
		return project;
	}

	private ChoiceQuestion question;
	private UiLabel component;
	private Project project;
}
