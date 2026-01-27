package com.kizlyak.internetshop.infrastructure;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonDataMapper<T> {

    private final String filePath;
    private final Gson gson;
    private final Type listType;

    public JsonDataMapper(String filePath, Type listType) {
        this.filePath = filePath;
        this.listType = listType;

        // ОНОВЛЕНО: Додаємо LocalDateTimeAdapter, щоб Java 25 не видавала помилку
        this.gson = new GsonBuilder()
              .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
              .setPrettyPrinting()
              .create();
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
            return new ArrayList<>();
        }
    }
}