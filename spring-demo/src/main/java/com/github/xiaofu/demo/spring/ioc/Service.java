package com.github.xiaofu.demo.spring.ioc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class Service implements IService{
	//默认是使用类型绑定，但是如果在XML定义时当前服务指定的是按名称绑定，所以默认，它会使用当前属性名进行绑定
	//可以使用@Qualifier来改变绑定名称名称
	@Autowired
	IMapData<Entity> mapDataName;
	@Override
	public void test(Entity entity) {
		 
		System.out.println(mapDataName.get(entity));
	}

}
