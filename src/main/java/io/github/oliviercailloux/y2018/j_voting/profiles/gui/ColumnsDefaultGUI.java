package io.github.oliviercailloux.y2018.j_voting.profiles.gui;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import io.github.oliviercailloux.y2018.j_voting.Voter;
import io.github.oliviercailloux.y2018.j_voting.profiles.ProfileI;
import io.github.oliviercailloux.y2018.j_voting.profiles.StrictProfile;
import io.github.oliviercailloux.y2018.j_voting.profiles.management.ProfileBuilder;
import io.github.oliviercailloux.y2018.j_voting.profiles.management.ReadProfile;

/**
 * This class will be used as a generalization of Columns GUI
 * It implements a GUI as a Tree to allow dragging and dropping items inside itself
 */
public class ColumnsDefaultGUI extends ProfileDefaultGUI{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ColumnsDefaultGUI.class.getName());
	protected static TreeItem itemBeingDragged;
	protected Tree tree = new Tree(mainShell, SWT.NONE);
	protected int nbColumns = 0;
	
	@Override
	public ProfileBuilder tableDisplay(String[] args) throws IOException {
		LOGGER.debug("tableDisplay :");
		Preconditions.checkNotNull(args[0]);
		
		String arg = args[0];//arg is the file path
		ReadProfile rp = new ReadProfile();
		try(FileInputStream is = new FileInputStream(arg)){
			ProfileI profileI = rp.createProfileFromStream(is);
			profileBuilder = new ProfileBuilder(profileI);
			
			//table (tree) layout handling
			mainShell.setLayout(new TreeColumnLayout());
			tree.setLinesVisible(true);
			tree.setHeaderVisible(true);
			
			List<String> columnTitles = createColumns();
			nbColumns = columnTitles.size();
			
			for (int i = 0 ; i < nbColumns ; i++) {
				tree.getColumn(i).pack(); // resize automatically the column
			}
			
			populateRows();
			handleDragAndDrop();
			
			mainShell.open();
			while (!display.isDisposed()) {
				if (!display.readAndDispatch()) display.sleep();
			}
			
			return profileBuilder; // return profileBuilder containing the profile get in the read file
		}
	}
	
	public void handleDragAndDrop() {
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
						newItem.setText(itemBeingDragged.getText());
						itemBeingDragged.dispose();
						tree.setSelection(new TreeItem[] {newItem});
					}
				}
				tree.setInsertMark(null, false);
				itemBeingDragged = null;
			}
		});
	}
	
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
			TreeColumn column = new TreeColumn (tree, SWT.NONE);
			column.setText(titles.get(i));
		}
		
		
		return titles;
	}

}