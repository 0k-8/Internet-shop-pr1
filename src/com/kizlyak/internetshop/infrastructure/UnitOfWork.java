package com.kizlyak.internetshop.infrastructure;

public interface UnitOfWork {

    void commit(); // Зберегти всі зміни у файли
}