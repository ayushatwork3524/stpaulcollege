package com.main.stpaul.services.serviceInterface;

import java.io.ByteArrayInputStream;

public interface ExcelService {
    ByteArrayInputStream generateExcel(String query, String stdClass, String section, String session) throws Exception;

    ByteArrayInputStream generateRawExcel() throws Exception;
}
