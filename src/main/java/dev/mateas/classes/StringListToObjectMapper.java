package dev.mateas.classes;

import dev.mateas.interfaces.Mapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringListToObjectMapper implements Mapper {
    private List<String> headers;
    private List<List<String>> data;
    private Class clazz;

    public StringListToObjectMapper(Class clazz) {
        this.clazz = clazz;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public void setData(List<List<String>> data) {
        this.data = data;
    }

    public List<String> buildSetterNames() {
        List<String> setterNames = new ArrayList<String>();

        for(String headerElement : headers) {
            setterNames.add("set" + headerElement.substring(0, 1).toUpperCase() + headerElement.substring(1));
        }

        return setterNames;
    }

    public List<Method> buildSetterMethods(List<String> setterNames) {
        List<Method> setterMethods = new ArrayList<Method>();
        List<Method> declaredMethods = Arrays.stream(clazz.getDeclaredMethods()).toList();

        for (String setterName : setterNames) {
            for (Method declaredMethod : declaredMethods) {
                if (declaredMethod.getName().equals(setterName))
                    setterMethods.add(declaredMethod);
            }
        }

        return setterMethods;
    }

    public void invokeMethod(Object object, Method setterMethod, String argument) throws InvocationTargetException, IllegalAccessException {
        String type = setterMethod.getParameters()[0].getType().getName();

        switch(type) {
            case "byte":
                setterMethod.invoke(object, Byte.valueOf(argument));
                break;
            case "short":
                setterMethod.invoke(object, Short.valueOf(argument));
                break;
            case "char":
                setterMethod.invoke(object, argument.charAt(0));
                break;
            case "int":
                setterMethod.invoke(object, Integer.valueOf(argument));
                break;
            case "long":
                setterMethod.invoke(object, Long.valueOf(argument));
                break;
            case "float":
                setterMethod.invoke(object, Float.valueOf(argument));
                break;
            case "double":
                setterMethod.invoke(object, Double.valueOf(argument));
                break;
            default:
                setterMethod.invoke(object, argument);
                break;
        }
    }

    public List map() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List result = new ArrayList();
        List<String> setterNames = buildSetterNames();
        List<Method> setterMethods = buildSetterMethods(setterNames);
        int numberOfSetters = setterMethods.size();


        for(List<String> dataElement : data) {
            Object object = clazz.newInstance();

            for (int i = 0; i<numberOfSetters; i++) {
                Method setterMethod = setterMethods.get(i);
                invokeMethod(object, setterMethod, dataElement.get(i));
            }

            result.add(object);
        }

        return result;
    }
}
