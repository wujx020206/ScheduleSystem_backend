package cn.edu.fc.javaee.core.config;

import cn.edu.fc.javaee.core.util.JwtHelper;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Configuration
public class OpenFeignConfig{

    @Bean
    public RequestInterceptor requestInterceptor(){
        return new OpenFeignHeaderInterceptor();
    }
}

class OpenFeignHeaderInterceptor implements RequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(OpenFeignHeaderInterceptor.class);

    private static final JwtHelper jwtHelper = new JwtHelper();

    @Override
    public void apply(RequestTemplate requestTemplate) {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        if (null == request) {
            return;
        }

        Enumeration<String> headerNames = request.getHeaderNames();
        if (null == headerNames) {
            return;
        }
        logger.info("apply: feign interceptor.....");
        boolean hasAuth = false;
        // 把请求过来的header请求头 原样设置到feign请求头中
        // 包括token
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            // 跳过 content-length,防止报错Feign报错feign.RetryableException: too many bytes written executing
            if (name.equalsIgnoreCase("Content-Length")) {
                logger.debug("apply: skip Content-Length");
                continue;
            }
            if (name.equalsIgnoreCase("Transfer-Encoding")) {
                logger.debug("apply: skip Transfer-Encoding");
                continue;
            }
            if (name.equalsIgnoreCase("authorization")){
                continue;
            }
            this.addHeader(requestTemplate, name, request.getHeader(name));

        }
        this.addHeader(requestTemplate, "Content-Type", new String[]{"application/json;charset=UTF-8"});

        logger.debug("apply: add admin token...");
        String adminToken = jwtHelper.createToken(1L, "admin", 0L, 1, 3600);
        this.addHeader(requestTemplate, "authorization", adminToken);
    }

    private void addHeader(RequestTemplate requestTemplate, String name, String... values) {
        if (!requestTemplate.headers().containsKey(name)) {
            requestTemplate.header(name, values);
            logger.debug("addHeader: name = {}, values= {}",name,values);
        }
    }

}
