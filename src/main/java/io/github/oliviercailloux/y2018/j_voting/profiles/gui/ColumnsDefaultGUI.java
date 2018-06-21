package io.github.oliviercailloux.y2018.j_voting.profiles.gui;

import java.io.*;
import java.util.*;
import java.util.List;

import javax.swing.text.TableView;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerRow;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.slf4j.*;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;

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
	protected ViewerCell cellBeingDragged = tableViewer.getCell(new Point(0, 0));
	protected int destinationX = 0;
	protected int destinationY = 0;

	@Override
	public void tableDisplay(String fileName) {
		LOGGER.debug("tableDisplay");

		// table layout handling
		mainShell.setLayout(new GridLayout());
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		table.setLayoutData(data);

		// createColumns();

		List<String> columnTitles = createColumns();
		populateRows();
		handleDragAndDrop();
		checkRadioButton();

		for (int i = 0; i < columnTitles.size(); i++) {
			table.getColumn(i).pack(); // resize automatically the column
		}

		save = new Button(mainShell, SWT.PUSH);
		save.setText("Save");
		save.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				save(fileName);
			}
		});
	}

	/**
	 * Implements dragging and dropping a cell at a time within the same column.<br>
	 * When moving it up, the cell it is dropped on and those below will go down one
	 * cell. <br>
	 * When moving it down, the cell it is dropped on and those above will go
	 * up one cell.
	 */
	public void handleDragAndDrop() {
		LOGGER.debug("handleDragAndDrop :");
		table.addListener(SWT.MouseDown, new Listener() {
			@Override
			public void handleEvent(Event event) {
				ViewerCell viewerCell = tableViewer.getCell(new Point(event.x, event.y));
				sourceX = event.x;
				sourceY = event.y;
				if (viewerCell == null)
					return;
				cellBeingDragged = viewerCell;
			}
		});

		table.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (cellBeingDragged == null)
					return;
				ViewerCell destinationCell = tableViewer.getCell(new Point(event.x, event.y));

				if (destinationCell != null && destinationCell != cellBeingDragged) {
					// if moving from one voter to another
					if (cellBeingDragged.getColumnIndex() != destinationCell.getColumnIndex()) {
						MessageBox messageBox = new MessageBox(mainShell, SWT.OK);
						messageBox.setText("Warning");
						messageBox.setMessage("You can't move alternatives between voters !");
						messageBox.open();

					} else {// if moving cell within the same column
						ViewerCell currentCell = tableViewer.getCell(new Point(sourceX, sourceY));
						String cellBeingDraggedText = cellBeingDragged.getText();

						// if source is over destination in the table
						if (cellBeingDragged.getBounds().y < destinationCell.getBounds().y) {
							while (currentCell.getBounds().y != destinationCell.getBounds().y) {
								LOGGER.debug("Current cell : {} replaced by {}", currentCell.getText(),
										currentCell.getNeighbor(ViewerCell.BELOW, false).getText());
								currentCell.setText(currentCell.getNeighbor(ViewerCell.BELOW, false).getText());
								currentCell = currentCell.getNeighbor(ViewerCell.BELOW, false);
							}
							destinationCell.setText(cellBeingDraggedText);

						} else {// if source is underneath destination in the table
							while (currentCell.getBounds().y != destinationCell.getBounds().y) {
								LOGGER.debug("Current cell : {} replaced by {}", currentCell.getText(),
										currentCell.getNeighbor(ViewerCell.ABOVE, false).getText());
								currentCell.setText(currentCell.getNeighbor(ViewerCell.ABOVE, false).getText());
								currentCell = currentCell.getNeighbor(ViewerCell.ABOVE, false);
							}
							destinationCell.setText(cellBeingDraggedText);
						}
					}
				}
			}
		});
	}

	/**
	 * Saves the changes to the file containing the profile.
	 * 
	 * @param outputFile
	 */
	@Override
	public void save(String outputFile) {
		LOGGER.debug("save :");
		Preconditions.checkNotNull(outputFile);
		File file = new File(outputFile);

		// soc tests etc

		int columnModified = cellBeingDragged.getColumnIndex();

		String newPrefString = table.getItem(0).getText(columnModified);
		for (TableItem item : Iterables.skip(Arrays.asList(table.getItems()), 1)) {
			newPrefString += "," + item.getText(columnModified);
		}

		voterToModify = columnModified + 1;
		
		Voter voter = new Voter(voterToModify);
		StrictPreference newPref = new ReadProfile().createStrictPreferenceFrom(newPrefString);
		LOGGER.debug("New preference for voter v {} : {}", voter, newPref);
		// change preference for this Voter in global ProfileBuilder
		profileBuilder.addVote(voter, newPref);

		try (OutputStream outputStream = new FileOutputStream(file)) {
			String fileExtension = file.toString().substring(file.toString().length() - 3);
			if (fileExtension.equals("soc")) {
				StrictProfile sp = profileBuilder.createStrictProfile();
				sp.writeToSOC(outputStream);
			} else { // fileExtension == "soi"
				StrictProfileI spi = profileBuilder.createStrictProfileI();
				spi.writeToSOI(outputStream);
			}
		} catch (IOException ioe) {
			MessageBox dialog = new MessageBox(mainShell, SWT.ICON_QUESTION | SWT.OK);
			dialog.setText("IOException");
			dialog.setMessage("Error when opening Stream : " + ioe);
			dialog.open();
		}
	}
}
