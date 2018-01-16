package com.example;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
 
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
 
 
public class VipcloudUtil
{
	private static final Pattern pattern = Pattern.compile("\\[([^\\[\\]]+)\\]");
	public static boolean deleteDir(File dir)
	{
		if (dir.isDirectory())
		{
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++)
			{
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success)
				{
					return false;
				}
			}
		}
		return dir.delete();
	}
 

	// writeUTF in out limited 65535 length.
	public static void WriteLargeUtf8(String str, DataOutput out)
			throws IOException
	{
		byte[] data = str.getBytes("UTF-8");
		out.writeInt(data.length);
		out.write(data);
	}

	public static String ReadLargeUtf8(DataInput in) throws IOException
	{
		int length = in.readInt();
		byte[] data = new byte[length];
		in.readFully(data);
		String result = new String(data, "UTF-8");
		return result;
	}

	 
	 

	public static byte[] SerializeObject(Writable obj) throws IOException
	{
		ObjectOutputStream outputStream = null;
		try
		{
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			outputStream = new ObjectOutputStream(byteArray);
			obj.write(outputStream);
			outputStream.flush();
			return byteArray.toByteArray();
		}
		finally
		{
			outputStream.close();
		}
	}

	public static void DeserializeObject(byte[] datas, Writable obj)
			throws IOException
	{
		ObjectInputStream inputStream = null;
		try
		{
			inputStream = new ObjectInputStream(new ByteArrayInputStream(datas));
			obj.readFields(inputStream);
		}
		finally
		{
			inputStream.close();
		}
	}

	 

	public static void WriteArrayListLong(DataOutput out, ArrayList<Long> items)
			throws IOException
	{
		out.writeInt(items.size());
		for (Long item : items)
		{
			out.writeLong(item.longValue());
		}
	}

	public static void WriteArrayListString(DataOutput out,
			ArrayList<String> items) throws IOException
	{
		out.writeInt(items.size());
		if (items.size() == 0)
			return;
		for (String item : items)
		{
			WriteLargeUtf8(item, out);
			// out.writeUTF(item);
		}
	}

	public static void WriteHashMap(DataOutput out,
			HashMap<String, String> hashMap) throws IOException
	{
		out.writeInt(hashMap.size());
		if (hashMap.size() == 0)
			return;

		for (Map.Entry<String, String> entry : hashMap.entrySet())
		{
			out.writeUTF(entry.getKey());
			if(entry.getValue() == null){
				throw new NullPointerException(entry.getKey() + " val is null point.");
			}
			WriteLargeUtf8(entry.getValue(), out);
		}
	}

	public static void ReadHashMap(DataInput in, HashMap<String, String> hashMap)
			throws IOException
	{
		hashMap.clear();
		int len = in.readInt();
		for (int i = 0; i < len; i++)
		{
			String key = in.readUTF();
			String value = ReadLargeUtf8(in);
			hashMap.put(key, value);
		}
	}

 

	public static void ReadArrayListString(DataInput in, ArrayList<String> items)
			throws IOException
	{
		int len = in.readInt();
		for (int i = 0; i < len; i++)
		{
			items.add(ReadLargeUtf8(in));

		}
	}

	public static boolean isNumeric(String str)
	{

		if (str.isEmpty())
			return false;

		for (int i = str.length(); --i >= 0;)
		{
			if (!Character.isDigit(str.charAt(i)))
			{
				return false;
			}
		}
		return true;
	}

	 
	
	/**
	 * 将含有的标引的数据，转换为用'@'符号链接的字符串。
	 * @author Xukaiqiang 2016-9-5 上午11:24:43
	 * @param str
	 * @return
	 */
	public static String parseIndexObject(String str){
		StringBuffer info = new StringBuffer();
		if(isEmpty(str)){
			return info.toString();
		}
		for (String item : str.split(";")) {
			if (item.length() > 0 && item.charAt(0) == '[')
			{
				int pos = item.indexOf(']');
				if (pos != -1)
				{
					String key = item.substring(1, pos);
					if (isNumeric(key))
					{
						String v = item.substring(pos + 1);
						info.append(key + "@" + v +";");
					}
				}
			}
		}
		return info.toString();
	}

	public static String GetInputPath(FileSplit file)
	{
		Path inPath = file.getPath();
		String inputTable = inPath.getParent().getName();
		String stepName = inPath.getParent().getParent().getName();
		return "/" + stepName + "/" + inputTable;
	}

	public static boolean isAllowInputPath(String curInputPath,
			String allowInputs)
	{
		String[] inputPaths = allowInputs.split(",");
		for (String inputPath : inputPaths)
		{
			if (inputPath.endsWith(inputPath))
			{
				return true;
			}
		}
		return false;
	}

	 
	public static void printArrayListString(ArrayList<String> items)
	{
		StringBuilder tempStr = new StringBuilder();
		for (String item : items)
		{
			if (tempStr.length() != 0)
				tempStr.append(',');
			tempStr.append(item);
		}
		System.out.println(tempStr.toString());
	}

	public static void printArrayListLong(ArrayList<Long> items)
	{
		StringBuilder tempStr = new StringBuilder();
		for (Long item : items)
		{
			if (tempStr.length() != 0)
				tempStr.append(',');
			tempStr.append(item.toString());
		}
		System.out.println(tempStr.toString());
	}

	 

	 

	public static <T extends Object> boolean isEmpty(Object T)
	{

		if (T == null)
		{
			return true;
		}
		boolean flag = false;
		if (T instanceof String)
		{
			flag = "".equals(((String) T).trim()) ? true : false;
		}
		else if (T instanceof Object[])
		{
			flag = ((Object[]) T).length == 0;
		}
		else if (T instanceof Collection<?>)
		{
			Collection<?> c = (Collection<?>) T;
			flag = c.isEmpty();
		}
		else if (T instanceof Map<?, ?>)
		{
			Map<?, ?> map = (Map<?, ?>) T;
			flag = map.isEmpty();
		}
		else
		{

		}
		return flag;
	}

	 
	private static Boolean ruleStop(String str)
	{
		//空白字符
		if(Pattern.matches("^\\s+$", str))
		{
			return true;
		}
		//整数、小数、百分比
		if(Pattern.matches("^\\d*\\.*\\d*%*$", str))
		{
			return true;
		}
		//第xx
		if(Pattern.matches("^第.*$", str))
		{
			return true;
		}
		//以数字开头的
		if(Pattern.matches("^\\d+.*$", str))
		{
			return true;
		}
		//单个英文字母
		if(Pattern.matches("^\\w$", str))
		{
			return true;
		}
		return false;
	}
	
	public static final String full2HalfChange(String QJstr)
			throws UnsupportedEncodingException {

		StringBuffer outStrBuf = new StringBuffer("");

		String Tstr = "";

		byte[] b = null;

		for (int i = 0; i < QJstr.length(); i++) {

			Tstr = QJstr.substring(i, i + 1);

			// 全角空格转换成半角空格

			if (Tstr.equals("　")) {

				outStrBuf.append(" ");

				continue;

			}

			b = Tstr.getBytes("unicode");

			// 得到 unicode 字节数据

			if (b[2] == -1) {

				// 表示全角？

				b[3] = (byte) (b[3] + 32);

				b[2] = 0;

				outStrBuf.append(new String(b, "unicode"));

			} else {

				outStrBuf.append(Tstr);

			}

		} // end for.
		return outStrBuf.toString();
	}
	
	public static String GetInputTable(FileSplit file)
	{
		Path inPath = file.getPath();
		String inputTable = inPath.getParent().getName();
		return "/" + inputTable;
	}
	
	public static String GetInputPath(Path file)
	{
		if (file == null)
			return "Test_InputFile";
		String inputTable = file.getName();
		String stepName = file.getParent().getName();
		return "/" + inputTable;
	}
	
	
	public static String sortRelValus(String relValues){
		
		if(VipcloudUtil.isEmpty(relValues)){
			return "";
		}
		TreeMap<String, String> trees = new TreeMap<String, String>();
		for (String v : relValues.split(";")) {
			int index = v.lastIndexOf("@");
			if(index == -1 || index == v.length() -1){
				continue;
			}
			String key = v.substring(index, v.length());
			trees.put(key, v);
		}
		StringBuffer sb = new StringBuffer();
		for (Entry<String, String> et : trees.entrySet()) {
			sb.append(et.getValue() + ";");
		}
		return sb.toString();
		
	}
	
	public static String sort360FieldData(String valueOf360){
		if(VipcloudUtil.isEmpty(valueOf360)){
			return "";
		}
		StringBuffer data = new StringBuffer();
		int i = 0;
		for (String part : valueOf360.split("\\|")) {
			TreeMap<String, String> trees = new TreeMap<String, String>();
			for (String v : part.split(";")) {
				trees.put(v, "");
			}
			StringBuffer sb = new StringBuffer();
			for (String k : trees.keySet()) {
				sb.append(k +";");
			}
			data.append(sb.toString());
			if(i == 0){
				data.append("|");
			}
			i ++;
		}
		return data.toString();
	}
	
	
	public static String sortVisClassValue(String visValue){
		if(VipcloudUtil.isEmpty(visValue)){
			return "";
		}
		TreeMap<String, String> trees = new TreeMap<String, String>();
		StringBuffer sb = new StringBuffer();
		for (String v : visValue.split(";")) {
			trees.put(v, "");
		}
		for (String k : trees.keySet()) {
			sb.append(k + ";");
		}
		return sb.toString();
	}
	
	public static String washInvalidOrgAndWriterValue(String value){
		if(VipcloudUtil.isEmpty(value)){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (String v : value.split(";")) {
			String newV = v.replaceAll("\\[+\\d*\\]", "");
			if(isEmpty(newV)){
				continue;
			}
			sb.append(v + ";");
		}
		return sb.toString();
	}
	
	// just only fix 'showorgans', diff organ as same index
	public static String washInvalidOrgansValue(String value){
		if(isEmpty(value)){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		boolean fstIndex = false;
		for (String v : value.split(";")) {
			boolean valid = true;
			Matcher matcher = pattern.matcher(v);
			if(matcher.find()){
				String index = matcher.group(1);
				if(!fstIndex && "1".equals(index)){
					fstIndex = true;
				}else if(fstIndex && "1".equals(index)){
					valid = false;
				}
			}
			if(valid){
				sb.append(v + ";");
			}else{
				String tmp = v.replaceFirst("\\[+\\d*\\]", "");
				sb.append(tmp + ";");
			}
		}
		return sb.toString();
	}
	
	public static void addRelationData(HashMap<String, String> dataMap, String key, String value){
		
		dataMap.put(key, VipcloudUtil.sortRelValus(value));
	}
	
	public static Object newInstance(String className) throws IOException
	{
		Object instance = null;
		try
		{
			instance = Class.forName(className).newInstance();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		return instance;
	}
}
