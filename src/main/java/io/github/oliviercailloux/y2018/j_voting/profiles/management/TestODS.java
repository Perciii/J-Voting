package io.github.oliviercailloux.y2018.j_voting.profiles.management;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Cell;
import org.odftoolkit.simple.table.Column;
import org.odftoolkit.simple.table.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.oliviercailloux.y2018.j_voting.Alternative;
import io.github.oliviercailloux.y2018.j_voting.StrictPreference;
import io.github.oliviercailloux.y2018.j_voting.Voter;

/**
 * Simplified from <a href=
 * "https://incubator.apache.org/odftoolkit/simple/demo/demo9.html">odftoolkit
 * doc</a>.
 *
 * @author Olivier Cailloux, modified by Quentin Sauvage
 *
 */
public class TestODS {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestODS.class);

	public static void main(String[] args) throws Exception {
		new TestODS().generateSpreadsheetDocument();
	}

	@SuppressWarnings("unused")
	public void generateSpreadsheetDocument() throws Exception {
		try (InputStream inputStream = TestODS.class.getResourceAsStream("demo9_data.ods");
				SpreadsheetDocument spreadsheetDoc = SpreadsheetDocument.loadDocument(inputStream)) {
			List<Table> tables = spreadsheetDoc.getTableList();
			for (Table table : tables) {
				List<Voter> voters = new ArrayList<>();
				for (Column column : table.getColumnList()) {
					List<Alternative> alternatives = new ArrayList<>();
					String firstCellText = column.getCellByIndex(0).getDisplayText();
					voters.add(new Voter(Integer.parseInt(
							firstCellText.substring(firstCellText.length() - 1, firstCellText.indexOf(" ")))));
					for (int alt = Integer.parseInt(column.getCellByIndex(1).getDisplayText()); alt < column
							.getCellCount(); alt++) {
						alternatives.add(new Alternative(alt));
					}
					StrictPreference pref = new StrictPreference(alternatives);
				}
			}
			String preference = spreadsheetDoc.getTableByName("B").getCellRangeByPosition("A2", "end").toString();
			// what does toString() return ?

			Cell positionCell = spreadsheetDoc.getTableByName("B").getCellByPosition("E1");
			LOGGER.info("Found: {}.", positionCell.getDisplayText());
			positionCell.setStringValue("ploum");
			spreadsheetDoc.save("demo9s.ods");
		}
	}
}