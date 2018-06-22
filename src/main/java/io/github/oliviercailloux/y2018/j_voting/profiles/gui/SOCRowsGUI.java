package io.github.oliviercailloux.y2018.j_voting.profiles.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.oliviercailloux.y2018.j_voting.Alternative;
import io.github.oliviercailloux.y2018.j_voting.Preference;
import io.github.oliviercailloux.y2018.j_voting.Voter;
import io.github.oliviercailloux.y2018.j_voting.profiles.StrictProfile;

public class SOCRowsGUI extends ProfileDefaultGUI {
	private static final Logger LOGGER = LoggerFactory.getLogger(SOCRowsGUI.class.getName());

	@Override
	public void createColumns() {
		LOGGER.debug("createColumns :");
		StrictProfile strictProfile = profileBuilder.createStrictProfile();

		// if profile get from file is SOC, create a StrictProfile from it

		// COLUMNS
		List<String> titles = new ArrayList<>();
		Iterable<Alternative> allAlternatives = strictProfile.getAlternatives();
		titles.add("Voters");
		for (Alternative a : allAlternatives) {
			titles.add("Alternative " + a);
		}
		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}
	}

	@Override
	public void checkRadioButton() {
		LOGGER.debug("checkRadioButtons");
		columnsButton.setSelection(false);
		rowsButton.setSelection(true);
		wrapButton.setSelection(false);
	}

	@Override
	public void populateRows() {
		LOGGER.debug("populateRows :");
		StrictProfile strictProfile = profileBuilder.createStrictProfile();

		// ROWS
		Iterable<Voter> allVoters = strictProfile.getAllVoters(); // get voters from profile

		int nbAlternatives = strictProfile.getNbAlternatives();

		List<String> line = new ArrayList<>();
		for (Voter v : allVoters) {

			line.add("Voter " + v.getId());
			Preference pref = strictProfile.getPreference(v);
			Iterable<Alternative> allPref = Preference.toAlternativeSet(pref.getPreferencesNonStrict());
			for (Alternative a : allPref) {
				line.add(a.toString());
			}

			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(line.toArray(new String[nbAlternatives + 1])); // create a row with ith alternatives
			line.clear(); // empty the list

		}

	}

	public static void main(String[] args) throws IOException {
		SOCRowsGUI socRows = new SOCRowsGUI();
		socRows.displayProfileWindow(args);
	}

}
