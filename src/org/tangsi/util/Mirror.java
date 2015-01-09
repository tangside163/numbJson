package org.tangsi.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 增强反射工具类
 * @author tangsi
 *
 */
public class Mirror {

	private Class clazz;

	public Mirror(Class clazz) {
		this.clazz = clazz;
	}
	
	/**
	 * 是否是类数字：如byte、short、int、long、float、double、Byte、Short、Integer、Long、Float、Double、BigInteger、BigDecimal、AtomicInteger、AtomicLong
	 * @return
	 */
	public boolean isNumberLike() {
		return byte.class == this.clazz || short.class == this.clazz
				|| int.class == this.clazz || long.class == this.clazz
				|| float.class == this.clazz || double.class == this.clazz
				|| Number.class.isAssignableFrom(this.clazz);
	}
	
	/**
	 * 是否是类字符串:如String、StringBuffer、StringBuilder、char、Character、CharSequense
	 * @return
	 */
	public boolean isCharacterLike() {
		return CharSequence.class.isAssignableFrom(this.clazz) || char.class == this.clazz;
	}

}
