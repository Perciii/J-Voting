package io.github.oliviercailloux.y2018.j_voting.profiles.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.oliviercailloux.y2018.j_voting.Voter;
import io.github.oliviercailloux.y2018.j_voting.profiles.StrictProfileI;

public class SOIColumnsGUI extends ColumnsDefaultGUI {
	private static final Logger LOGGER = LoggerFactory.getLogger(SOIColumnsGUI.class.getName());

	@Override
	public void createColumns() {
		LOGGER.debug("createColumns :");
		StrictProfileI strictProfile = profileBuilder.createStrictProfileI();
		// if profile get from file is SOI, create a StrictProfile from it

		Iterable<Voter> allVoters = strictProfile.getAllVoters(); // get voters from profile

		// COLUMNS
		List<String> titles = new ArrayList<>();
		for (Voter v : allVoters) {
			titles.add("Voter " + v.getId());
		}
		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}
	}

	@Override
	public void populateRows() {
		LOGGER.debug("populateRows");
		StrictProfileI strictProfile = profileBuilder.createStrictProfileI();
		// ROWS
		List<String> alternatives = new ArrayList<>();

		int nbAlternatives = strictProfile.getMaxSizeOfPreference();

		for (int i = 0; i < nbAlternatives; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			// create a row with ith alternatives
			item.setText(strictProfile.getIthAlternativesAsStrings(i).toArray(new String[nbAlternatives]));
			alternatives.clear(); // empty the list
		}
	}

	public static void main(String[] args) throws IOException {
		SOIColumnsGUI soiColumns = new SOIColumnsGUI();
		soiColumns.displayProfileWindow(args);
	}

}
