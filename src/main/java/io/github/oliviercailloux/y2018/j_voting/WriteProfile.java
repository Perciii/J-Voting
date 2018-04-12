package io.github.oliviercailloux.y2018.j_voting;

import java.io.*;

public class WriteProfile {

	/**
	 * Writes a StrictProfile into a SOC file.
	 * @param profile
	 * @throws IOException
	 */
	public static void writeSOC(StrictProfile profile) throws IOException {
		try(BufferedWriter bw = new BufferedWriter(new FileWriter("profil.soc"))){
	        PrintWriter pWriter = new PrintWriter(bw);
	        pWriter.print(profile);
		}
	}
}
