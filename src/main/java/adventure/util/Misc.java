package adventure.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import br.gov.frameworkdemoiselle.util.Reflections;
import br.gov.frameworkdemoiselle.util.Strings;

public final class Misc {

	private Misc() {
	}

	public static <T> T copyFields(T from, T to) throws Exception {
		for (Field field : Reflections.getNonStaticFields(to.getClass())) {
			if (Reflections.getFieldValue(field, from) != null && Reflections.getFieldValue(field, to) == null) {
				Object value = Reflections.getFieldValue(field, from);

				String setter = "set" + Strings.firstToUpper(field.getName());
				Method method = to.getClass().getMethod(setter, value.getClass());
				method.invoke(to, value);
			}
		}

		return to;
	}
}
