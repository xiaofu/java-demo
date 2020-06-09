package com.github.xiaofu.lucene.demo.suggest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.lucene.search.suggest.InputIterator;
import org.apache.lucene.util.BytesRef;

public class ProductIterator implements InputIterator {
	private Iterator<Product> productIterator;
    private Product currentProduct;
 
    ProductIterator(Iterator<Product> productIterator) {
        this.productIterator = productIterator;
    }
    
    @Override
    public boolean hasContexts() {
        return true;
    }
 
    /**
     * 是否有设置payload信息
     */
    @Override
    public boolean hasPayloads() {
        return true;
    }
 
    public Comparator<BytesRef> getComparator() {
        return null;
    }
    @Override
    public BytesRef next() {
        if (productIterator.hasNext()) {
            currentProduct = productIterator.next();
            try {
                return new BytesRef(currentProduct.getName().getBytes("UTF8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Couldn't convert to UTF-8",e);
            }
        } else {
            return null;
        }
    }
    @Override
    public BytesRef payload() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(currentProduct);
            out.close();
            return new BytesRef(bos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Well that's unfortunate.");
        }
    }
    
    @Override
    public Set<BytesRef> contexts() {
        try {
            Set<BytesRef> regions = new HashSet<BytesRef>();
            for (String region : currentProduct.getRegions()) {
                regions.add(new BytesRef(region.getBytes("UTF8")));
            }
            return regions;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Couldn't convert to UTF-8");
        }
    }
 
    @Override
    public long weight() {
        return currentProduct.getNumberSold();
    }
}
 
