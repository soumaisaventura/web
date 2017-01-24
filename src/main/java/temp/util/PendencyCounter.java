package temp.util;

import br.gov.frameworkdemoiselle.util.Reflections;

import java.lang.reflect.Field;

public final class PendencyCounter {

    private PendencyCounter() {
    }

    public static int count(Object object) {
        int pendency = 0;

        if (object != null) {
            for (Field field : Reflections.getNonStaticDeclaredFields(object.getClass())) {
                if (field.isAnnotationPresent(PendencyCount.class) && Reflections.getFieldValue(field, object) == null) {
                    pendency++;
                }
            }
        }

        return pendency;
    }
}
