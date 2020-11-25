package com.github.xiaofu.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.io.Files;

public class ClickHouseWriter {
	public static void main(String[] args) throws IOException {
		readAndWriteJson();
	}
	
	public   static void  readAndWriteJson() throws IOException
	{
		ObjectMapper mapper=new ObjectMapper();
		BufferedReader reader = Files.newReader(new File("E:\\vip\\runwork_git\\impala-statistics\\data.JSONEachRow"),
				Charset.forName("utf-8"));
		String line = reader.readLine();
		BufferedWriter writer =Files.newWriter(new File("data.JSONEachRow"), Charset.forName("utf-8"));
		while (line != null) {
			ObjectNode node=(ObjectNode)mapper.readTree(line);
			String time= node.get("visit_time").asText();
			try {
				node.put("visit_time",
						LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
								.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			} catch (Exception e) {
				try {
					node.put("visit_time", LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd -HH:mm:ss.SSS"))
							.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
					System.out.println("just see see:"+node.get("visit_time"));
				} catch (Exception e1) {
					try {
						node.put("visit_time", LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS"))
								.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
					} catch (Exception e2) {
						//
						try {
							node.put("visit_time", LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"))
									.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
						} catch (Exception e3) {
							//
							System.out.println("time format error:"+line);
							line = reader.readLine();
							continue;
						}
					
					}
				}
			}
			int site_id=node.get("site_id").asInt();
			int user_organ_id=node.get("user_organ_id").asInt();
			int catalog=node.get("catalog").asInt();
			node.put("site_id",site_id);
			node.put("user_organ_id", user_organ_id);
			node.put("catalog", catalog);
			node.put("keywords", "");
			writer.write(node.toString()+"\n");
			line=reader.readLine();
		}
		reader.close();
		writer.close();
	}

}
