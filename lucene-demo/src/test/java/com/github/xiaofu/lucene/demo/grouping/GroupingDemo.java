/**
 * 
 */
package com.github.xiaofu.lucene.demo.grouping;

import java.io.IOException;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.grouping.GroupDocs;
import org.apache.lucene.search.grouping.GroupingSearch;
import org.apache.lucene.search.grouping.TopGroups;
import org.apache.lucene.util.BytesRef;

/**
 * @author fulaihua
 *
 */
public class GroupingDemo {
    public static void main(String[] args) throws Exception {
        IndexHelper indexHelper = new IndexHelper();
        indexHelper.createIndexForGroup(1, "Java", "一周精通Java");
        indexHelper.createIndexForGroup(2, "Java", "一周精通MyBatis");
        indexHelper.createIndexForGroup(3, "Java", "一周精通Struts");
        indexHelper.createIndexForGroup(4, "Java", "一周精通Spring");
        indexHelper.createIndexForGroup(5, "Java", "一周精通Spring Cloud");
        indexHelper.createIndexForGroup(6, "Java", "一周精通Hibernate");
        indexHelper.createIndexForGroup(7, "Java", "一周精通JVM");
        indexHelper.createIndexForGroup(8, "C", "一周精通C");
        indexHelper.createIndexForGroup(9, "C", "C语言详解");
        indexHelper.createIndexForGroup(10, "C", "C语言调优");
        indexHelper.createIndexForGroup(11, "C++", "一周精通C++");
        indexHelper.createIndexForGroup(12, "C++", "C++语言详解");
        indexHelper.createIndexForGroup(13, "C++", "C++语言调优");
        IndexSearcher indexSearcher = indexHelper.getIndexSearcher();
        GroupingDemo groupingDemo = new GroupingDemo();
        //把所有的文档都查出来，由添加的数据可以知道，一共有三组，Java组有7个文档，C和C++组分别都有3个文档
        //当然了如果做全匹配的话，还可以用new MatchAllDocsQuery()
        BooleanQuery query = new BooleanQuery.Builder().add(new TermQuery(new Term("author", "Java")), BooleanClause.Occur.SHOULD).add(new TermQuery(new Term
                        ("author", "C")),
                BooleanClause.Occur.SHOULD).add(new TermQuery(new Term("author", "C++")), BooleanClause.Occur.SHOULD).build();
        //控制每次返回几组
        int groupLimit = 2;
        //控制每一页的组内文档数
        int groupDocsLimit = 2;
        //控制组的偏移
        int groupOffset = 0;
        //为了排除干扰因素，全部使用默认的排序方式，当然你还可以使用自己喜欢的排序方式
        //初始值为命中的所有文档数，即最坏情况下，一个文档分成一组，那么文档数就是分组的总数
        int totalGroupCount = indexSearcher.count(query);
        TopGroups<BytesRef> topGroups;
        System.out.println("#### 组间的分页大小为：" + groupLimit);
        System.out.println("#### 组内分页大小为：" + groupDocsLimit);
        while (groupOffset < totalGroupCount) {//说明还有不同的分组
            //控制组内偏移，每次开始遍历一个新的分组时候，需要将其归零
            int groupDocsOffset = 0;
            System.out.println("#### 开始组的分页");
            topGroups = groupingDemo.group(indexSearcher, query, "author", groupDocsOffset, groupDocsLimit, groupOffset, groupLimit);
            //具体搜了一次之后，就知道到底有多少组了，更新totalGroupCount为正确的值
            totalGroupCount = topGroups.totalGroupCount;
            GroupDocs<BytesRef>[] groups = topGroups.groups;
            //开始对组进行遍历
            for (int i = 0; i < groups.length; i++) {
                long totalHits = iterGroupDocs(indexSearcher, groups[i]);//获得这个组内一共多少doc
                //处理完一次分页，groupDocsOffset要更新
                groupDocsOffset += groupDocsLimit;
                //如果组内还有数据，即模拟组内分页的情况，那么应该继续遍历组内剩下的doc
                while (groupDocsOffset < totalHits) {
                    topGroups = groupingDemo.group(indexSearcher, query, "author", groupDocsOffset, groupDocsLimit, groupOffset, groupLimit);
                    //这里面的组一定要和外层for循环正在处理的组保持一致，其实这里面浪费了搜索数据，为什么？
                    //因为Lucene是对多个组同时进行组内向后翻页，而我只是一个组一个组的处理，其它不处理的组相当于是浪费的
                    //所以从这种角度来说，设置groupLimit为1比较合理，即每次处理一个组，而每次只将一个组的组内文档向后翻页
                    GroupDocs<BytesRef> group = topGroups.groups[i];
                    totalHits = iterGroupDocs(indexSearcher, group);
                    //此时需要更新组内偏移量
                    groupDocsOffset += groupDocsLimit;
                }
                //至此，一个组内的doc全部遍历完毕，开始下一组
                groupDocsOffset = 0;
            }
            groupOffset += groupLimit;
            System.out.println("#### 结束组的分页");
        }
    }
    private static long iterGroupDocs(IndexSearcher indexSearcher, GroupDocs<BytesRef> groupDocs) throws IOException {
        long totalHits = groupDocs.totalHits.value;
        System.out.println("\t#### 开始组内分页");
        System.out.println("\t分组名称：" + groupDocs.groupValue.utf8ToString());
        ScoreDoc[] scoreDocs = groupDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println("\t\t组内记录：" + indexSearcher.doc(scoreDoc.doc));
        }
        System.out.println("\t#### 结束组内分页");
        return totalHits;
    }
    public TopGroups<BytesRef> group(IndexSearcher indexSearcher, Query query, String groupField,
                                     int groupDocsOffset, int groupDocsLimit, int groupOffset, int groupLimit) throws Exception {
        return group(indexSearcher, query, Sort.RELEVANCE, Sort.RELEVANCE, groupField, groupDocsOffset, groupDocsLimit, groupOffset, groupLimit);
    }
    public TopGroups<BytesRef> group(IndexSearcher indexSearcher, Query query, Sort groupSort, Sort withinGroupSort, String groupField,
                                     int groupDocsOffset, int groupDocsLimit, int groupOffset, int groupLimit) throws Exception {
        //实例化GroupingSearch实例，传入分组域
        GroupingSearch groupingSearch = new GroupingSearch(groupField);
        //设置组间排序方式
        groupingSearch.setGroupSort(groupSort);
        //设置组内排序方式
        groupingSearch.setSortWithinGroup(withinGroupSort);
        //是否要填充每个返回的group和groups docs的排序field
        //groupingSearch.setFillSortFields(true);
        //设置用来缓存第二阶段搜索的最大内存，单位MB，第二个参数表示是否缓存评分
        groupingSearch.setCachingInMB(64.0, true);
        //是否计算符合查询条件的所有组
        groupingSearch.setAllGroups(true);
        groupingSearch.setAllGroupHeads(true);
        //设置一个分组内的上限
        groupingSearch.setGroupDocsLimit(groupDocsLimit);
        //设置一个分组内的偏移
        groupingSearch.setGroupDocsOffset(groupDocsOffset);
        TopGroups<BytesRef> result = groupingSearch.search(indexSearcher, query, groupOffset, groupLimit);
        return result;
    }
}
