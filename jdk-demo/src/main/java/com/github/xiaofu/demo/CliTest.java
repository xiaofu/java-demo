package com.github.xiaofu.demo;

import java.util.Calendar;
import java.util.TimeZone;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CliTest {
	public static void main(String[] args) throws ParseException {
		Options opts = new Options();
		Option op = OptionBuilder.withArgName("configuration").hasArg()
				.create("D");
		Option op1 = OptionBuilder.withArgName("configuration").hasArg()
				.create("conf");
		opts.addOption(op);
		opts.addOption(op1);
		 CommandLineParser parser = new GnuParser();
		 CommandLine cmd = parser.parse(opts, new String[]{"-D conf=23","-D test=5","-conf=2.xml","-conf=3.xml"}, true);
		 for (String item : cmd.getOptionValues("D")) {
			System.out.println(item);
		}
		 for (String item : cmd.getOptionValues("conf")) {
				System.out.println(item);
			}
		 System.out.println(String.format("%1$tF %1$tT", Calendar.getInstance() .getTime()));
		 System.out.println(Calendar.getInstance() .getTime());
		System.out.println(TimeZone.getDefault());

	}
}
