package dev.mateas.interfaces;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface Mapper {
    List map() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException;
    void setHeaders(List<String> headers);
    void setData(List<List<String>> data);
}
