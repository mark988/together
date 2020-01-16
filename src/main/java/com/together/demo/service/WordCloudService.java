package com.together.demo.service;

import com.together.demo.pojo.vo.Result;

import java.io.IOException;

public interface WordCloudService {
    String create(String sourceWorddPath,String destPngPath) throws IOException;
}
