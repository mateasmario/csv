package dev.mateas.interfaces;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface Parser {
    List parse() throws IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException;
}
