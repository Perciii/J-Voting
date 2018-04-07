package io.github.oliviercailloux.y2018.j_voting;

import java.io.*;

public class WriteProfile {

	/**
	 * Writes a StrictProfile into a SOC file.
	 * @param profile
	 * @throws IOException
	 */
	public static void writeSOC(StrictProfile profile) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter("profil.soc"));
        PrintWriter pWriter = new PrintWriter(bw);
        pWriter.print(profile);
        pWriter.close() ;
	}
	
	public static void main(String[] args) {
		StrictProfile socprofile=StrictProfile.fromSOCorSOI(new File(WriteProfile.class.getResource("/Files/profil.soc").getFile());
		System.out.println("SOC profile :");
		System.out.println(socprofile.toSOC());
	}
}
