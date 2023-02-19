package dev.mateas.classes;

import dev.mateas.interfaces.Mapper;
import dev.mateas.interfaces.Parser;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvParser implements Parser {
    String path;
    Class clazz;
    File file;
    BufferedReader bufferedReader;

    public CsvParser(String path, Class clazz) throws FileNotFoundException {
        this.path = path;
        this.clazz = clazz;

        File file = new File(path);
        bufferedReader = new BufferedReader(new FileReader(file));
    }

    public List<String> readHeaders() throws IOException {
        List<String> headers = new ArrayList<String>();
        String line;

        line = bufferedReader.readLine();

        return Arrays.stream(line.split(",")).toList();
    }

    public List<List<String>> readData() throws IOException {
        List<List<String>> data = new ArrayList<List<String>>();

        String line;

        while ((line = bufferedReader.readLine()) != null) {
            data.add(Arrays.stream(line.split(",")).toList());
        }

        return data;
    }

    public List parse() throws IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List result = new ArrayList();

        List<String> headers = readHeaders();
        List<List<String>> data = readData();

        Mapper mapper = new StringListToObjectMapper(clazz);
        mapper.setHeaders(headers);
        mapper.setData(data);
        result = mapper.map();

        return result;
    }
}
