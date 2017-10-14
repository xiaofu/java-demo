/**
 * 
 */
package com.github.xiaofu.demo.hbase;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.HServerAddress;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.catalog.MetaReader;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Row;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.KeyOnlyFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.MD5Hash;
import org.apache.hadoop.hbase.util.RegionSplitter;
import org.apache.hadoop.hbase.util.RegionSplitter.SplitAlgorithm;
import org.apache.hadoop.hbase.util.Writables;

import com.google.common.collect.Lists;

/**
 * @author fulaihua
 * 
 */
public class ClientOp {
	public static Configuration conf = null;
	private static HTable onlineTable;
	private static HTable localTable;
	public static String TABLE = "flh_test";
	static {
		conf = HBaseConfiguration.create();
		try {
			conf.set("hbase.zookeeper.quorum","vdatanode1,vnamenode,vdatanode2");
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException,
			InterruptedException {
		//deleteTable(TABLE);
		// scanRootOrMeta();
		 //deleteTable(TABLE);
		// deleteRow(TABLE,"aa");
		 //createTable(TABLE, new String[] { "colfam1" });
		// batchTest("test_flh");
		// selectRow(TABLE, "row2");
		// exportOnlineToLocal();
		// System.out.println(idToMD5Hash("JG@1494"));
		// inportDataToTest();
		/*
		 * for(int i=1 ;i<10 ;i++) {
		 * writeRow(TABLE,"row"+i,"colfam1","qual1","3"); }
		 */

		// mergeRegionOnline();
		// TestRegion();
		// scanRootOrMeta();
		// split("test_flh");
		// assign("test_flh,,1450860289148.66d10102117f74347029dab830fde714.");
		// scaner(TABLE);
		//printTableMetadata("user_recommendation_info");
		// deleteRowsForJCZ();
		//oneRowMultipleCols(TABLE);
		//move("5e30225f17d3723528cb65d07face13d" ,"node205.vipcloud,60020,1461580533463");
		//compact("user_recommendation_info");
		//System.out.println(new HRegionInfo(Bytes.toBytes("user_test"),"aaaaaa".getBytes(),"cccccc".getBytes()).getRegionNameAsString());
	}

	static void oneRowMultipleCols(String tableName) throws IOException {
		HTable table = new HTable(conf, tableName);
		byte[] ROW1 = Bytes.toBytes("row1");
		// byte[] ROW2 = Bytes.toBytes("row2");
		byte[] COLFAM1 = Bytes.toBytes("colfam1");
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		byte[] QUAL1 = Bytes.toBytes("2015-01-02");
		byte[] QUAL2 = Bytes.toBytes("2015-08-02");
		byte[] QUAL3 = Bytes.toBytes("2015-11-01");
		byte[] QUAL4 = Bytes.toBytes("2015-12-12");
		List<Row> batch = new ArrayList<Row>();
		Put put1 = new Put(ROW1);
		Calendar date3 = Calendar.getInstance();
		date3.clear();
		date3.set(2015, 10, 01, 0, 0, 0);
		Calendar date4 = Calendar.getInstance();
		date4.clear();
		date4.set(2015, 11, 12, 0, 0, 0);
		put1.add(COLFAM1, Bytes.toBytes(date3.getTimeInMillis()),
				Bytes.toBytes(format.format(date3.getTime())));
		put1.add(COLFAM1, Bytes.toBytes(date4.getTimeInMillis()),
				Bytes.toBytes(format.format(date4.getTime())));
		batch.add(put1);
		Put put2 = new Put(ROW1);
		Calendar date1 = Calendar.getInstance();
		date1.clear();
		date1.set(2015, 0, 02, 0, 0, 0);
		Calendar date2 = Calendar.getInstance();
		date2.clear();
		date2.set(2015, 07, 02, 0, 0, 0);
		put2.add(COLFAM1, Bytes.toBytes( date1.getTimeInMillis()),
				Bytes.toBytes(format.format(date1.getTime())));
		put2.add(COLFAM1, Bytes.toBytes( date2.getTimeInMillis()),
				Bytes.toBytes(format.format(date2.getTime())));
		batch.add(put2);

		try {
			table.batch(batch);
		} catch (Exception e) {
			System.err.println("Error: " + e);
		}
		scaner(tableName);

	}

	public static void deleteRowsForJCZ() throws IOException {

		/*
		 * File file=new File("D:\\VIP\\test"); List<Delete> lists=new
		 * ArrayList<Delete>(); BufferedReader reader=new BufferedReader(new
		 * InputStreamReader(new FileInputStream(file))); String
		 * line=reader.readLine(); while(line!=null) { lists.add(new
		 * Delete(Bytes.toBytes(idToMD5Hash(line)))); line=reader.readLine(); }
		 */

		Delete del1 = new Delete(Bytes.toBytes(idToMD5Hash("27109402")));

		localTable.delete(del1);

	}

	public static void printTableMetadata(String tableName)
			throws TableNotFoundException, IOException {
		HBaseAdmin admin = new HBaseAdmin(conf);
		HTableDescriptor desc = admin.getTableDescriptor(Bytes
				.toBytes(tableName));
		/*
		 * for (HColumnDescriptor columnDesc : desc.getColumnFamilies()) {
		 * System.out.println("columnDesc:"+columnDesc.toString()); }
		 */
		System.out.println("desc:" + desc.toString());
	}

	public static void assign(String regionName)
			throws MasterNotRunningException, ZooKeeperConnectionException,
			IOException {
		HBaseAdmin admin = new HBaseAdmin(conf);
		admin.assign(Bytes.toBytes(regionName));
	}
	public static void  move(String encodedRegionName,String serverName) throws IOException, InterruptedException
	{
		HBaseAdmin admin = new HBaseAdmin(conf);
		admin.move(Bytes.toBytes(encodedRegionName), Bytes.toBytes(serverName));
		 
	}
	public static void  compact(String tableNameOrRegionName) throws IOException, InterruptedException
	{
		HBaseAdmin admin = new HBaseAdmin(conf);
		admin.compact(tableNameOrRegionName);
		admin.close();
		 
	}
	public static void split(String tableNameOrRegionName) throws IOException,
			InterruptedException {
		HBaseAdmin admin = new HBaseAdmin(conf);
		admin.split(tableNameOrRegionName);
		admin.close();
	}

	public static void createTable(String tablename, String[] cfs)
			throws IOException {
		HBaseAdmin admin = new HBaseAdmin(conf);
		if (admin.tableExists(tablename)) {
			System.out.println("表已经存在！");
		} else {
			HTableDescriptor tableDesc = new HTableDescriptor(tablename);
			for (int i = 0; i < cfs.length; i++) {
				tableDesc.addFamily(new HColumnDescriptor(cfs[i]));
			}
			SplitAlgorithm splitAlgor = new RegionSplitter.HexStringSplit();
			admin.createTableWithAutoSplittableQualifier(tableDesc,
					splitAlgor.split(2));
			System.out.println("表创建成功！");
		}
	}

	public static void deleteTable(String tablename) throws IOException {
		try {
			HBaseAdmin admin = new HBaseAdmin(conf);
			//admin.disableTable(tablename);
			admin.deleteTable(tablename);
			System.out.println("表删除成功！");
		} catch (MasterNotRunningException e) {
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			e.printStackTrace();
		}
	}

	public static void writeRow(String tablename, String rowKey, String family,
			String qualifier, String value) {
		try {
			HTable table = new HTable(conf, tablename);
			Put put = new Put(Bytes.toBytes(idToMD5Hash(rowKey)));
			put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier),
					Bytes.toBytes(value));

			table.put(put);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void selectRow(String tablename, String rowKey)
			throws IOException {
		HTable table = new HTable(conf, tablename);
		Get g = new Get(idToMD5Hash(rowKey).getBytes());
		// select all versions of all columns specified row
		g.setMaxVersions();

		Result rs = table.get(g);
		if (rs.raw().length == 0)
			System.out.println("no data");
		for (KeyValue kv : rs.raw()) {
			 
			System.out.println(Bytes.toString(kv.getValue()));

		}
		table.close();
	}
	public static void deleteRow(String tablename, List<Delete> list)
			throws IOException {
		HTable table = new HTable(conf, tablename);
		table.delete(list);
		System.out.println("删除行成功！");
		table.close();
	}
	
	public static void deleteRow(String tablename, String rowkey)
			throws IOException {
		HTable table = new HTable(conf, tablename);
		List<Delete> list = new ArrayList<Delete>();
		Delete d1 = new Delete(idToMD5Hash(rowkey).getBytes());
		// d1.deleteColumn("bb".getBytes(), "dd".getBytes());
		list.add(d1);
		table.delete(list);
		System.out.println("删除行成功！");
	}
	public static void deleteRowColumn(String tablename, String rowkey,String family,String column)
			throws IOException {
		HTable table = new HTable(conf, tablename);
		List<Delete> list = new ArrayList<Delete>();
		Delete d1 = new Delete(idToMD5Hash(rowkey).getBytes());
		 d1.deleteColumn(family.getBytes(),column.getBytes());
		list.add(d1);
		table.delete(list);
		table.close();
		System.out.println("删除行成功！");
	}
	/**
	 * get all rows,all columns latest version
	 * 
	 * @param tablename
	 */
	public static void scaner(String tablename) {
		try {
			HTable table = new HTable(conf, tablename);
			Scan s = new Scan();
			s.addFamily(Bytes.toBytes("main"));
			//s.addColumn(Bytes.toBytes("main"), Bytes.toBytes("_keyid"));
			FilterList filterLists=new FilterList();
			filterLists.addFilter(new KeyOnlyFilter());
			filterLists.addFilter(new SingleColumnValueFilter());
			s.setCaching(1000);
			s.setCacheBlocks(false);
			s.setFilter(filterLists);
			ResultScanner rs = table.getScanner(s);
			for (Result r : rs) {
				KeyValue[] kv = r.raw();
				for (int i = 0; i < kv.length; i++) { 
					System.out.print(new String(kv[i].getRow()) + " ");
					System.out.print(new String(kv[i].getFamily()) + " ");
					System.out.print( Bytes.toLong( kv[i].getQualifier()) + " ");
					System.out.print(kv[i].getTimestamp() + " ");
					System.out.println(new String( kv[i].getValue()));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static List<String> scanerReturnRow(String tablename) throws IOException {
		List<String> lists=Lists.newArrayList();
		HTable table =null;
		try {
			  table = new HTable(conf, tablename);
			Scan s = new Scan();
			s.addFamily(Bytes.toBytes("main"));
			//s.addColumn(Bytes.toBytes("main"), Bytes.toBytes("_keyid"));
			FilterList filterLists=new FilterList();
			filterLists.addFilter(new KeyOnlyFilter());
			filterLists.addFilter(new SingleColumnValueFilter());
			s.setCaching(1000);
			s.setCacheBlocks(false);
			//s.setFilter(filterLists);
			ResultScanner rs = table.getScanner(s);
			int count=0;
			final int len=50000;
			for (Result r : rs) {
				KeyValue[] kv = r.raw();
				lists.add(new String(r.getRow()));
				count++;
				if(count==len)
					break;
			}
			rs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			table.close();
		}
		return lists;
	}
	public static void batchTest(String tablename) throws IOException {
		HTable table = new HTable(conf, tablename);
		byte[] ROW1 = Bytes.toBytes("row1");
		byte[] ROW2 = Bytes.toBytes("row2");
		byte[] COLFAM1 = Bytes.toBytes("colfam1");
		byte[] COLFAM2 = Bytes.toBytes("colfam2");
		byte[] QUAL1 = Bytes.toBytes("qual1");
		byte[] QUAL2 = Bytes.toBytes("qual2");
		List<Row> batch = new ArrayList<Row>();
		Put put = new Put(ROW2);
		put.add(COLFAM2, QUAL1, Bytes.toBytes("val5"));
		batch.add(put);
		Get get1 = new Get(ROW1);
		get1.addColumn(COLFAM1, QUAL1);
		batch.add(get1);
		Delete delete = new Delete(ROW1);
		delete.deleteColumns(COLFAM1, QUAL2);
		batch.add(delete);
		Get get2 = new Get(ROW2);
		get2.addFamily(Bytes.toBytes("BOGUS"));
		batch.add(get2);
		Object[] results = new Object[batch.size()];
		try {
			table.batch(batch, results);
		} catch (Exception e) {
			System.err.println("Error: " + e);
		}
		for (int i = 0; i < results.length; i++) {
			System.out.println("Result[" + i + "]: " + results[i]);
		}
	}

	public static void inportDataToTest() throws IOException {
		List<Put> lists = new ArrayList<Put>();
		for (int i = 0; i < 1000; i++) {
			Put put = new Put(Bytes.toBytes(idToMD5Hash(UUID.randomUUID()
					.toString().replace("-", ""))));
			put.setWriteToWAL(false);

			put.add(Bytes.toBytes("colfam1"), Bytes.toBytes("aa"),
					Bytes.toBytes(i + ""));
			lists.add(put);
		}
		localTable.setAutoFlush(false);
		try {
			localTable.put(lists);
		} finally {
			localTable.flushCommits();
		}
	}

	public static String idToMD5Hash(String idKey) {
		return MD5Hash.getMD5AsHex(Bytes.toBytes(idKey)).substring(0, 8) + ":"
				+ idKey;
	}

	public static void exportOnlineToLocal() throws IOException {
		Scan scan = new Scan();
		scan.setCaching(20);
		scan.setBatch(20);
		ResultScanner scanner = null;
		try {
			scanner = onlineTable.getScanner(scan);
			List<Put> lists = new ArrayList<Put>();
			for (Result result : scanner) {
				for (KeyValue kv : result.list()) {
					System.out.println(kv);
					Put put = new Put();
					put.add(kv);
					lists.add(put);
				}
			}
			// localTable.put(lists);

		} finally {
			if (scanner != null)
				scanner.close();
			onlineTable.close();
			localTable.close();

		}
	}

	@SuppressWarnings("deprecation")
	public static void TestRegion() throws IOException {
		Map<HRegionInfo, HServerAddress> maps = localTable.getRegionsInfo();
		for (Map.Entry<HRegionInfo, HServerAddress> item : maps.entrySet()) {
			System.out.println(item.getKey().getRegionNameAsString());

		}
		System.out.println("region counts:" + maps.size());
	}

	public static void scanRootOrMeta() throws IOException {
 
		Scan scan = MetaReader.getScanForTableName(HConstants.META_TABLE_NAME);
		// scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("regioninfo"));
		scan.setCaching(1000);
		scan.addFamily(HConstants.CATALOG_FAMILY);
		/*
		 * scan.addColumn(HConstants.CATALOG_FAMILY,
		 * HConstants.REGIONINFO_QUALIFIER);
		 * scan.addColumn(HConstants.CATALOG_FAMILY,
		 * HConstants.SERVER_QUALIFIER);
		 */
		HTable table = new HTable(conf, HConstants.META_TABLE_NAME);
		ResultScanner scanner = table.getScanner(scan);
		try {

			for (Result result : scanner) {
				System.out.println(MetaReader
						.getServerNameFromCatalogResult(result));
				// System.out.println(Bytes.toString(result.getRow()));
				/*
				 * System.out.println((HRegionInfo) Writables.getWritable(
				 * result.getValue(HConstants.CATALOG_FAMILY,
				 * HConstants.REGIONINFO_QUALIFIER), new HRegionInfo()));
				 */

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			scanner.close();
			table.close();
		}
	}

}
