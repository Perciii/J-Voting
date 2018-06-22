package io.github.oliviercailloux.y2018.j_voting.profiles.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class AdaptedTest {
	static TreeItem itemBeingDragged;
	public static void main (String [] args) {
		final int COLUMNCOUNT = 4;
		final Display display = new Display ();
		
		Shell shell = new Shell (display);
		shell.setBounds (10,10,400,400);
		
		final Tree tree = new Tree(shell, SWT.NONE);
		tree.setBounds(10,10,350,350);
		tree.setHeaderVisible(true);
		
		/* create the columns */
		int columnWidth = tree.getClientArea().width / COLUMNCOUNT;
		
		for (int i = 0; i < COLUMNCOUNT; i++) {
			TreeColumn column = new TreeColumn(tree, SWT.NONE);
			column.setText("Col " + i);
			column.setWidth(columnWidth);
		}
		/* end create the columns */
		
		for (int i = 0; i < 9; i++) { /* create the items */
			TreeItem item = new TreeItem(tree, SWT.NONE);
			for (int j = 0; j < COLUMNCOUNT; j++) {
				item.setText(j, "item" + i + "-" + j);
			}
		}
		
		tree.addListener(SWT.DragDetect, new Listener() {
			@Override
			public void handleEvent(Event event) {
				TreeItem item = tree.getItem(new Point(event.x, event.y));
				if (item == null) return;
				itemBeingDragged = item;
			}
		});
		
		TreeItem[] items = tree.getItems();
		for (TreeItem item : items) {
		System.out.println("item : " +  item);
		}
		System.out.println("nbcolumns : " +  tree.getColumnCount());
		System.out.println("nbItems : " +  tree.getItemCount());
		
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
					int index = -1;
					for (int i = 0; i < tree.getItems().length; i++) {
						if (items[i] == item) {
							System.out.println(items[i].getText());
							index = i;
							break;
						}
					}
					if (index != -1) {
						TreeItem newItem = new TreeItem(tree, SWT.NONE);
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