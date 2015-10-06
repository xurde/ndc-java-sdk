package org.iata.ndc.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

class Initializer {
	private static final Class[] CLASSES_TO_SKIP = {
			Boolean.class,
			String.class,
			BigDecimal.class,
			BigInteger.class,
			List.class,
			JAXBElement.class,
			XMLGregorianCalendar.class,
			Duration.class
	};

	private Initializer() {
	}

	/**
	 * Creates an object and recursively initializes all of its
	 * required fields to empty objects created using no-arg
	 * constructor.
	 *
	 * @param formClass
	 * @return
	 */
	public static <T> T getObject(Class<T> formClass) {
		try {
			T object = formClass.newInstance();

			// find all fields
			Field[] fields = formClass.getDeclaredFields();
			for (Field field : fields) {
				// process only complex fields
				if (!skipField(field.getType())) {
					// if a required JAXB field is null, initialize it with a new object
					field.setAccessible(true);
					if (field.get(object) == null && isRequired(field)) {
						Object newObject = getObject(field.getType());
						field.set(object, newObject);
					}
				}
			}
			return object;

		} catch (InstantiationException e) {
			throw new IllegalStateException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}

	private static boolean isRequired(Field field) {
		for (Annotation a : field.getAnnotations()) {
			if (a instanceof XmlElement) {
				XmlElement e = (XmlElement) a;
				return e.required();
			}
		}
		return false;
	}

	private static boolean skipField(Class<?> clazz) {
		for (Class<?> test : CLASSES_TO_SKIP) {
			if (test.equals(clazz))
				return true;
		}
		return false;
	}
}
