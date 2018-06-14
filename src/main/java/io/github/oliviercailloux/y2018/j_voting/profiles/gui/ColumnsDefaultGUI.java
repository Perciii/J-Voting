package io.github.oliviercailloux.y2018.j_voting.profiles.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.oliviercailloux.y2018.j_voting.Voter;
import io.github.oliviercailloux.y2018.j_voting.profiles.StrictProfile;
import io.github.oliviercailloux.y2018.j_voting.profiles.management.ProfileBuilder;

public class ColumnsDefaultGUI extends ProfileDefaultGUI{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ColumnsDefaultGUI.class.getName());
	static TreeItem itemBeingDragged;
	
	@Override
	public List<String> createColumns() {
		LOGGER.debug("createColumns :");
		StrictProfile strictProfile = profileBuilder.createStrictProfile();//if profile get from file is SOC, create a StrictProfile from it

		Iterable<Voter> allVoters = strictProfile.getAllVoters(); //get voters from profile
		
		int i = 0; 
		
		//COLUMNS
		List<String> titles = new ArrayList<>();
		for(Voter v : allVoters){
			titles.add("Voter " + v.getId());
			i++;
		}
		for (i = 0 ; i < titles.size() ; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText(titles.get(i));
		}
		
		
		return titles;
	}
	
	public ProfileBuilder tableDisplay(String[] args) throws IOException {
		final Display display = new Display ();
		
		Shell shell = new Shell (display);
		shell.setBounds (10,10,400,400);
		
		final Tree tree = new Tree(shell, SWT.NONE);
		tree.setBounds(10,10,350,350);
		tree.setHeaderVisible(true);
		
		ColumnsDefaultGUI cdg = new ColumnsDefaultGUI();
		cdg.createColumns();
		cdg.populateRows();
		
		
		
		tree.addListener(SWT.DragDetect, new Listener() {
			@Override
			public void handleEvent(Event event) {
				TreeItem item = tree.getItem(new Point(event.x, event.y));
				if (item == null) return;
				itemBeingDragged = item;
			}
		});
		
		tree.addListener(SWT.MouseMove, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (itemBeingDragged == null) return;
				TreeItem item = tree.getItem(new Point(event.x, event.y));
				tree.setInsertMark(item, true);
			}
		});
		
		tree.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (itemBeingDragged == null) return;
				TreeItem item = tree.getItem(new Point(event.x, event.y));
				
				if (item != null && item != itemBeingDragged) {
					/* determine insertion index */
					TreeItem[] items = tree.getItems();
					int index = -1;
					for (int i = 0; i < items.length; i++) {
						if (items[i] == item) {
							index = i;
							break;
						}
					}
					if (index != -1) { /* always true in this trivial example */
						TreeItem newItem = new TreeItem(tree, SWT.NONE, index);
						for (int i = 0; i < COLUMNCOUNT; i++) {
							newItem.setText(i, itemBeingDragged.getText(i));
						}
						itemBeingDragged.dispose();
						tree.setSelection(new TreeItem[] {newItem});
					}
				}
				tree.setInsertMark(null, false);
				itemBeingDragged = null;
			}
		});
		shell.open();
		while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose ();
	}

}