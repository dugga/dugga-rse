/*******************************************************************************
 * Copyright (c) 2005-2009 SoftLanding Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     SoftLanding - initial API and implementation
 *******************************************************************************/
package com.softlanding.rse.extensions.spooledfiles;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.ibm.as400.access.PrintObject;
import com.ibm.as400.access.SpooledFile;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class SpooledFilesTableViewer extends Composite implements IMenuListener {
	private Table table;
	private TableViewer viewer;
	private SpooledFilesContentProvider spooledFilesContentProvider;
	private ArrayList actions = new ArrayList();
	private static SpooledFileFilter filter;
	private static SpooledFileFactory spooledFileFactory;
	
	private static final int minWidth = 75;
	private static final String TAG_SORTER_COLUMN = "sorterColumn"; //$NON-NLS-1$
	private static final String TAG_SORTER_REVERSED = "sorterReversed"; //$NON-NLS-1$
	private static final String tableID = "SpooledFilesTableViewer"; //$NON-NLS-1$
	
	private static ColumnLayoutData[] columnLayouts = {
		new ColumnWeightData(100, minWidth, true),
		new ColumnWeightData(100, minWidth, true),
		new ColumnWeightData(100, minWidth, true),
		new ColumnWeightData(100, minWidth, true),
		new ColumnWeightData(100, minWidth, true),
		new ColumnWeightData(100, minWidth, true),
		new ColumnWeightData(100, minWidth, true),
		new ColumnWeightData(100, minWidth, true),
		new ColumnWeightData(100, minWidth, true),
		new ColumnWeightData(100, minWidth, true),
		new ColumnWeightData(100, minWidth, true),
		new ColumnWeightData(100, minWidth, true),
		new ColumnWeightData(100, minWidth, true),
		new ColumnWeightData(100, minWidth, true),
		new ColumnWeightData(100, minWidth, true)};
		
	private static String[] columnHeaders = {
		ExtensionsPlugin.getResourceString("File_4"), //$NON-NLS-1$
		ExtensionsPlugin.getResourceString("Device_or_Queue_5"), //$NON-NLS-1$
		ExtensionsPlugin.getResourceString("User_Data_6"), //$NON-NLS-1$
		ExtensionsPlugin.getResourceString("Status_7"), //$NON-NLS-1$
		ExtensionsPlugin.getResourceString("Total_Pages_8"), //$NON-NLS-1$
		ExtensionsPlugin.getResourceString("Current_Page_9"), //$NON-NLS-1$
		ExtensionsPlugin.getResourceString("User_10"),	 //$NON-NLS-1$
		ExtensionsPlugin.getResourceString("Job_11"), //$NON-NLS-1$
		ExtensionsPlugin.getResourceString("Job_Number_12"), //$NON-NLS-1$
		ExtensionsPlugin.getResourceString("File_Number_13"),	 //$NON-NLS-1$
		ExtensionsPlugin.getResourceString("Priority_14"),					 //$NON-NLS-1$
		ExtensionsPlugin.getResourceString("Copy_15"), //$NON-NLS-1$
		ExtensionsPlugin.getResourceString("Form_Type_16"), //$NON-NLS-1$
		ExtensionsPlugin.getResourceString("Creation_Date_17"), //$NON-NLS-1$
		ExtensionsPlugin.getResourceString("Creation_Time_18") //$NON-NLS-1$
	};


	public SpooledFilesTableViewer(Composite parent, int style) {
		super(parent, style);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		setLayout(layout);
		createControls();
	}
	
	private void createControls() {
		spooledFilesContentProvider = new SpooledFilesContentProvider();
		createTable();
	}
	
	private void createTable() {
		table = new Table(this, SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
		table.setLayoutData(
		new GridData(GridData.FILL_BOTH | GridData.GRAB_VERTICAL | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
		table.setLinesVisible(true);
		viewer = new TableViewer(table);
    	viewer.setUseHashlookup(true);    	
    	createColumns();
    	createMenus();
    	viewer.setLabelProvider(new SpooledFilesLabelProvider());
    	viewer.setSorter(new SpooledFilesSorter(3));
//    	addAction(new SaveColumnsAction(table, "ResourceChangeTableViewer", "Save column sizes"));
//    	addAction(new SelectAllAction("Select all", table));
    	viewer.setContentProvider(spooledFilesContentProvider);
    	viewer.setInput(ExtensionsPlugin.getWorkspace().getRoot());
		table.setSize(600, 300);
		table.update();
	}
	
	private void createColumns() {
		SelectionListener headerListener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// column selected - need to sort
				int column = table.indexOf((TableColumn) e.widget);
				SpooledFilesSorter oldSorter = (SpooledFilesSorter) viewer.getSorter();
				if (oldSorter != null && column == oldSorter.getColumnNumber()) {
				oldSorter.setReversed(!oldSorter.isReversed());
				viewer.refresh();
				} else {
					viewer.setSorter(new SpooledFilesSorter(column));
				}
			}
		};
		TableLayout layout = new TableLayout();
		table.setLayout(layout);
		table.setHeaderVisible(true);	
		for (int i = 0; i < columnHeaders.length; i++) {
			int w = ExtensionsPlugin.getDefault().getPreferenceStore().getInt(tableID + i);
			if (w != 0) {
				columnLayouts[i] = new ColumnPixelData(w, true);
			}
		}
		for (int i = 0; i < columnHeaders.length; i++) {
			layout.addColumnData(columnLayouts[i]);
			TableColumn tc = new TableColumn(table, SWT.NONE,i);
			tc.setResizable(columnLayouts[i].resizable);
			tc.setText(columnHeaders[i]);
			tc.addSelectionListener(headerListener);
		}
		table.setSize(table.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	private void createMenus() {
		MenuManager menuMgr = new
		MenuManager("#SpooledFilesPopUp"); //$NON-NLS-1$
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		menuMgr.addMenuListener(this);
		menuMgr.setRemoveAllWhenShown(true);
		viewer.getControl().setMenu(menu);
	}
	
	public void addAction(Action action) {
		actions.add(action);
	}
	
	public void addSeparator() {
		actions.add(new Separator());
	}
	
	public void menuAboutToShow(IMenuManager menu) {
		menu.add(new GroupMarker("openViewSave"));	 //$NON-NLS-1$
		menu.add(new GroupMarker("changeMoveDelete")); //$NON-NLS-1$
		menu.add(new GroupMarker("holdReleaseMessage")); //$NON-NLS-1$
		
		Item[] items = ((Table)viewer.getControl()).getSelection();
		//if (items.length > 0) {
			Iterator iter = actions.iterator();
			while (iter.hasNext()) {
				Object menuItem = iter.next();
				if (menuItem instanceof Action) {
					menu.add((Action)menuItem);
				}
				if (menuItem instanceof Separator) {
					menu.add((Separator)menuItem);
				}
			}
		//}
	}
	
	public void refresh() {
		viewer.refresh();
	}
	
	public TableViewer getTableViewer() {
		return viewer;
	}
	
	private class SpooledFilesContentProvider implements IStructuredContentProvider {		
		public void dispose() {
		}	
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}		
		public Object[] getElements(Object arg0) {
			if (spooledFileFactory == null) spooledFileFactory = getDefaultSpooledFileFactory();
			if (filter == null) filter = getDefaultSpooledFileFilter();
			if ((spooledFileFactory == null) || (filter == null)) return new SpooledFileModel[0];
			try {
				SpooledFile[] splfs = spooledFileFactory.getSpooledFiles(filter);
				SpooledFileModel[] models = new SpooledFileModel[splfs.length];
				for (int i = 0; i < splfs.length; i++)
					models[i] = new SpooledFileModel(splfs[i]);
				return models;
			} catch (Exception e) {
				return new SpooledFileModel[0];
			}
		}
	}
	
	static class SpooledFilesLabelProvider
		extends LabelProvider
		implements ITableLabelProvider {

		private static String[] keys = {
			"file", //$NON-NLS-1$
			"queue", //$NON-NLS-1$
			"data", //$NON-NLS-1$
			"sts", //$NON-NLS-1$
			"pages", //$NON-NLS-1$
			"page", //$NON-NLS-1$
			"user",	 //$NON-NLS-1$
			"job", //$NON-NLS-1$
			"jobnbr", //$NON-NLS-1$
			"fileNbr",	 //$NON-NLS-1$
			"pty",					 //$NON-NLS-1$
			"cpy", //$NON-NLS-1$
			"type", //$NON-NLS-1$
			"crtDate", //$NON-NLS-1$
			"crtTime" //$NON-NLS-1$
		};
				
		public String getColumnText(Object element, int columnIndex) {
			if ((columnIndex >= 0) && (columnIndex <= 14)) {
				SpooledFileModel model = (SpooledFileModel)element;
				SpooledFile splf = model.getSpooledFile();
				try {
					String rtnText;
					switch (columnIndex) { 
						case 0: rtnText = splf.getName(); break;
						case 1: rtnText = model.getOutputQueue(); break;
						case 2: rtnText = splf.getStringAttribute(PrintObject.ATTR_USERDATA); break;
						case 3: rtnText = splf.getStringAttribute(PrintObject.ATTR_SPLFSTATUS); break;
						case 4: rtnText = splf.getIntegerAttribute(PrintObject.ATTR_PAGES).toString(); break;
						case 5: rtnText = splf.getIntegerAttribute(PrintObject.ATTR_CURPAGE).toString(); break;
						case 6: rtnText = splf.getJobUser(); break;
						case 7: rtnText = splf.getJobName(); break;
						case 8: rtnText = splf.getJobNumber(); break;
						case 9: rtnText = new Integer(splf.getNumber()).toString(); break;
						case 10: rtnText = splf.getStringAttribute(PrintObject.ATTR_OUTPTY); break;
						case 11: rtnText = splf.getIntegerAttribute(PrintObject.ATTR_COPIES).toString(); break;
						case 12: rtnText = splf.getStringAttribute(PrintObject.ATTR_FORMTYPE); break;
						case 13: rtnText = model.getCreateDate(); break;
						case 14: rtnText = model.getCreateTime(); break;
						default: rtnText = ""; //$NON-NLS-1$
					}
					if (rtnText == null) rtnText = ""; //$NON-NLS-1$
					return rtnText;
				} catch (Exception e) {}
			}
			return "";  //$NON-NLS-1$
		}
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
	}
	
	private static class SpooledFilesSorter extends ViewerSorter {
		private boolean reversed = false;
		private int columnNumber;
		private static final int NUM_COLUMNS = 15;
	// column headings:	"Type","Link"
	private static final int[][] SORT_ORDERS_BY_COLUMN = {
		{0, 3, 10, 13, 14, 1, 2, 4, 5, 6, 7, 8, 9, 11, 12},	/* file */ 
		{1, 3, 10, 13, 14, 0, 2, 4, 5, 6, 7, 8, 9, 11, 12},	/* queue */ 
		{2, 3, 10, 13, 14, 0, 1, 4, 5, 6, 7, 8, 9, 11, 12},	/* data */ 
		{3, 10, 13, 14, 0, 1, 2, 4, 5, 6, 7, 8, 9, 11, 12},	/* sts */ 
		{4, 3, 10, 13, 14, 0, 1, 2, 5, 6, 7, 8, 9, 11, 12},	/* pages */ 
		{5, 3, 10, 13, 14, 0, 1, 2, 4, 6, 7, 8, 9, 11, 12},	/* current page */ 
		{6, 3, 10, 13, 14, 0, 1, 2, 4, 5, 7, 8, 9, 11, 12},	/* user */ 
		{7, 8, 3, 10, 13, 14, 0, 1, 2, 4, 5, 6, 9, 11, 12},	/* job */ 
		{8, 7, 3, 10, 13, 14, 0, 1, 2, 4, 5, 6, 9, 11, 12},	/* job nbr */
		{9, 3, 10, 13, 14, 0, 1, 2, 4, 5, 6, 7, 8, 11, 12},	/* file nbr */  
		{10, 3, 13, 14, 0, 1, 2, 4, 5, 6, 7, 8, 9, 11, 12},	/* pty */ 
		{11, 3, 10, 13, 14, 0, 1, 2, 4, 5, 6, 7, 8, 9, 12},	/* copies */ 
		{12, 3, 10, 13, 14, 0, 1, 2, 4, 5, 6, 7, 8, 9, 11},	/* form type */ 
		{13, 14, 3, 10, 0, 1, 2, 4, 5, 6, 7, 8, 9, 11, 12},	/* create date */ 
		{14, 13, 3, 10, 0, 1, 2, 4, 5, 6, 7, 8, 9, 11, 12}	/* create time */ 
	};
		
		public SpooledFilesSorter(int columnNumber) {
			this.columnNumber = columnNumber;
		}
		
		public int compare(Viewer viewer, Object e1, Object e2) {
			SpooledFileModel m1 = (SpooledFileModel)e1;
			SpooledFileModel m2 = (SpooledFileModel)e2;
			int[] columnSortOrder = SORT_ORDERS_BY_COLUMN[columnNumber];
			int result = 0;
			for (int i = 0; i < NUM_COLUMNS; ++i) {
				result = compareColumnValue(columnSortOrder[i], m1, m2);
				if (result != 0)
					break;
			}
			if (reversed)
				result = -result;
			return result;
		}
		
		private int compareColumnValue(int columnNumber,  SpooledFileModel m1, SpooledFileModel m2) {
			try {
				switch (columnNumber) {
					case 0: /* file */
						if (m1.getSpooledFile().getName().equals(m2.getSpooledFile().getName()))
						return 0;
						return collator.compare(m1.getSpooledFile().getName(), m2.getSpooledFile().getName());
					case 1: /* queue */
						if (m1.getOutputQueue().equals(m2.getOutputQueue()))
						return 0;
						return collator.compare(m1.getOutputQueue(), m2.getOutputQueue());
					case 2: /* data */
						if (m1.getSpooledFile().getStringAttribute(PrintObject.ATTR_USERDATA).equals(m2.getSpooledFile().getStringAttribute(PrintObject.ATTR_USERDATA)))
						return 0;
						return collator.compare(m1.getSpooledFile().getStringAttribute(PrintObject.ATTR_USERDATA), m2.getSpooledFile().getStringAttribute(PrintObject.ATTR_USERDATA));
					case 3: /* sts */
						if (m1.getSpooledFile().getStringAttribute(PrintObject.ATTR_SPLFSTATUS).equals(m2.getSpooledFile().getStringAttribute(PrintObject.ATTR_SPLFSTATUS)))
						return 0;
						return collator.compare(m1.getSpooledFile().getStringAttribute(PrintObject.ATTR_SPLFSTATUS), m2.getSpooledFile().getStringAttribute(PrintObject.ATTR_SPLFSTATUS));
					case 4: /* pages */
						if (m1.getSpooledFile().getIntegerAttribute(PrintObject.ATTR_PAGES).intValue() == m2.getSpooledFile().getIntegerAttribute(PrintObject.ATTR_PAGES).intValue())
							return 0;
						if (m1.getSpooledFile().getIntegerAttribute(PrintObject.ATTR_PAGES).intValue() > m2.getSpooledFile().getIntegerAttribute(PrintObject.ATTR_PAGES).intValue())
							return 1;
						else 
							return -1;
					case 5: /* current page */
						if (m1.getSpooledFile().getIntegerAttribute(PrintObject.ATTR_CURPAGE).intValue() == m2.getSpooledFile().getIntegerAttribute(PrintObject.ATTR_CURPAGE).intValue())
							return 0;
						if (m1.getSpooledFile().getIntegerAttribute(PrintObject.ATTR_CURPAGE).intValue() > m2.getSpooledFile().getIntegerAttribute(PrintObject.ATTR_CURPAGE).intValue())
							return 1;
						else 
							return -1;
					case 6: /* job user */
						if (m1.getSpooledFile().getJobUser().equals(m2.getSpooledFile().getJobUser()))
						return 0;
						return collator.compare(m1.getSpooledFile().getJobUser(), m2.getSpooledFile().getJobUser());
					case 7: /* job */
						if (m1.getSpooledFile().getJobName().equals(m2.getSpooledFile().getJobName()))
						return 0;
						return collator.compare(m1.getSpooledFile().getJobName(), m2.getSpooledFile().getJobName());
					case 8: /* job nbr */
						if (m1.getSpooledFile().getJobNumber().equals(m2.getSpooledFile().getJobNumber()))
						return 0;
						return collator.compare(m1.getSpooledFile().getJobNumber(), m2.getSpooledFile().getJobNumber());
					case 9: /* splf nbr */
						if (m1.getSpooledFile().getNumber() == m2.getSpooledFile().getNumber())
							return 0;
						if (m1.getSpooledFile().getNumber() > m2.getSpooledFile().getNumber())
							return 1;
						else 
							return -1;
					case 10: /* pty */
						if (m1.getSpooledFile().getStringAttribute(PrintObject.ATTR_OUTPTY).equals(m2.getSpooledFile().getStringAttribute(PrintObject.ATTR_OUTPTY)))
						return 0;
						return collator.compare(m1.getSpooledFile().getStringAttribute(PrintObject.ATTR_OUTPTY), m2.getSpooledFile().getStringAttribute(PrintObject.ATTR_OUTPTY));
					case 11: /* copies */
						if (m1.getSpooledFile().getIntegerAttribute(PrintObject.ATTR_COPIES).intValue() == m2.getSpooledFile().getIntegerAttribute(PrintObject.ATTR_COPIES).intValue())
							return 0;
						if (m1.getSpooledFile().getIntegerAttribute(PrintObject.ATTR_COPIES).intValue() > m2.getSpooledFile().getIntegerAttribute(PrintObject.ATTR_COPIES).intValue())
							return 1;
						else 
							return -1;
					case 12: /* formtype */
						if (m1.getSpooledFile().getStringAttribute(PrintObject.ATTR_FORMTYPE).equals(m2.getSpooledFile().getStringAttribute(PrintObject.ATTR_FORMTYPE)))
						return 0;
						return collator.compare(m1.getSpooledFile().getStringAttribute(PrintObject.ATTR_FORMTYPE), m2.getSpooledFile().getStringAttribute(PrintObject.ATTR_FORMTYPE));
					case 13: /* date */
						if (m1.getSpooledFile().getStringAttribute(PrintObject.ATTR_DATE).equals(m2.getSpooledFile().getStringAttribute(PrintObject.ATTR_DATE)))
						return 0;
						return collator.compare(m1.getSpooledFile().getStringAttribute(PrintObject.ATTR_DATE), m2.getSpooledFile().getStringAttribute(PrintObject.ATTR_DATE));
					case 14: /* time */
						if (m1.getSpooledFile().getStringAttribute(PrintObject.ATTR_TIME).equals(m2.getSpooledFile().getStringAttribute(PrintObject.ATTR_TIME)))
						return 0;
						return collator.compare(m1.getSpooledFile().getStringAttribute(PrintObject.ATTR_TIME), m2.getSpooledFile().getStringAttribute(PrintObject.ATTR_TIME));
					default:
						return 0;
				}
			} catch (Exception e) { return 0; }
		}
	
		public int getColumnNumber() {
			return columnNumber;
		}

		public boolean isReversed() {
			return reversed;
		}

		public void setReversed(boolean newReversed) {
			reversed = newReversed;
		}

	}

	public static SpooledFileFactory getSpooledFileFactory() {
		return spooledFileFactory;
	}

	public static void setSpooledFileFactory(SpooledFileFactory spooledFileFactory) {
		SpooledFilesTableViewer.spooledFileFactory = spooledFileFactory;
	}
	
	private SpooledFileFactory getDefaultSpooledFileFactory() {
		return null;
	}
	
	private SpooledFileFilter getDefaultSpooledFileFilter() {
		return null;
	}

	/**
	 * Returns the tableID.
	 * @return String
	 */
	public static String getTableID() {
		return tableID;
	}

	/**
	 * Returns the filter.
	 * @return SpooledFileFilter
	 */
	public static SpooledFileFilter getFilter() {
		return filter;
	}

	/**
	 * Sets the filter.
	 * @param filter The filter to set
	 */
	public static void setFilter(SpooledFileFilter filter) {
		SpooledFilesTableViewer.filter = filter;
	}

}
