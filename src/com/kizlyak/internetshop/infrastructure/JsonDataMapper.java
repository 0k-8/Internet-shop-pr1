package com.kizlyak.internetshop.infrastructure;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonDataMapper<T> {

    private final String filePath;
    private final Gson gson;
    private final Type listType;

    public JsonDataMapper(String filePath, Type listType) {
        this.filePath = filePath;
        this.listType = listType;
        // setPrettyPrinting робить JSON читабельним для людини
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void writeToFile(List<T> data) {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            System.err.println("Помилка запису у файл " + filePath + ": " + e.getMessage());
        }
    }

    public List<T> readFromFile() {
        try (FileReader reader = new FileReader(filePath)) {
            List<T> data = gson.fromJson(reader, listType);
            return (data != null) ? data : new ArrayList<>();
        } catch (IOException e) {
            // Якщо файлу ще немає, повертаємо порожній список
            return new ArrayList<>();
        }
    }
}