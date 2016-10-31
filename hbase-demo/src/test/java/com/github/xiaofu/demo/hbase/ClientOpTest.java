package com.github.xiaofu.demo.hbase;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.catalog.CatalogTracker;
import org.apache.hadoop.hbase.catalog.MetaEditor;
import org.apache.hadoop.hbase.catalog.MetaReader;
import org.apache.hadoop.hbase.client.Append;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Increment;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.RegionSplitter;
import org.apache.hadoop.hbase.util.RegionSplitter.SplitAlgorithm;
import org.junit.Before;
import org.junit.Test;

public class ClientOpTest {
	private static Configuration conf = null;
	private static String TABLE = "flh_test";
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
	public void selectRow() throws IOException {
		 
		ClientOp.selectRow(TABLE, "row1");
	
	}
	
	@Test
	public void writeRow() throws IOException {
		ClientOp.writeRow(TABLE, "row1","main", "a", "value1");
		ClientOp.writeRow(TABLE, "row1","main", "a", "value2");

	
	}

	@Test
	public void deleteRowColumn() throws IOException {
		//ClientOp.writeRow(ClientOp.TABLE, "row1","colfam1", "a", "value1");
		//ClientOp.writeRow(ClientOp.TABLE, "row1","colfam1", "b", "value1");
		//ClientOp.deleteRow(TABLE, "row1");
		ClientOp.deleteRowColumn(TABLE, "row1","main","a");
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
		CatalogTracker catalogTracker=new CatalogTracker(HBaseConfiguration.create());
	 
		if(MetaReader.tableExists(catalogTracker, TABLE))
		{
			System.out.println("ok");
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
		HRegionInfo regionInfo=new HRegionInfo(Bytes.toBytes(TABLE), Bytes.toBytes("b3333313"), null,false,1471342179526l);
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
