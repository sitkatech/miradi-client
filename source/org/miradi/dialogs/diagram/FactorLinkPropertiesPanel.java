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
package org.miradi.dialogs.diagram;

import org.miradi.actions.jump.ActionJumpDiagramWizardLinkDirectThreatsToTargetsStep;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramLink;
import org.miradi.project.Project;
import org.miradi.questions.DiagramLinkColorQuestion;
import org.miradi.utils.CommandVector;

public class FactorLinkPropertiesPanel extends ObjectDataInputPanel
{
	public static FactorLinkPropertiesPanel createWithOnlyBidirectionalAndColorPropertiesPanel(Project projectToUse, DiagramLink link)
	{
		return new FactorLinkPropertiesPanel(projectToUse, link);
	}
	
	private FactorLinkPropertiesPanel(Project projectToUse, DiagramLink link)
	{
		super(projectToUse, link.getRef());

		addField(createCheckBoxField(DiagramLink.getObjectType(), DiagramLink.TAG_IS_BIDIRECTIONAL_LINK, DiagramLink.BIDIRECTIONAL_LINK, BooleanData.BOOLEAN_FALSE));
		addField(createChoiceField(DiagramLink.getObjectType(), DiagramLink.TAG_COLOR, new DiagramLinkColorQuestion()));
		
		setObjectRefsWithGroupBoxLinkAndChildrenRefs(link);
		updateFieldsFromProject();
	}

	private void setObjectRefsWithGroupBoxLinkAndChildrenRefs(DiagramLink groupBoxLink)
	{
		ORefList selfOrChildrenRefs = groupBoxLink.getSelfOrChildren();
		setObjectRefs(selfOrChildrenRefs);
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Link Properties");
	}
	
	@Override
	public Class getJumpActionClass()
	{
		return ActionJumpDiagramWizardLinkDirectThreatsToTargetsStep.class;
	}
	
	@Override
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		if (event.isSetDataCommandWithThisTypeAndTag(DiagramLink.getObjectType(), DiagramLink.TAG_IS_BIDIRECTIONAL_LINK))
			ensureSiblingsHaveEqualBidirectionality((CommandSetObjectData) event.getCommand());
	}

	private void ensureSiblingsHaveEqualBidirectionality(CommandSetObjectData command)
	{
		try
		{
			ORef diagramLinkRef = command.getObjectORef();
			DiagramLink diagramLink = DiagramLink.find(getProject(), diagramLinkRef);
			ensureSiblingsHaveEqualBidirectionality(diagramLink);
			
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
			ensureChildrenBidirectionality(groupBoxLink, diagramLink.isBidirectional());
		}
	}

	private void ensureChildrenBidirectionality(DiagramLink groupBoxLink, boolean desiredMode) throws CommandFailedException
	{
		ORefList diagramLinkChildRefs = groupBoxLink.getGroupedDiagramLinkRefs();
		for (int index = 0; index < diagramLinkChildRefs.size(); ++index)
		{
			DiagramLink childLink = DiagramLink.find(getProject(), diagramLinkChildRefs.get(index));
			if(desiredMode != childLink.isBidirectional())
			{
				CommandVector setBidirectionality = childLink.createCommandsToSetBidirectionalFlag(desiredMode);
				getProject().executeAsSideEffect(setBidirectionality);
			}
		}
	}
}
