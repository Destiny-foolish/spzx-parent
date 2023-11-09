package com.atguigu.spzx.manager.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.atguigu.spzx.manager.mapper.CategoryMapper;
import org.apache.poi.ss.formula.functions.T;

import java.io.IOException;
import java.util.List;
import java.util.Map;

//监听器不能被spring管理，因为spring是单例模式，如果实现多个文件上使用单例模式会乱码
public class ExcelListener<T> implements ReadListener<T> {

    /**
     每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    /**
     * 缓存的数据,如果不做缓存的话，会造成OOM问题，可以参考机组DMA方式
     */
    private List cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    //通过构造传递Mapper，操作数据库
    private CategoryMapper categoryMapper;

    public ExcelListener(CategoryMapper categoryMapper){
        this.categoryMapper = categoryMapper;
    }

    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        cachedDataList.add(t);
        if(cachedDataList.size() >= BATCH_COUNT) {
            //调用方法，一次性添加到数据库
            saveData();
            //clear cachedDataList
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        //保存数据
        saveData();
    }
    private void saveData(){
        categoryMapper.batchInsert(cachedDataList);
    }
}
