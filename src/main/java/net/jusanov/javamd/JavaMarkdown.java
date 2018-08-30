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

	static boolean pa = false;
	static boolean it = false;
	static boolean bo = false;
	static boolean co = false;
	static boolean ol = false;
	static boolean ul = false;
	
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
				
				while ((line = markdown.readLine()) != null) {
					
					html.append(formatElements(line));
					
				}
				
				if (pa == true) {
					pa = false;
					html.append("</p>\n");
				}
				
				htmlOut.write(html.toString());
				
				markdown.close();
				htmlOut.close();
				
			}
			
		}

	}
	
	public static String formatElements(String line) {
		
		StringBuilder str = new StringBuilder();
		
		if (line.startsWith("#")) {
			
			int h = 0;
			
			for (char c : line.toCharArray()) if ((c + "").equalsIgnoreCase("#")) h++; else break;
			
			str.append("<h" + h + ">");
			str.append(line.substring(h));
			str.append("</h" + h + ">");
			
		} else if (line.startsWith("---")) str.append("<hr />");
		else if (line.startsWith("-") || line.startsWith("*")) {
			
			if (ul == false) {
				ul = true;
				str.append("<ul>\n");
			}
			if (line.charAt(1) == ' ') line = line.substring(2);
			else line = line.substring(1);
			
			str.append("<li>" + formatText(line) + "</li>");
			
		} else if (!line.isEmpty() && line.charAt(1) == '.') {
			
			// The try/catch block is intended to make sure it is an actual numbered list
			try {
				
				int num = Integer.parseInt(line.charAt(0) + "");
				
				if (ol == false) {
					ol = true;
					str.append("<ol>\n");
				}
				if (line.charAt(2) == ' ') line = line.substring(3);
				else line = line.substring(2);
				
				str.append("<li>" + formatText(line) + "</li>");
				
			} catch (NumberFormatException e) {
				
			}
			
		} else if(!line.isEmpty()) {
			
			if (pa == false) {
				pa = true;
				str.append("<p>");
			}
			
			str.append(formatText(line));
			
		} else {
			
			if (ul == true) {
				ul = false;
				str.append("</ul>\n");
			}
			
			if (ol == true) {
				ol = false;
				str.append("</ol>\n");
			}
			
			if (pa == true) {
				pa = false;
				str.append("</p>\n");
			}
			
		}
		str.append("\n");
		
		return str.toString();
		
	}
	
	public static String formatText(String text) {
		
		String par = "";
		
		char[] chars = text.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			
			char c = text.charAt(i);
			
			// Italic
			if ((c + "").equalsIgnoreCase("_")) {
				if (it == false) {
					it = true;
					par += "<em>";
				} else if (it == true) {
					it = false;
					par += "</em>";
				}
			}
			
			// Bold
			else if ((c + "").equalsIgnoreCase("*")) {
				if ((text.charAt(i + 1) + "").equalsIgnoreCase("*")) {
					i++;
					if (bo == false) {
						bo = true;
						par += "<strong>";
					} else if (bo == true) {
						bo = false;
						par += "</strong>";
					}
				}
			}
			
			// Code
			else if ((c + "").equalsIgnoreCase("`")) {
				if (co == false) {
					co = true;
					par += "<code>";
				} else if (co == true) {
					co = false;
					par += "</code>";
				}
			}
			
			// Normal handling
			else par += c;
			
		}
		
		return par;
		
	}

}
