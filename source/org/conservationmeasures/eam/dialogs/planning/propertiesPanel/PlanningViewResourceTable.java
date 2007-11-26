/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import java.awt.Color;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objectpools.ResourcePool;
import org.conservationmeasures.eam.objects.AccountingCode;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.FundingSource;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

public class PlanningViewResourceTable extends PlanningViewAbstractTableWithPreferredScrollableViewportSize implements ObjectPicker
{
	public PlanningViewResourceTable(PlanningViewResourceTableModel modelToUse)
	{
		super(modelToUse);
		model = modelToUse;
		
		setBackground(getColumnBackGroundColor(0));
		selectionListeners = new Vector();
		rebuildColumnEditorsAndRenderers();
	}
	
	public Color getColumnBackGroundColor(int column)
	{
		return AppPreferences.RESOURCE_TABLE_BACKGROUND;
	}

	int getPreferredScrollableViewportWidth()
	{
		return getPreferredSize().width;
	}
	
	public void rebuildColumnEditorsAndRenderers()
	{
		for (int i = 0; i < model.getColumnCount(); ++i)
		{
			createResourceCombo(i);
			createFundingSourceColumn(i);
			createAccountingCodeColumn(i);
		}
	}
	
	private void createAccountingCodeColumn(int column)
	{
		if (! model.isAccountingCodeColumn(column))
			return;
		
		AccountingCode[] accountingCodes = getObjectManager().getAccountingCodePool().getAllAccountingCodes();
		AccountingCode invalidAccountingCode = new AccountingCode(getObjectManager(), BaseId.INVALID);
		createComboColumn(accountingCodes, column, invalidAccountingCode);
	}
	
	private void createResourceCombo(int column)
	{
		if (! model.isResourceColumn(column))
			return;
		
		ProjectResource[] resources = getAllProjectResources();
		ProjectResource invalidResource = new ProjectResource(getObjectManager(), BaseId.INVALID);
		createComboColumn(resources, column, invalidResource);
	}
	
	private void createFundingSourceColumn(int column)
	{
		if (! model.isFundingSourceColumn(column))
			return;

		FundingSource[] fundingSources = getObjectManager().getFundingSourcePool().getAllFundingSources();
		FundingSource invalidFundintSource = new FundingSource(getObjectManager(), BaseId.INVALID);
		createComboColumn(fundingSources, column, invalidFundintSource);
	}
	
	public int getColumnAlignment()
	{
		return JLabel.RIGHT;
	}
	
	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}
	
	public ProjectResource[] getAllProjectResources()
	{
		return  getResourcePool().getAllProjectResources();
	}
	
	public ResourcePool getResourcePool()
	{
		return getObjectManager().getResourcePool();
	}

	private Project getProject()
	{
		return model.getProject();
	}
	
	private ObjectManager getObjectManager()
	{
		return getProject().getObjectManager();
	}
	
	public TreeTableNode[] getSelectedTreeNodes()
	{
		return null;
	}

	public BaseObject[] getSelectedObjects()
	{
		int selectedRow = getSelectedRow();
		if (selectedRow < 0)
			return new BaseObject[0];
		
		if (selectedRow >=  model.getRowCount())
			return new BaseObject[0];
		
		Assignment selectedAssignment = model.getAssignment(selectedRow);
		if (selectedAssignment == null)
			return new BaseObject[0];
	
		return new BaseObject[] {selectedAssignment};
	}
	
	public ORefList getSelectionHierarchy()
	{
		return null;
	}
		
	public ORefList[] getSelectedHierarchies()
	{
		int[] rows = getSelectedRows();
		ORefList[] selectedHierarchies = new ORefList[rows.length];
		for(int i = 0; i < rows.length; ++i)
		{
			BaseObject objectFromRow = model.getBaseObjectForRow(rows[i]);
			ORefList selectedObjectRefs = new ORefList();
			selectedObjectRefs.add(objectFromRow.getRef());
			selectedHierarchies[i] = selectedObjectRefs;
		}
		
		return selectedHierarchies;
	}

	public void ensureObjectVisible(ORef ref)
	{
		// TODO Auto-generated method stub
		// we should scroll the table as needed to make this 
		// probably-newly-created object visible
	}

	public void addSelectionChangeListener(ListSelectionListener listener)
	{
		selectionListeners.add(listener);
	}

	public void removeSelectionChangeListener(ListSelectionListener listener)
	{
		selectionListeners.remove(listener);
	}

	public void valueChanged(ListSelectionEvent e)
	{
		super.valueChanged(e);
		if(selectionListeners == null)	
			return;
		
		for(int i = 0; i < selectionListeners.size(); ++i)
		{
			ListSelectionListener listener = (ListSelectionListener)selectionListeners.get(i);
			listener.valueChanged(null);
		}
	}
	
	private Vector selectionListeners;
	private PlanningViewResourceTableModel model;
    public static final String UNIQUE_IDENTIFIER = "PlanningViewResourceTable";
}
