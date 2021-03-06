package com.github.xiaofu.demo.hbase;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.catalog.CatalogTracker;
import org.apache.hadoop.hbase.catalog.MetaEditor;
import org.apache.hadoop.hbase.catalog.MetaReader;
import org.apache.hadoop.hbase.client.Append;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Increment;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.RegionSplitter;
import org.apache.hadoop.hbase.util.RegionSplitter.SplitAlgorithm;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class ClientOpTest {
	private static Configuration conf = null;
	private static String TABLE = "modify_classobject_info_nd";
	static {
		conf = HBaseConfiguration.create();
		try {

			conf.set("hbase.zookeeper.quorum",
					"node203.vipcloud,node204.vipcloud,node205.vipcloud");
			/*
			 * conf.set( "hbase.zookeeper.quorum",
			 * "node600.vipcloud,node601.vipcloud,node602.vipcloud,node603.vipcloud,node604.vipcloud"
			 * ); onlineTable = new HTable(conf, "file_uploader");
			 */
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	
	@Before
	public void setUp() throws Exception {
		
		
	}
	@Test
	public void deleteData() throws IOException
	{
		for (int i = 0; i < 100; i++)
		{
			List<String> rowKeyLists=ClientOp.scanerReturnRow(TABLE);
			List<Delete> deleteLists=Lists.newArrayList();
			for (String item : rowKeyLists)
			{
				deleteLists.add(new Delete(Bytes.toBytes(item)));
			}
			ClientOp.deleteRow(TABLE, deleteLists);
		}
	
	}
	@Test
	public void selectRow() throws IOException {
		 
		ClientOp.selectRow(TABLE, "35"); 
		//ClientOp.selectRow(TABLE, "row2");
		//ClientOp.selectRow(TABLE, "row3");
	
	}
	@Test
	public void writeRowMore() throws IOException {
		ClientOp.writeRow(TABLE, "row2","main", "b", "value3");
		ClientOp.writeRow(TABLE, "row3","main", "c", "value4");

	
	}
	@Test
	public void writeRow() throws IOException {
		ClientOp.writeRow(TABLE, "row1","main", "a", "value1");

	
	}

	@Test
	public void deleteRowColumn() throws IOException {
		//ClientOp.writeRow(ClientOp.TABLE, "row1","colfam1", "a", "value1");
		//ClientOp.writeRow(ClientOp.TABLE, "row1","colfam1", "b", "value1");
		//ClientOp.deleteRow(TABLE, "row1");
		ClientOp.deleteRow(TABLE, "row1");
		//ClientOp.deleteRow(TABLE, "row2");
		//ClientOp.deleteRow(TABLE, "row3");
	}

	@Test
	public void deteteRowSpecifiedColumnVersion() throws IOException {
		ClientOp.writeRow(ClientOp.TABLE, "row1","colfam1", "a", "value1");
		ClientOp.writeRow(ClientOp.TABLE, "row1","colfam1", "a", "value1");
		ClientOp.writeRow(ClientOp.TABLE, "row1","colfam1", "a", "value1");
		HTable table = new HTable(conf, TABLE);
		List<Delete> list = new ArrayList<Delete>();
		Delete d1 = new Delete("row1".getBytes());
		d1.deleteColumn("cf".getBytes(), "a".getBytes());
		list.add(d1);
		table.delete(list);
		System.out.println("删除行成功！");
		ClientOp.selectRow(TABLE, "row1");
		ClientOp.deleteRow(TABLE, "row1");
	}

	@Test
	public void appendValue() throws IOException {
		ClientOp.writeRow(ClientOp.TABLE, "row1","colfam1", "a", "value1");
		ClientOp.writeRow(ClientOp.TABLE, "row1","colfam1", "a", "value1");
		ClientOp.selectRow(TABLE, "row1");
		HTable table = new HTable(conf, TABLE);
		// 历史版本直接删除了，只有最新版本+追加值了
		Append append = new Append("row1".getBytes());
		append.add("cf".getBytes(), "a".getBytes(), "22".getBytes());
		table.append(append);
		System.out.println("====================");
		ClientOp.selectRow(TABLE, "row1");
		ClientOp.deleteRow(TABLE, "row1");
	}

	@Test
	public void incrementValue() throws IOException {

		ClientOp.writeRow(ClientOp.TABLE, "row1","colfam1", "a", "1");
		ClientOp.writeRow(ClientOp.TABLE, "row1","colfam1", "a", "1");
		ClientOp.selectRow(TABLE, "row1");
		HTable table = new HTable(conf, TABLE);
		Increment increment = new Increment("row1".getBytes());
		increment.addColumn("cf".getBytes(), "a".getBytes(), 100);

		System.out.println("====================");
		System.out.println(increment.toString());
		ClientOp.selectRow(TABLE, "row1");
		ClientOp.deleteRow(TABLE, "row1");
	}
	@Test
	public  void testMetaReader() throws IOException
	{
		/*
		 * CatalogTracker catalogTracker=new
		 * CatalogTracker(HBaseConfiguration.create());
		 * 
		 * if(MetaReader.tableExists(catalogTracker, TABLE)) { System.out.println("ok");
		 * }
		 */
		HTable hMeta = new HTable(conf, HConstants.META_TABLE_NAME);
		Scan scan = new Scan();
		scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("server"));
		scan.setCaching(1000);
		ResultScanner scanner = hMeta.getScanner(scan);
		Iterator<Result> iterator = scanner.iterator();
		// Process the region location info, consider to refactor it using
		// HRegionInterface.getOnlineRegions()
		while (iterator.hasNext()) {
			Result result = iterator.next();
			KeyValue kv = result.getColumnLatest(Bytes.toBytes("info"), Bytes.toBytes("server"));
			String rawRegion = Bytes.toString(kv.getRow());
			System.out.println(Bytes.toString(HRegionInfo.getTableName(Bytes.toBytes(rawRegion))));
			
		}
	}
	
	/**
	 * TODO：操作元数据的标准方式
	 * @throws IOException 
	 */
	@Test
	public  void testMetaEditor() throws IOException
	{
		CatalogTracker catalogTracker=new CatalogTracker(HBaseConfiguration.create());
		HRegionInfo regionInfo=new HRegionInfo(Bytes.toBytes(TABLE),Bytes.toBytes("80000000"),null,false,1484211569379l);
		MetaEditor.deleteRegion(catalogTracker, regionInfo);
	 
	}
	
	@Test
	public   void createTable()
			throws IOException {
		HBaseAdmin admin = new HBaseAdmin(conf);
		if (admin.tableExists(TABLE)) {
			System.out.println("表已经存在！");
		} else {
			HTableDescriptor tableDesc = new HTableDescriptor(TABLE);
			String[] cfs=new String[]{"main"};
			for (int i = 0; i < cfs.length; i++) {
				tableDesc.addFamily(new HColumnDescriptor(cfs[i]));
			}
			SplitAlgorithm splitAlgor = new RegionSplitter.HexStringSplit();
			admin.createTableWithAutoSplittableQualifier(tableDesc,
					splitAlgor.split(2));
			System.out.println("表创建成功！");
		}
	}
}
