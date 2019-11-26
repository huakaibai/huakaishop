package com.zhibinwang.core.mapper;

import java.util.List;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * @desc 将list<do> 转换list<dto> 或者反转也可以</></>
 * @author 花开
 */
public class MapperUtils {
	static MapperFactory mapperFactory;
	static {
		mapperFactory = new DefaultMapperFactory.Builder().build();
	}

	public static <S, D> List<D> mapAsList(Iterable<S> source, Class<D> destinationClass) {
		return mapperFactory.getMapperFacade().mapAsList(source, destinationClass);
	}
}
