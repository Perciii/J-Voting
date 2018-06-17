package io.github.oliviercailloux.y2018.j_voting.profiles.gui;

import java.io.*;
import java.util.*;
import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.slf4j.*;

import com.google.common.base.Preconditions;

import io.github.oliviercailloux.y2018.j_voting.*;
import io.github.oliviercailloux.y2018.j_voting.profiles.*;
import io.github.oliviercailloux.y2018.j_voting.profiles.management.*;

public class SOCWrappedColumnsGUI extends ColumnsDefaultGUI {
	private static final Logger LOGGER = LoggerFactory.getLogger(SOCWrappedColumnsGUI.class.getName());
	
	//TODO: change everything so that the GUI allows editing
	
	@Override
	public List<String> createColumns() {
		LOGGER.debug("createColumns :");
		StrictProfile strictProfile = profileBuilder.createStrictProfile();//if profile get from file is SOC, create a StrictProfile from it
		Set<Preference> uniquePreferences = strictProfile.getUniquePreferences();
		
		//COLUMNS
		List<String> titles = new ArrayList<>();
		for(Preference p : uniquePreferences){
			int nbVoters = strictProfile.getNbVoterForPreference(p);
			
			String voterOrVoters = (nbVoters > 1) ? " voters" : " voter";
			titles.add(nbVoters + voterOrVoters);
		}
		for (int i = 0 ; i < titles.size() ; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText(titles.get(i));
		}
		
		return titles;
	}

	@Override
	public void checkRadioButton() {
		LOGGER.debug("checkRadioButtons");
		columnsButton.setSelection(false);			
		rowsButton.setSelection(false);		
		wrapButton.setSelection(true);
	}
	
	@Override
	public void populateRows() {
		LOGGER.debug("populateRows :");
		//ROWS
		StrictProfile strictProfile = profileBuilder.createStrictProfile();
		List<String> alternatives = new ArrayList<>();
		
		int nbAlternatives = strictProfile.getNbAlternatives();//nb of rows

		for(int i = 0 ; i < nbAlternatives ; i++){
			List <Alternative> ithAlternatives = strictProfile.getIthAlternativesOfUniquePreferences(i); // get ith alternative of each voter
			for(Alternative alt : ithAlternatives) {
				alternatives.add(alt.toString()); // convert alternatives in the list to strings
			}

			TableItem item = new TableItem (table, SWT.NONE);
			item.setText(alternatives.toArray(new String[nbAlternatives]));	// create a row with ith alternatives
			alternatives.clear(); // empty the list
		}
	}
	
	public static void main (String [] args) throws IOException {
		SOCWrappedColumnsGUI socWrapped = new SOCWrappedColumnsGUI();
		profileBuilder = socWrapped.tableDisplay(args);
	}

}

