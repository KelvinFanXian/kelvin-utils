package com.github.KelvinFanXian.pattern;

import java.util.regex.Pattern;

/**
 * @author Kelvin范显
 * @createDate 2018年10月11日
 */
public class CommonPatterns {
    // 邮编
    public static Pattern ZIP_CODE_PATTERN = Pattern.compile(
            "(?<![0-9])" //左边不能是数字
            + "[0-9]{6}"
            + "(?![0-9])"); //右边不能是数字

    // 移动电话
    public static Pattern MOBILE_PHONE_PATTERN = Pattern.compile(
            "(?<![0-9])" //左边不能是数字
            + "((0|\\+86|0086)\\s?)?" // 0 +86 0086
            + "1[34578][0-9]-?[0-9]{4}-?[0-9]{4}" // 186-1234-5678
            + "(?![0-9])"); //右边不能是数字

    // 固定电话
    public static Pattern FIXED_PHONE_PATTERN = Pattern.compile(
            "(?<![0-9])" //左边不能是数字
            + "(\\(?0[0-9]{2,3}\\)?-?)?" //区号
            + "[0-9]{7,8}"//市内号码
            + "(?![0-9])"); //右边不能是数字

    // 日期
    public static Pattern DATE_PATTERN = Pattern.compile(
            "(?<![0-9])" //左边不能是数字
            + "\\d{4}-" //年
            + "(0?[1-9]|1[0-2])-" //月
            + "(0?[1-9]|[1-2][0-9]|3[01])"//日
            + "(?![0-9])"); //右边不能是数字

    // 时间
    public static Pattern TIME_PATTERN = Pattern.compile(
            "(?<![0-9])" //左边不能是数字
            + "([0-1][0-9]|2[0-3])" //小时
            + ":" + "[0-5][0-9]"//分钟
            + "(?![0-9])"); //右边不能是数字

    // 身份证
    public static Pattern ID_CARD_PATTERN = Pattern.compile(
            "(?<![0-9])" //左边不能是数字
            + "[1-9][0-9]{14}" //一代身份证
            + "([0-9]{2}[0-9xX])?" //二代身份证多出的部分
            + "(?![0-9])"); //右边不能是数字

    // IP
    public static Pattern IP_PATTERN = Pattern.compile(
            "(?<![0-9])" //左边不能是数字
            + "((0{0,2}[0-9]|0?[0-9]{2}|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}"
            + "(0{0,2}[0-9]|0?[0-9]{2}|1[0-9]{2}|2[0-4][0-9]|25[0-5])"
            + "(?![0-9])");//右边不能是数字

    // URL
    public static Pattern URL_PATTERN = Pattern.compile(
            "(http|https)://" + "[-0-9a-zA-Z.]+" //主机名
            + "(:\\d+)?" //端口
            + "(" //可选的路径和查询-开始
            + "/[-\\w$.+!*'(),%;:@&=]*" //第一层路径
            + "(/[-\\w$.+!*'(),%;:@&=]*)*" //可选的其他层路径
            + "(\\?[-\\w$.+!*'(),%;:@&=]*)?" //可选的查询字符串
        + ")?"); //可选的路径和查询-结束

    // 新浪邮箱
    public static Pattern SINA_EMAIL_PATTERN = Pattern.compile(
            "[a-z0-9]"
            + "[a-z0-9_]{2,14}"
            + "[a-z0-9]@sina\\.com");

    // QQ邮箱
    public static Pattern QQ_EMAIL_PATTERN = Pattern.compile(
            //点、减号、下划线不能连续出现两次或两次以上
            "(?![-0-9a-zA-Z._]*(--|\\.\\.|__))"
            + "[a-zA-Z]" //必须以英文字母开头
            + "[-0-9a-zA-Z._]{1,16}" //3-18位 英文、数字、减号、点、下划线组成
            + "[a-zA-Z0-9]@qq\\.com"); //由英文字母、数字结尾

    // 邮箱
    public static Pattern GENERAL_EMAIL_PATTERN = Pattern.compile(
            "[0-9a-zA-Z][-._0-9a-zA-Z]{0,63}" //用户名
            + "@"
            + "([0-9a-zA-Z][-0-9a-zA-Z]{0,62}\\.)+" //域名部分
        + "[a-zA-Z]{2,3}"); //顶级域名

    // 中文字符
    public static Pattern CHINESE_PATTERN = Pattern.compile(
            "[\\u4e00-\\u9fff]");
}
