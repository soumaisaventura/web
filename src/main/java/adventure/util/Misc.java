package adventure.util;

import adventure.entity.User;
import adventure.entity.UserRegistration;
import br.gov.frameworkdemoiselle.util.Reflections;
import br.gov.frameworkdemoiselle.util.Strings;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class Misc {

    private Misc() {
    }

    public static <T> void copyFields(T from, T to) throws Exception {
        for (Field field : Reflections.getNonStaticFields(to.getClass())) {
            if (Reflections.getFieldValue(field, from) != null && Reflections.getFieldValue(field, to) == null) {
                Object value = Reflections.getFieldValue(field, from);

                String setter = "set" + Strings.firstToUpper(field.getName());
                Method method = to.getClass().getMethod(setter, value.getClass());
                method.invoke(to, value);
            }
        }
    }

    private static String stringfyMembers(List<String> members) {
        String memberNames = "";
        for (int i = 0; i < members.size(); i++) {
            String separator;

            if (i == 0) {
                separator = "";
            } else if (i == members.size() - 1) {
                separator = " e ";
            } else {
                separator = ", ";
            }

            memberNames += separator + members.get(i);
        }
        return memberNames;
    }

    public static String stringfy(List<UserRegistration> members) {
        List<String> result = new ArrayList();
        for (UserRegistration teamFormation : members) {
            result.add(teamFormation.getUser().getProfile().getName());
        }

        return stringfyMembers(result);
    }

    public static String simplifyUsername(User user) {
        String result = null;

        if (user != null && user.getProfile() != null && user.getProfile().getName() != null) {
//            String parts[] = user.getProfile().getName().split(" ");
//            result = parts[0] + (parts.length > 1 ? " " + parts[1] : "") + (parts.length > 2 && parts[1].length() <= 3 ? " " + parts[2] : "");
            result = user.getProfile().getName();
        }


        return result;
    }

    public static String capitalize(String string) {
        String capitalized = null;

        if (string != null) {
            capitalized = "";

            for (String part : string.toLowerCase().split(" ")) {
                if (!part.isEmpty() && part.charAt(0) == 'd' && part.length() <= 3) {
                    capitalized += part;
                    // } else if (part.length() > 2) {
                } else {
                    capitalized += Strings.firstToUpper(part);
                    // capitalized += part;
                }
                capitalized += " ";
            }

            capitalized = capitalized.trim();
        }

        return capitalized;
    }


    public static boolean isEmpty(Object object) {
        boolean result = true;

        if (object != null) {
            for (Field field : Reflections.getNonStaticDeclaredFields(object.getClass())) {
                result &= Reflections.getFieldValue(field, object) == null;
            }
        }
        return result;
    }
}
