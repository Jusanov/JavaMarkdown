package net.jusanov.javamd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class JavaMarkdown {

	public static void main(String[] args) throws IOException {
		
		File mdDir = new File("Markdown/");
		File htmlDir = new File("HTML/");
		File[] mds = mdDir.listFiles();

		if (!mdDir.exists()) mdDir.mkdir();
		if (!htmlDir.exists()) htmlDir.mkdir();
		
		for (File md : mds) {
			
			System.out.println(md.getPath());
			
			File htmlFile = new File(md.getPath().replace("Markdown", "HTML").replace(".md", ".html"));
			
			if (!md.isDirectory()) {
				
				// Readers & Writers
				BufferedReader markdown = new BufferedReader(new InputStreamReader(new FileInputStream(md)));
				BufferedWriter htmlOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlFile)));
				StringBuilder html = new StringBuilder();
				
				// Line & Status info
				String line;
				boolean p = false;
				
				while ((line = markdown.readLine()) != null) {
					
					if (line.startsWith("#")) {
						
						int h = 0;
						
						for (char c : line.toCharArray()) if ((c + "").equalsIgnoreCase("#")) h++; else break;
						
						html.append("<h" + h + ">");
						html.append(line.substring(h));
						html.append("</h" + h + ">");
						
					} else if (line.startsWith("---")) html.append("<hr />");
					else if(!line.isEmpty()) {
						
						if (p == false) {
							p = true;
							html.append("<p>");
						}
						
						html.append(line);
						
					} else {
						if (p == true) {
							p = false;
							html.append("</p>\n");
						}
					}
					html.append("\n");
					
				}
				
				if (p == true) {
					p = false;
					html.append("</p>\n");
				}
				
				htmlOut.write(html.toString());
				
				markdown.close();
				htmlOut.close();
				
			}
			
		}

	}

}
