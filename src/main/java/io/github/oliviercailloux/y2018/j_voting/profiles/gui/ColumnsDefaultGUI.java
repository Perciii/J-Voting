package io.github.oliviercailloux.y2018.j_voting.profiles.gui;

import java.io.*;
import java.util.*;
import java.util.List;

import javax.swing.text.TableView;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.slf4j.*;

import com.google.common.base.Preconditions;

import io.github.oliviercailloux.y2018.j_voting.*;
import io.github.oliviercailloux.y2018.j_voting.profiles.*;
import io.github.oliviercailloux.y2018.j_voting.profiles.management.*;

/**
 * Generalization of profile displaying GUIs
 */
public class ColumnsDefaultGUI extends ProfileDefaultGUI {
	private static final Logger LOGGER = LoggerFactory.getLogger(ColumnsDefaultGUI.class.getName());

	protected int sourceX = 0;
	protected int sourceY = 0;
	protected ViewerCell cellBeingDragged = tableViewer.getCell(new Point(sourceX, sourceY));
	protected int destinationX = 0;
	protected int destinationY = 0;
	
	/**
	 * Displays the profile with the edit button
	 * @param args
	 * @return the profile builder made from the profile
	 * @throws IOException
	 */
	@Override
	public ProfileBuilder tableDisplay(String[] args) throws IOException {
		LOGGER.debug("tableDisplay");
		Preconditions.checkNotNull(args[0]);

		String arg = args[0];//arg is the file path
		ReadProfile rp = new ReadProfile();
		try(FileInputStream is = new FileInputStream(arg)){
			ProfileI profileI = rp.createProfileFromStream(is);
			profileBuilder = new ProfileBuilder(profileI);

			//table layout handling
			mainShell.setLayout(new GridLayout());
			table.setLinesVisible (true);
			table.setHeaderVisible (true);
			GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
			table.setLayoutData(data);
			
			displayRadioButtons(args);

			createColumns();
	
			populateRows();
			handleDragAndDrop();
		
			List<String> columnTitles = createColumns();
			
			for (int i = 0 ; i < columnTitles.size() ; i++) {
				table.getColumn(i).pack(); // resize automatically the column
			}
			
			edit.setText("Edit");
			edit.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					editStrictPreference(arg); //open edit modal
				}
			});
			
			mainShell.setText("Edit Profile");
			mainShell.pack();
			mainShell.open();

			while (!display.isDisposed()) {
				if (!display.readAndDispatch()) display.sleep();
			}
			
			return profileBuilder; // return profileBuilder containing the profile get in the read file
		}
	}
	
	
	public void handleDragAndDrop() {
		table.addListener(SWT.DragDetect, new Listener() {
			@Override
			public void handleEvent(Event event) {
				//TreeItem item = tree.getItem(new Point(event.x, event.y));
				ViewerCell viewerCell = tableViewer.getCell(new Point(event.x,event.y));
				sourceX = event.x;
				sourceY = event.y;
				if (viewerCell == null) return;
				cellBeingDragged = viewerCell;
			}
		});
		
		table.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (cellBeingDragged == null) return;
				ViewerCell destinationCell = tableViewer.getCell(new Point(event.x,event.y));
				
				if (destinationCell != null && destinationCell != cellBeingDragged) {
					if(cellBeingDragged.getColumnIndex() != destinationCell.getColumnIndex()) {//if moving from one voter to another
						MessageBox messageBox = new MessageBox(mainShell, SWT.OK);
				        messageBox.setText("Warning");
				        messageBox.setMessage("You can't move alternatives between voters !");
				        messageBox.open();
					} else {
						String destinationCellText = destinationCell.getText();
						destinationCell.setText(cellBeingDragged.getText());
						cellBeingDragged.setText(destinationCellText);
					}
				}
				cellBeingDragged = null;
			}
		});
	}
}

