package com.together.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 *  版本查询
 */

@Slf4j
@RestController
public class VersionController {
    @RequestMapping(value = "/version", method = GET)
    public Map<String, Object> versionInformation() {
        return readGitProperties();
    }
    private Map<String, Object> readGitProperties() {
        InputStream inputStream = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            inputStream = classLoader.getResourceAsStream("git.properties");
            String versionJson =CharStreams.toString(new InputStreamReader(inputStream, Charsets.UTF_8));
            JSONObject jsonObject = JSON.parseObject(versionJson);
            Set<Map.Entry<String, Object>> entrySet = jsonObject.entrySet();

            if (!CollectionUtils.isEmpty(entrySet)) {
                return entrySet.stream()
                        .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (o, n) -> n));
            }
        } catch (Exception e) {
            log.error("get git version info fail", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                log.error("close inputstream fail", e);
            }
        }
        return new HashMap<>();
    }

    String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}