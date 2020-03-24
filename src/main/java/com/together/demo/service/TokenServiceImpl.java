package com.together.demo.service;

import com.together.demo.exception.GlobalException;
import com.together.demo.pojo.enums.CommonEnum;
import com.together.demo.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private  RedisService redisService;

    @Override
    public String createToken() {

        String str = UUIDUtil.get32UUID();
        StringBuffer  token = new StringBuffer();
        try {
            token.append("token_").append(str);
            redisService.setEx(token.toString(), token.toString(),5000L);
            boolean  notEmpty = StringUtils.isNotEmpty(token.toString());
            if (notEmpty) {
                return token.toString();
            }
        }catch (Exception  ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean checkToken(HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        log.info(" request token:{}",token);
        if(StringUtils.isBlank(token)) {
            token = request.getParameter("token");
            if(StringUtils.isBlank(token)) {
                throw new GlobalException(CommonEnum.PARAMETER_IS_NULL);
            }
        }
        if(!redisService.exists(token)) {
            throw new GlobalException(CommonEnum.PARAMETER_IS_ERRO);
        }
        log.info("删除token");
        boolean remove = redisService.remove(token);
        if(!remove) {
            throw new GlobalException(CommonEnum.PARAMETER_IS_ERRO);
        }
        return true;
    }
}
