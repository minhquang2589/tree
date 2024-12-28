package com.example.demo.classInterFace;

import java.sql.SQLException;

public interface initDataInterface<T> {
    void initData(T data) throws SQLException;
}

