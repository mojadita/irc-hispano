/**
 * 
 */
package es.lcssl.irc.protocol;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author luis
 *
 */
public class MaskUtils{
	static private String regex_1 = 
			"0*(0|[1-9][0-9]?|1[0-9][0-9]|2[0-4][0-9]|25[0-5])";
	static private String regex_ipv4 =
			regex_1 + "(\\." + regex_1 + "){3}";
	static public String regex_hostname =
			"([0-9A-Za-z-]+\\.){1,}[0-9A-Za-z-]*[A-Za-z]";
	static public String regex =
//			"((" + regex_ipv4 + ")|(" + regex_hostname + "))";
			"\\b((" + regex_hostname + ")|(" + regex_ipv4 + "))\\b";
	static public Pattern pattern = Pattern.compile(regex);
	
	
	
	
	
	public static final void main(String[] args) {
		System.out.println("Regex = " + regex);
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		while (in.hasNextLine()) {
			String s = in.nextLine();
			Matcher m = pattern.matcher(s);
			while (m.find()) {
				System.out.println("[" + s.substring(m.start(), m.end()) + "]");
				for (int i = 0; i < m.groupCount(); i++) {
					if (m.start(i) < 0) continue;
					System.out.print("  g" + i + "[" + m.start(i) + "-" + m.end(i) + "]: " +
					s.substring(m.start(i), m.end(i)));
					System.out.println();
				}
			}
		}
	}
}
