/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.base.ObjectPoolTable;
import org.conservationmeasures.eam.dialogs.base.ObjectPoolTableModel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiPopupMenu;

abstract public class DiagramPageList extends ObjectPoolTable
{
	public DiagramPageList(MainWindow mainWindowToUse, ObjectPoolTableModel objectPoolTableModel)
	{
		super(objectPoolTableModel, SORTABLE_COLUMN_INDEX);
		project = mainWindowToUse.getProject();
		actions = mainWindowToUse.getActions();
		
		getSelectionModel().addListSelectionListener(new DiagramObjectListSelectionListener(project));
		addMouseListener(new MouseHandler());
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setBorder(BorderFactory.createEtchedBorder());
		setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		resizeTable(6);
		
		ignoreKeystrokesThatShouldGoToDiagram();
	}

	private void ignoreKeystrokesThatShouldGoToDiagram()
	{
		InputMap im = getInputMap(javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		KeyStroke ctrlA = KeyStroke.getKeyStroke('A', KeyEvent.CTRL_DOWN_MASK);
		im.put(ctrlA, "none");
	}
	
	@Override
	protected void addRowHeightSaver()
	{
		//NOTE: this is called from the base class constructor
		//By not calling super, we are disabling row hieght changing (saving)
	}
	
	public void listChanged()
	{
		getObjectPoolTableModel().rowsWereAddedOrRemoved();
	}

	private Vector buildCommandsToSetCurrentDiagramObjectRef(ORef selectedRef) throws Exception
	{
		ORef currentDiagramRef = getCurrentDiagramViewDataRef();
		if (currentDiagramRef.equals(selectedRef))
			return new Vector();
	
		Vector commandsVector = new Vector();
		ViewData viewData = project.getDiagramViewData();
		commandsVector.add(new CommandSetObjectData(viewData.getRef(), getCurrentDiagramViewDataTag(), selectedRef));
		return commandsVector;
	}

	public static String getCurrentDiagramViewDataTag(int objectType)
	{
		if (objectType == ObjectType.CONCEPTUAL_MODEL_DIAGRAM)
			return ViewData.TAG_CURRENT_CONCEPTUAL_MODEL_REF;
		
		if (objectType == ObjectType.RESULTS_CHAIN_DIAGRAM)
			return ViewData.TAG_CURRENT_RESULTS_CHAIN_REF;
		
		throw new RuntimeException("Could not find corrent tag for " + objectType);
	}
	
	public String getCurrentDiagramViewDataTag()
	{
		return getCurrentDiagramViewDataTag(getManagedDiagramType());
	}
	
	public ORef getCurrentDiagramViewDataRef() throws Exception
	{
		ViewData viewData = project.getDiagramViewData();
		return getCurrentDiagramViewDataRef(viewData, getManagedDiagramType());
	}
	
	public JPopupMenu getPopupMenu()
	{
		UiPopupMenu menu = new UiPopupMenu();
		Class[] rightClickMenuActions = getPopUpMenuActions();
		for (int i = 0; i < rightClickMenuActions.length; ++i)
		{
			menu.add(getActions().get(rightClickMenuActions[i]));
		}

		return menu;
	}

	private void handleRightClick(MouseEvent event)
	{
		JPopupMenu menu = getPopupMenu();
		getActions().updateActionStates();
		menu.show(this, event.getX(), event.getY());
	}
	
	public static ORef getCurrentDiagramViewDataRef(ViewData viewData, int objectType) throws Exception
	{
		String currrentDiagramViewDataTag = getCurrentDiagramViewDataTag(objectType);
		String orefAsJsonString = viewData.getData(currrentDiagramViewDataTag);
		
		return ORef.createFromString(orefAsJsonString);
	}
	
	public class MouseHandler extends MouseAdapter
	{
		public void mousePressed(MouseEvent event)
		{
			if(event.isPopupTrigger())
				doRightClickMenu(event);
		}

		public void mouseReleased(MouseEvent event)
		{
			if(event.isPopupTrigger())
				doRightClickMenu(event);
		}
		
		private void doRightClickMenu(MouseEvent event)
		{
			handleRightClick(event);
		}
	}
	
	public class DiagramObjectListSelectionListener  implements ListSelectionListener
	{
		public DiagramObjectListSelectionListener(Project projectToUse)
		{
			project = projectToUse;
		}

		public void valueChanged(ListSelectionEvent event)
		{
			try
			{
				setCurrentDiagram();
			}
			catch(Exception e)
			{
				EAM.panic(e);
			}
		}

		private void setCurrentDiagram() throws Exception
		{
			Vector commandsToExecute = new Vector();
			commandsToExecute.addAll(ensureDefaultMode());
			commandsToExecute.addAll(buildCommandsToSetCurrentDiagramObjectRef(getSelectedRef()));
			
			if (commandsToExecute.size() == 0)
				return;
			
			project.executeCommandsAsTransaction((Command[]) commandsToExecute.toArray(new Command[0]));		
		}

		private Vector ensureDefaultMode() throws Exception
		{
			ViewData viewData = project.getCurrentViewData();
			if (viewData.getData(ViewData.TAG_CURRENT_MODE).equals(ViewData.MODE_DEFAULT))
				return new Vector();
			
			Vector defaultCommandVector = new Vector();
			defaultCommandVector.add(new CommandSetObjectData(viewData.getRef(), ViewData.TAG_CURRENT_MODE, ViewData.MODE_DEFAULT));
			return defaultCommandVector;
		}

		private ORef getSelectedRef()
		{
			if (getSelectedHierarchies().length == 0)
				return ORef.INVALID;
			
			ORefList selectedDiagramObjectRefs = getSelectedHierarchies()[0];
			ORef selectedDiagramObjectRef = selectedDiagramObjectRefs.get(0);
			if (selectedDiagramObjectRef.isInvalid())
				return ORef.INVALID;
			
			return selectedDiagramObjectRef;
		}
		
		private Project project;
	}
	
	public boolean shouldSaveColumnWidth()
	{
		return false;
	}
	
	private Actions getActions()
	{
		return actions;
	}
	
	abstract public boolean isResultsChainPageList();
	
	abstract public boolean isConceptualModelPageList();
	
	abstract public int getManagedDiagramType();
	
	abstract public Class[] getPopUpMenuActions();
	
	private Project project;
	private Actions actions;
	
	private static final int SORTABLE_COLUMN_INDEX = 0;
}