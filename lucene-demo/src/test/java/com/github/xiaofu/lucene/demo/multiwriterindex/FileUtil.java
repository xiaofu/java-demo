package com.github.xiaofu.lucene.demo.multiwriterindex;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class FileUtil {
	/**读取文件信息和下属文件夹
	 * @param folder
	 * @return
	 * @throws IOException
	 */
	public static List<FileBean> getFolderFiles(String folder) throws IOException {
		List<FileBean> fileBeans = new LinkedList<FileBean>();
		File file = new File(folder);
		if(file.isDirectory()){
			File[] files = file.listFiles();
			if(files != null){
				for (File file2 : files) {
					fileBeans.addAll(getFolderFiles(file2.getAbsolutePath()));
				}
			}
		}else{
			FileBean bean = new FileBean();
			bean.setPath(file.getAbsolutePath());
			bean.setModified(file.lastModified());
			bean.setContent(new String(Files.readAllBytes(Paths.get(folder))));
			fileBeans.add(bean);
		}
		return fileBeans;
	}

}
