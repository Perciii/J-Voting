package io.github.oliviercailloux.y2018.j_voting.profiles.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TableColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import io.github.oliviercailloux.y2018.j_voting.profiles.ProfileI;
import io.github.oliviercailloux.y2018.j_voting.profiles.StrictProfile;
import io.github.oliviercailloux.y2018.j_voting.profiles.StrictProfileI;
import io.github.oliviercailloux.y2018.j_voting.profiles.management.ProfileBuilder;
import io.github.oliviercailloux.y2018.j_voting.profiles.management.ReadProfile;

/**
 * Generalization of profile displaying GUIs
 */
public class ColumnsDefaultGUI extends ProfileDefaultGUI {
	private static final Logger LOGGER = LoggerFactory.getLogger(ColumnsDefaultGUI.class.getName());

	protected int sourceX = 0;
	protected int sourceY = 0;
	protected ViewerCell cellBeingDragged = tableViewer.getCell(new Point(0, 0));
	protected static Button saveButton;
	protected int destinationX = 0;
	protected int destinationY = 0;

	/**
	 * Displays the window : the table containing the profile as well as the
	 * buttons.
	 * 
	 * @param args
	 *            not <code>null</code>
	 * @throws IOException
	 */
	@Override
	public void displayProfileWindow(String[] args) throws IOException {
		LOGGER.debug("displayProfileWindow");
		Preconditions.checkNotNull(args);
		Preconditions.checkNotNull(args[0]);

		String arg = args[0];// arg is the file path
		ReadProfile rp = new ReadProfile();
		try (FileInputStream is = new FileInputStream(arg)) {
			ProfileI profileI = rp.createProfileFromStream(is);
			profileBuilder = new ProfileBuilder(profileI);

			displayRadioButtons(args);

			saveButton = new Button(mainShell, SWT.PUSH);
			saveButton.setText("Save");

			GridData gridData = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
			gridData.horizontalSpan = 1;
			saveButton.setLayoutData(gridData);

			saveButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					save(arg);
				}
			});

			saveButton.setVisible(true);
			GridData data = (GridData) saveButton.getLayoutData();
			data.exclude = false;

			tableDisplay();

			mainShell.setText("Edit Profile");
			mainShell.pack();
			mainShell.open();

			while (!display.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		}
	}

	@Override
	public void tableDisplay() {
		LOGGER.debug("tableDisplay");

		// table layout handling
		mainShell.setLayout(new GridLayout());
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		table.setLayoutData(data);

		createColumns();
		populateRows();
		handleDragAndDrop();
		checkRadioButton();

		TableColumn[] tableColumns = table.getColumns();
		for (TableColumn tableColumn : tableColumns) {
			tableColumn.pack();
		}
	}

	/**
	 * Implements dragging and dropping a cell at a time within the same column.<br>
	 * When moving it up, the cell it is dropped on and those below will go down one
	 * cell. <br>
	 * When moving it down, the cell it is dropped on and those above will go up one
	 * cell.
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
	 *            not <code>null</code>
	 */
	@Override
	public void save(String outputFile) {
		LOGGER.debug("save :");
		Preconditions.checkNotNull(outputFile);
		File file = new File(outputFile);

		ProfileBuilder pb = new ProfileBuilder(new ReadProfile().createProfileFromColumnsTable(table));

		try (OutputStream outputStream = new FileOutputStream(file)) {
			String fileExtension = file.toString().substring(file.toString().length() - 3);
			if (fileExtension.equals("soc")) {
				StrictProfile sp = pb.createStrictProfile();
				sp.writeToSOC(outputStream);
			} else { // fileExtension == "soi"
				StrictProfileI spi = pb.createStrictProfileI();
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
