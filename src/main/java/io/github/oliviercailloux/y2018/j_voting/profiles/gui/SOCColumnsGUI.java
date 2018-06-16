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

public class SOCColumnsGUI extends ProfileDefaultGUI{
	private static final Logger LOGGER = LoggerFactory.getLogger(SOCColumnsGUI.class.getName());
	
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
	
	@Override
	public void populateRows() {
		LOGGER.debug("populateRows :");
		//ROWS
		StrictProfile strictProfile = profileBuilder.createStrictProfile();
		List<String> alternatives = new ArrayList<>();
		
		int nbAlternatives = strictProfile.getNbAlternatives();

		for(int i = 0 ; i < nbAlternatives ; i++){
			List <Alternative> ithAlternatives = strictProfile.getIthAlternatives(i); // get ith alternative of each voter
			for(Alternative alt : ithAlternatives) {
				alternatives.add(alt.toString()); // convert alternatives in the list to strings
			}

			TableItem item = new TableItem (table, SWT.NONE);
			item.setText(alternatives.toArray(new String[nbAlternatives]));	// create a row with ith alternatives
			alternatives.clear(); // empty the list
		}
	}

	public static void main (String [] args) throws IOException {
		SOCColumnsGUI socColumns = new SOCColumnsGUI();
		profileBuilder = socColumns.tableDisplay(args);
	}

}

