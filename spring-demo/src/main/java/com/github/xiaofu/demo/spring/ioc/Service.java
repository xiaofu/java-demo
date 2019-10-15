package com.github.xiaofu.demo.spring.ioc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class Service implements IService{
	
	@Autowired
	IMapData<Entity> mapDataName;
	@Override
	public void test(Entity entity) {
		 
		System.out.println(mapDataName.get(entity));
	}

}
