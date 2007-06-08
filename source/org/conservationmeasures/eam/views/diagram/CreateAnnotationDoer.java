/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JDialog;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.AnnotationSelectionList;
import org.conservationmeasures.eam.dialogs.EAMDialog;
import org.conservationmeasures.eam.dialogs.ObjectTablePanel;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelButton;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.utils.FastScrollPane;
import org.conservationmeasures.eam.views.ObjectsDoer;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;
import org.martus.swing.Utilities;

public abstract class CreateAnnotationDoer extends ObjectsDoer
{
	
	abstract int getAnnotationType();
	abstract String getAnnotationIdListTag();


	public boolean isAvailable()
	{
		if (! isDiagramView())
			return false;
		
		return (getSelectedFactor() != null);
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		Factor factor = getSelectedFactor();
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			CommandCreateObject create = createObject();
			BaseId createdId = create.getCreatedId();
			getProject().executeCommand(CommandSetObjectData.createAppendIdCommand(factor, getAnnotationIdListTag(), createdId));
			
			ORef ref = new ORef(create.getObjectType(), createdId);
			ObjectPicker picker = getPicker();
			if(picker != null)
				picker.ensureObjectVisible(ref);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}
	
	protected CommandCreateObject createObject() throws CommandFailedException
	{
		CommandCreateObject create = new CommandCreateObject(getAnnotationType());
		getProject().executeCommand(create);
		return create;
	}
	
	protected CommandCreateObject cloneObject(BaseObject objectToClone) throws CommandFailedException
	{
		CommandCreateObject create = new CommandCreateObject(getAnnotationType());
		getProject().executeCommand(create);
		CommandSetObjectData[]  commands = objectToClone.createCommandsToClone(create.getCreatedId());
		getProject().executeCommands(commands);
		return create;
	}
	
	protected AnnotationSelectionList displayAnnotationList(String title, ObjectTablePanel tablePanel)
	{
		AnnotationSelectionList list = new AnnotationSelectionList(getProject(), tablePanel);
		EAMDialog dlg = new EAMDialog(getMainWindow(), title);
		JComponent buttonBar = createButtonBar(dlg, list);
		Container contents = dlg.getContentPane();
		contents.setLayout(new BorderLayout());
		contents.add(new FastScrollPane(list), BorderLayout.CENTER);
		contents.add(buttonBar, BorderLayout.AFTER_LAST_LINE);
		dlg.setModal(true);
		dlg.pack();
		dlg.setPreferredSize(new Dimension(600,400));
		Utilities.centerDlg(dlg);
		dlg.setVisible(true);
		tablePanel.dispose();
		return list;
	}
	
	private Box createButtonBar(EAMDialog dlg, AnnotationSelectionList list)
	{
		PanelButton cancel = new PanelButton(new CancelAction(dlg, list));
		PanelButton clone = new PanelButton(new CloneAction(dlg, list));
		dlg.getRootPane().setDefaultButton(cancel);
		Box buttonBar = Box.createHorizontalBox();
		Component[] components = new Component[] {Box.createHorizontalGlue(), cancel, clone, Box.createHorizontalStrut(10)};
		Utilities.addComponentsRespectingOrientation(buttonBar, components);
		return buttonBar;
	}
	
	public Factor getSelectedFactor()
	{
		BaseObject selected = getView().getSelectedObject();
		if(selected == null)
			return null;
		
		if(! Factor.isFactor(selected.getType()))
			return null;
		
		return (Factor)selected;
	}
	
	
	class CloneAction extends AbstractAction
	{
		public CloneAction(JDialog dialogToClose, AnnotationSelectionList panel)
		{
			super(EAM.text("Clone"));
			dlg = dialogToClose;
			listPanel = panel;
		}

		public void actionPerformed(ActionEvent arg0)
		{
			if (listPanel.getSelectedAnnotaton()==null)
			{
				EAM.okDialog(EAM.text("Notice"), new String[] {EAM.text("Please make a selection")});
				return;
			}
			dlg.dispose();
		}
		
		AnnotationSelectionList listPanel;
		JDialog dlg;
	}
	
	
	class CancelAction extends AbstractAction
	{
		public CancelAction(JDialog dialogToClose, AnnotationSelectionList panel)
		{
			super(EAM.text("Cancel"));
			dlg = dialogToClose;
			listPanel = panel;
		}

		public void actionPerformed(ActionEvent arg0)
		{
			listPanel.clearSelectedAnnotaton();
			dlg.dispose();
		}
		
		AnnotationSelectionList listPanel;
		JDialog dlg;
	}

}