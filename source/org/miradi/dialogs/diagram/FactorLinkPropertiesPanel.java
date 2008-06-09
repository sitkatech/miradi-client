/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.dialogs.diagram;

import org.miradi.actions.jump.ActionJumpDiagramWizardLinkDirectThreatsToTargetsStep;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.threatstressrating.properties.ThreatStressRatingPropertiesPanel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.FactorLink;
import org.miradi.project.Project;
import org.miradi.questions.DiagramLinkColorQuestion;
import org.miradi.views.umbrella.ObjectPicker;

public class FactorLinkPropertiesPanel extends ObjectDataInputPanel
{
	public FactorLinkPropertiesPanel(Project projectToUse, DiagramLink link)
	{
		super(projectToUse, ObjectType.FACTOR_LINK, link.getWrappedId());

		addField(createCheckBoxField(FactorLink.TAG_BIDIRECTIONAL_LINK, BooleanData.BOOLEAN_TRUE, BooleanData.BOOLEAN_FALSE));
		addField(createChoiceField(DiagramLink.getObjectType(), DiagramLink.TAG_COLOR, new DiagramLinkColorQuestion()));
		
		setObjectRefs(new ORef[]{link.getRef(), link.getWrappedRef()});
		updateFieldsFromProject();
	}

	public FactorLinkPropertiesPanel(MainWindow mainWindow, ORef factorLinkRef, ObjectPicker objectPicker) throws Exception
	{
		super(mainWindow.getProject(), factorLinkRef);
				
		addThreatStressRatingPropertiesPanel(mainWindow, objectPicker);
		
		setObjectRefs(objectPicker.getSelectedHierarchies()[0]);
		updateFieldsFromProject();
	}
	
	public FactorLinkPropertiesPanel(MainWindow mainWindow, DiagramLink link, ObjectPicker objectPicker) throws Exception
	{
		super(mainWindow.getProject(), ObjectType.FACTOR_LINK, link.getWrappedId());
		
		addField(createChoiceField(DiagramLink.getObjectType(), DiagramLink.TAG_COLOR, new DiagramLinkColorQuestion()));
		addThreatStressRatingPropertiesPanel(mainWindow, objectPicker);
		
		setObjectRefs(objectPicker.getSelectedHierarchies()[0]);
		updateFieldsFromProject();
	}

	private void addThreatStressRatingPropertiesPanel(MainWindow mainWindow, ObjectPicker objectPicker) throws Exception
	{
		ThreatStressRatingPropertiesPanel threatStressRatingPropertiesPanel = new ThreatStressRatingPropertiesPanel(mainWindow, objectPicker);
		addSubPanel(threatStressRatingPropertiesPanel);
		add(threatStressRatingPropertiesPanel);
	}

	public String getPanelDescription()
	{
		return EAM.text("Link Properties");
	}
	
	public Class getJumpActionClass()
	{
		return ActionJumpDiagramWizardLinkDirectThreatsToTargetsStep.class;
	}
	
	@Override
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		if (event.isSetDataCommandWithThisTypeAndTag(FactorLink.getObjectType(), FactorLink.TAG_BIDIRECTIONAL_LINK))
			ensureSiblingsHaveEqualBidirectionality((CommandSetObjectData) event.getCommand());
	}

	private void ensureSiblingsHaveEqualBidirectionality(CommandSetObjectData command)
	{
		try
		{
			ORef factorLinkRef = command.getObjectORef();
			FactorLink factorLink = FactorLink.find(getProject(), factorLinkRef);
			ORefList diagramLinkReferrerRefs = factorLink.findObjectsThatReferToUs(DiagramLink.getObjectType());
			for (int index = 0; index < diagramLinkReferrerRefs.size(); ++index)
			{
				DiagramLink diagramLink = DiagramLink.find(getProject(), diagramLinkReferrerRefs.get(index));
				ensureSiblingsHaveEqualBidirectionality(diagramLink);
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	private void ensureSiblingsHaveEqualBidirectionality(DiagramLink diagramLink) throws CommandFailedException
	{
		ORefList groupBoxReferrers = diagramLink.findObjectsThatReferToUs(DiagramLink.getObjectType());
		for (int index = 0; index < groupBoxReferrers.size(); ++index)
		{
			DiagramLink groupBoxLink = DiagramLink.find(getProject(), groupBoxReferrers.get(index));
			ensureChildrenBidirectionality(groupBoxLink, diagramLink.getUnderlyingLink().getData(FactorLink.TAG_BIDIRECTIONAL_LINK));
		}
	}

	private void ensureChildrenBidirectionality(DiagramLink groupBoxLink, String bidirectionalMode) throws CommandFailedException
	{
		ORefList diagramLinkChildRefs = groupBoxLink.getGroupedDiagramLinkRefs();
		for (int index = 0; index < diagramLinkChildRefs.size(); ++index)
		{
			DiagramLink childLink = DiagramLink.find(getProject(), diagramLinkChildRefs.get(index));
			String childLinkBidirectionalMode = childLink.getUnderlyingLink().getData(FactorLink.TAG_BIDIRECTIONAL_LINK);
			if (!bidirectionalMode.equals(childLinkBidirectionalMode))
			{
				CommandSetObjectData setBidirectionality = new CommandSetObjectData(childLink.getWrappedRef(), FactorLink.TAG_BIDIRECTIONAL_LINK, bidirectionalMode);
				getProject().executeInsideListener(setBidirectionality);
			}
		}
	}
}
