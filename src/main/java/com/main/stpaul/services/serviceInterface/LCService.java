package com.main.stpaul.services.serviceInterface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public interface LCService {
    ByteArrayOutputStream generateTc(String studentId) throws IOException ;
}
