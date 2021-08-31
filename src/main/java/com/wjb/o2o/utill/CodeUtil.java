package com.wjb.o2o.utill;

import com.google.code.kaptcha.Constants;

import javax.servlet.http.HttpServletRequest;

public class CodeUtil {
    public static boolean checkVerifyCode(HttpServletRequest request) {
        //从Session中获取正确验证码
        String verifyCodeExpected = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        //将其传入http的request拿parrmetterkey转换类型工具来得到parrmetter
        String verifyCodeActual = HttpServletRequestUtils.getString(request, "verifyCodeActual");
        //判断是否为空是否相等
        if (verifyCodeActual == null || !verifyCodeActual.equals(verifyCodeExpected)) {
            return false;
        }
        return true;
    }
}
