package com.github.xiaofu.demo.spring.ioc;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Scope("prototype")
@Repository("mapData")
public class MapData  implements IMapData<Entity>{

	@Override
	public String get(Entity t) {
		return  t.getT1();
	}

}
