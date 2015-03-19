package mc.alk.arena.controllers;

import mc.alk.arena.objects.CompetitionState;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author alkarin
 */
public class StateController {
    final static AtomicInteger count = new AtomicInteger(0);
    final static List<Class<? extends Enum>> enums = new ArrayList<Class<? extends Enum>>();


    public static CompetitionState[] values() {
        int size = 0;
        int i = 0;
        for (Class<? extends Enum> enumClass : enums) {
            size += enumClass.getEnumConstants().length;
        }
        CompetitionState[] states = new CompetitionState[size];
        for (Class<? extends Enum> enumClass : enums){
            for (Enum e : enumClass.getEnumConstants()) {
                states[i++] = (CompetitionState) e;
            }
        }
        return states;
    }

    public static void register(Class<? extends Enum> enumClass) {
        if (!enums.contains(enumClass)) {
            enums.add(enumClass);
        }
    }

    public static int register(Enum en) {
        Class<? extends Enum> enumClass = en.getClass();
        for (Class<? extends Enum> classes : enums){
            if (classes.equals(enumClass))
                continue;
            for (Enum e : classes.getEnumConstants()) {
                if (e.name().equalsIgnoreCase(en.name()))
                    throw new IllegalStateException("You can't have multiple CompetitionStates with the same name \n"+
                            enumClass.getSimpleName() +"."+en.name() +"  and "+ e.getDeclaringClass().getSimpleName() +"."+e.name());
            }
        }
        if (!enums.contains(enumClass)) {
            enums.add(enumClass);
        }
        return count.incrementAndGet();
    }

    public static CompetitionState fromString(String arg) {
        for (Class<? extends Enum> enumClass : enums) {
            Method m = null;
            try {
                m = enumClass.getMethod("fromString", String.class);
            } catch (Exception e) {
                /* no method there, check for next */
            }
            if (m == null) {
                try{
                    m = enumClass.getMethod("valueOf", String.class);
                } catch (Exception e ){
                    /* no method there, check for next */
                }
            }
            if (m == null) {
                continue;}
            try {
                Object o = m.invoke(null, arg);
                if (o == null || !(o instanceof CompetitionState))
                    continue;
                return (CompetitionState) o;
            } catch (Exception e) {
                /* continue on */
            }
        }
        return null;
    }
}
