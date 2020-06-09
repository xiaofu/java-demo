package com.github.xiaofu.lucene.demo.multiwriterindex;

public class FileBean {
	//路径
		private String path;
		//修改时间
		private Long modified;
		//内容
		private String content;
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}
		public Long getModified() {
			return modified;
		}
		public void setModified(Long modified) {
			this.modified = modified;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
	
}
