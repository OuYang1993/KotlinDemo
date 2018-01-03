package ouyang.com.serialtest.util;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by OuYang on 2015/9/24.
 * 通用工具
 */
public class CommonUtil {
    private static final String TAG = CommonUtil.class.getSimpleName();
    private final static byte[] hex = "0123456789ABCDEF".getBytes();

    /**
     * 点击屏幕EditText 之外的地方隐藏软键盘
     */

    public static void autoHideKeyboard(final Activity activity, View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(activity);
                    return false;
                }

            });
        }

        // If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                autoHideKeyboard(activity, innerView);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            View focus = activity.getCurrentFocus();
            if (focus != null) {
                inputMethodManager.hideSoftInputFromWindow(
                        focus.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 验证手机号码是否符合规则
     *
     * @param mobile 手机号码
     * @return true-符合   false- 不符合
     */
    public static boolean isMobileNumber(String mobile) {
        boolean isMatches = false;
        if (!TextUtils.isEmpty(mobile.trim())) {
            Pattern p = Pattern.compile("^(1[34578]\\d{9})$");
            Matcher m = p.matcher(mobile);
            isMatches = m.matches();
        }
        return isMatches;
    }

    /**
     * 验证固定电话是否符合规则
     * 不支持400 800等开头
     *
     * @param phone 电话号码
     * @return true-符合    false-不符合
     */
    public static boolean isPhoneNumber(String phone) {
        boolean isMatches = false;
        if (!TextUtils.isEmpty(phone.trim())) {
            Pattern p = Pattern.compile("(0\\d{2,3})?(-)?\\d{7,8}(-\\d{3,4})?");
            Matcher m = p.matcher(phone);
            isMatches = m.matches();
        }
        return isMatches;
    }


    /**
     * 如果字符串为null, 返回空字符串,避免产生空指针异常
     *
     * @param str str
     * @return 如果字符串为null, 返回空字符串,否则返回原值
     */
    public static String filterNull(String str) {
        return TextUtils.isEmpty(str) ? "" : str;
    }

    /**
     * 对银行卡号做星号隐藏处理
     * 留前6位和后4位,中间用星号代替,如果长度小于12位则不做处理
     *
     * @param cardNo 原卡号
     * @return 处理后的卡号
     */
    public static String formatCardNo(String cardNo) {
        if (!TextUtils.isEmpty(cardNo)) {
            if (cardNo.equalsIgnoreCase("null")) {
                return "";
            }
            if (cardNo.length() < 12) {
                return cardNo;
            }
            try {
                String prefix = cardNo.substring(0, 6);//前6位
                String suffix = cardNo.substring(cardNo.length() - 4, cardNo.length());//后4位
                return prefix + "********" + suffix;//前6位拼接8个星号在拼接后4位
            } catch (Exception e) {
                //如果出现异常, 则返回原卡号,一般是位数不足等异常
                e.printStackTrace();
                return cardNo;
            }
        }

        return "";
    }

    /**
     * 获取当前时间,返回格式为 yyyy-MM-dd HH:mm:ss
     *
     * @return 时间
     */
    public static String getCurrentTime() {
        return getCurrentTime("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取当前时间,返回格式为自定义格式,如果为空,则返回默认格式:yyyy-MM-dd HH:mm:ss
     *
     * @param pattern 日期格式
     * @return 时间
     */
    public static String getCurrentTime(String pattern) {
        if (TextUtils.isEmpty(pattern))
            pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        return format.format(System.currentTimeMillis());
    }

    /**
     * 按照自定义格式格式化时间,如果格式为空,则返回默认格式:yyyy-MM-dd HH:mm:ss
     *
     * @param pattern 日期格式
     * @return 时间
     */
    public static String formatTime(String pattern, Date time) {
        if (TextUtils.isEmpty(pattern))
            pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        return format.format(time);
    }

    /**
     * 按照自定义格式,格式化时间,如果格式为空,则返回默认格式:yyyy-MM-dd HH:mm:ss
     *
     * @param pattern 日期格式
     * @return 时间
     */
    public static String formatTime(String pattern, long time) {
        if (TextUtils.isEmpty(pattern))
            pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        return format.format(time);
    }

    /**
     * 将 时间转成Long型
     *
     * @param time    时间(如 2017-1-1 00:00:00)
     * @param pattern 传入时间的格式(如时间为2017-1-1,那么格式必须为yyyy-MM-dd,如果与传入时间格式不一致,则会出异常导致返回当前时间)
     * @return Long型的时间(出现异常默认返回当前时间)
     */
    public static Long parseTime(String time, String pattern) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
            Date date = format.parse(time);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return System.currentTimeMillis();
        }
    }


    /**
     * 按照自定义格式,格式化时间,如果格式为空,则返回默认格式:yyyy-MM-dd HH:mm:ss
     *
     * @param pattern 日期格式
     * @return 时间
     */
    public static String formatTime(String pattern, String time) {
        try {
            if (TextUtils.isEmpty(pattern))
                pattern = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
            Date date = format.parse(time);
            return format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return time;
        }
    }

    /**
     * 把yyyy-MM-dd HH:mm:ss 格式转化成只有日期
     *
     * @param time 时间
     * @return 不带时间的日期
     */
    public static String formatTimeToDay(String time) {
        return formatTime("yyyy-MM-dd", time);
    }

    /**
     * 把yyyy-MM-dd HH:mm:ss 格式转化成只有日期
     *
     * @param time 时间
     * @return 不带时间的日期
     */
    public static String formatTimeToMonth(String time) {
        return formatTime("yyyy-MM", time);
    }

    /**
     * ,格式化时间,返回格式:yyyy-MM-dd HH:mm:ss
     *
     * @return 时间
     */
    public static String formatTime(String time) {
        return formatTime(null, time);
    }

    /**
     * 格式化double类型,避免出现数值过大而出现科学计数法
     *
     * @param d double
     * @return 转换后的值, 保留两位小数
     */
    public static String formatDouble(double d) {
        DecimalFormat format = new DecimalFormat("#0.00");
        return format.format(d);
    }

    /**
     * 将两个String类型转换成BigDecimal 对比大小
     * <p>两个String值必须能转成数字,否则会报错</p>
     * <p>一般在对比金额的时候用,将string转double再对比可能会出现精度丢失的问题</p>
     *
     * @param first  对比的值
     * @param second 参考值
     * @return <p>如果 对比的值比参考值大,则返回 1</p>
     * <p>如果 对比的值比参考值小,则返回 -1</p>
     * <p>如果 对比的值和参考值相等,则返回 0</p>
     */
    public static int compareStringInDecimal(String first, String second) {
        BigDecimal d1 = new BigDecimal(first);
        BigDecimal d2 = new BigDecimal(second);
        return d1.compareTo(d2);
    }

    /**
     * <p>将单位为元的金额通过BigDecimal转换成单位为分,即金额*100,不保留小数</p>
     *
     * @param money 金额,单位为元
     * @return 金额, 单位为分
     */
    public static String formatYuanToFen(String money) {

        if (TextUtils.isEmpty(money)) {
            return new BigDecimal("0.00").toPlainString();
        }
        BigDecimal decimal = new BigDecimal(money);
        BigDecimal d = new BigDecimal("100");
        decimal = decimal.multiply(d);
        decimal = decimal.setScale(0, BigDecimal.ROUND_DOWN);
        Log.e(TAG, "formatYuanToFen --> before format: " + money + "\tafter format: " + decimal.toPlainString());
        return decimal.toPlainString();
    }

    /**
     * <p>将单位为分的金额通过BigDecimal转换成单位为元,即金额/100,保留两位小数,不进行四舍五入</p>
     *
     * @param money 金额,单位为分
     * @return 金额, 单位为元
     */
    public static String formatFenToYuan(String money) {
        if (TextUtils.isEmpty(money)) {
            return new BigDecimal("0").toPlainString();
        }
        BigDecimal decimal = new BigDecimal(money);
        decimal = decimal.setScale(2, BigDecimal.ROUND_DOWN);
        BigDecimal d = new BigDecimal("100.00");
        decimal = decimal.divide(d, BigDecimal.ROUND_DOWN);
        decimal = decimal.setScale(2, BigDecimal.ROUND_DOWN);
        Log.e(TAG, "formatFenToYuan --> before format: " + money + "\tafter format: " + decimal.toPlainString());
        return decimal.toPlainString();
    }

    /**
     * <p>将单位为分的金额通过BigDecimal转换成int值</p>
     *
     * @param fenMoney 金额,单位为分,不包含小数
     * @return 金额, int类型
     */
    public static long formatFenToLongValue(String fenMoney) {

        if (TextUtils.isEmpty(fenMoney)) {
            return new BigDecimal("0").intValueExact();
        }
        BigDecimal decimal = new BigDecimal(fenMoney);
        decimal = decimal.setScale(0, BigDecimal.ROUND_DOWN);
        return decimal.intValueExact();
    }

    /**
     * 对单位为分的金额进行补位操作,有的地方需要12位长度,因此再前补0
     * <p>注意:fenAmount的长度必须要比maxLength小</p>
     *
     * @param fenAmount 金额,单位为分
     * @param maxLength 长度,不足的补0,
     * @return 补位后的值
     */
    public static String formatFenToString(String fenAmount, int maxLength) {


        BigDecimal decimal;
        if (TextUtils.isEmpty(fenAmount)) {
            decimal = new BigDecimal("0");
        } else {
            decimal = new BigDecimal(fenAmount);
        }
        decimal = decimal.setScale(0, BigDecimal.ROUND_DOWN);
        return String.format("%0" + maxLength + "d", decimal.longValueExact());
    }

    /**
     * 获取今天之前或者之后几天  获取以前的日期 填写负数,以后的日期用正数
     * <p>返回格式为yyyy-MM-dd</p>
     *
     * @param days 天数
     * @return 日期
     */
    public static String getDayBeforeOrAfterToday(int days) {
        return getDayBeforeOrAfterDay(getCurrentTime("yyyy-MM-dd"), days);
    }

    /**
     * 获取某一天之前或者之后几天  获取以前的日期填写负数,以后的日期用正数
     * <p>返回格式为yyyy-MM-dd</p>
     *
     * @param day  时间,获取的是这个时间的前后多少天
     * @param days 天数
     * @return 日期
     */
    public static String getDayBeforeOrAfterDay(String day, int days) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            Date date = formatter.parse(day);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, days);//把日期往前减少一天，若想把日期向后推一天则将负数改为正数
            date = calendar.getTime();
            return formatter.format(date);
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * 获取某个月的第一天
     *
     * @param year    年份
     * @param month   月份(如果传的值为0,那么日期则是上一年的12月份)
     * @param pattern 返回时间格式(默认返回yyyy-MM-dd HH:mm:ss)
     * @return 时间
     */
    public static String getMinimumDate(int year, int month, String pattern) {

        if (TextUtils.isEmpty(pattern)) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        // 获取前月的第一天
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month - 1);//日历是从0开始的
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        String minTime = format.format(calendar.getTime());
        Log.e(TAG, "min time:" + minTime);
        return minTime;
    }

    /**
     * 获取某个月的最大时间
     *
     * @param year    年份
     * @param month   月份(如果传的值为0,那么日期则是上一年的12月份)
     * @param pattern 返回时间格式(默认返回yyyy-MM-dd HH:mm:ss)
     * @return 时间
     */
    public static String getMaximumDate(int year, int month, String pattern) {

        if (TextUtils.isEmpty(pattern)) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        // 获取前月的第一天
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month - 1);//日历是从0开始的
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String maxTime = format.format(calendar.getTime());
        Log.e(TAG, "max time:" + maxTime);
        return maxTime;
    }


    /**
     * 计算两个日期之间相差天数
     *
     * @param starDate 开始日期(格式必须为 yyyy-MM-dd)
     * @param endDate  结束日期(格式必须为 yyyy-MM-dd)
     * @return 间隔天数
     */
    public static int getDaysBetweenTwoDate(String starDate, String endDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date fromDate = format.parse(starDate);
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(fromDate);

            Date toDate = format.parse(endDate);
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(toDate);

            long startTimeInMillis = startCalendar.getTimeInMillis();
            long endTimeInMillis = endCalendar.getTimeInMillis();
            long between = endTimeInMillis - startTimeInMillis;
            int days = (int) (between / (24 * 60 * 60 * 1000));
            Log.e(TAG, "日期相差: " + days);
            return days;

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getCause());
        }
    }

    /**
     * 计算数之差(decimal1 减 decimal2)
     * <p>参数必须可以转为数字,若报错返回0,结果四舍五入保留两位小数</p>
     *
     * @param decimal1 数字1
     * @param decimal2 数字2
     * @return
     */
    public static String minus(String decimal1, String decimal2) {
        BigDecimal d1 = new BigDecimal(decimal1);
        BigDecimal d2 = new BigDecimal(decimal2);
        BigDecimal min = d1.subtract(d2);
        min = min.setScale(2, BigDecimal.ROUND_HALF_UP);
        return min.toPlainString();
    }


    /**
     * 比较两个日期大小,如果第一个时间比第二个时间大返回1; 如果相等返回0; 如果比第二个时间小返回-1
     * 如果出错 返回-2
     *
     * @param currentTime 时间1
     * @param time        时间2
     */
    public static int compareDate(String currentTime, String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date1 = sdf.parse(currentTime);
            Date date2 = sdf.parse(time);
            if (date1.before(date2)) {
                return -1;
            } else if (date1.after(date2)) {
                return 1;
            } else {
                return 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        }
    }

    /**
     * 判断两个时间是不是同一天
     * 若出错则返回false
     *
     * @param currentTime 时间1
     * @param time        时间2
     * @return true :是   false:否
     */
    public static boolean isTheSameDay(String currentTime, String time) {
        if (getDaysBetweenTwoDate(currentTime, time) == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 返回两个数的乘积,如果传入的值不是数字类型,那会返回第一个参数的值
     * 结果保留两位小数
     *
     * @param firstNumber  数字1
     * @param secondNumber 数字2
     */
    public static String multiply(String firstNumber, String secondNumber) {
        try {
            BigDecimal decimal = new BigDecimal(firstNumber);
            BigDecimal decimal2 = new BigDecimal(secondNumber);

            BigDecimal multiply = decimal.multiply(decimal2);
            multiply = multiply.setScale(2, BigDecimal.ROUND_HALF_UP);
            return multiply.toPlainString();

        } catch (Exception e) {
            e.printStackTrace();
            return firstNumber;
        }

    }


    /**
     * byte数组转十六进制字符串
     *
     * @param b
     * @return
     */
    public static String bytes2HexString(byte[] b) {
        byte[] buff = new byte[2 * b.length];
        for (int i = 0; i < b.length; i++) {
            buff[2 * i] = hex[(b[i] >> 4) & 0x0f];
            buff[2 * i + 1] = hex[b[i] & 0x0f];
        }
        return new String(buff);
    }


    /**
     * 十六进制转换字符串
     *
     * @return String 对应的字符串  str Byte字符串(Byte之间无分隔符 如:[616C6B])
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }


    /**
     * 转hex字符串转字节数组
     *
     * @param inHex
     * @return
     */
    public static byte[] hexToByteArr(String inHex)// hex字符串转字节数组
    {
        int hexlen = inHex.length();
        byte[] result;
        if (isOdd(hexlen) == 1) {// 奇数
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {// 偶数
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = hexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    // Hex字符串转byte
    private static byte hexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }

    private static int isOdd(int num) {
        return num & 0x1;
    }
}
