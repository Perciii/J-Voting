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

public class SOCRowsGUI extends ProfileDefaultGUI {
	private static final Logger LOGGER = LoggerFactory.getLogger(SOCRowsGUI.class.getName());
	
	@Override
	public List<String> createColumns() {
	LOGGER.debug("createColumns :");
		StrictProfile strictProfile = profileBuilder.createStrictProfile();//if profile get from file is SOC, create a StrictProfile from it
		
		int i = 0; 
		
		//COLUMNS
		
		List<String> titles = new ArrayList<>();
		titles.add("Voters");
		for(int a=1;a<=strictProfile.getNbAlternatives();a++ ){
			titles.add("Alternative " + a);
			i++;
		}
		for (i = 0 ; i < titles.size() ; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText(titles.get(i));
		}

		return titles;
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
		//ROWS
		StrictProfile strictProfile = profileBuilder.createStrictProfile();
		
		Iterable<Voter> allVoters = strictProfile.getAllVoters(); //get voters from profile
		
		int nbAlternatives = strictProfile.getNbAlternatives();

		List<String> line  = new ArrayList<>();
		for(Voter v : allVoters){

			line.add("Voter " + v.getId());
			Preference pref = strictProfile.getPreference(v);
			Iterable<Alternative> allPref = Preference.toAlternativeSet(pref.getPreferencesNonStrict());
			for(Alternative a : allPref){
				System.out.println(a);
				line.add(a.toString());
			}

			TableItem item = new TableItem (table, SWT.NONE);
			item.setText(line.toArray(new String[nbAlternatives+1]));	// create a row with ith alternatives
			line.clear(); // empty the list

		}
		
	}

	public static void main (String [] args) throws IOException {
		SOCRowsGUI socRows = new SOCRowsGUI();
		profileBuilder = socRows.tableDisplay(args);
	}

}

