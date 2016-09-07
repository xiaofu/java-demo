package com.github.xiaofu.demo.spring.ioc;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Scope("prototype")
@Repository("mapDataName")
public class NameMapData implements IMapData<Entity> {

	@Override
	public String get(Entity a) {
		 
		return a.getT1();
	}

}
