package ouyang.com.serialtest;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.BaseInputConnection;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ouyang.com.serialtest.databinding.ActivityMainBinding;
import ouyang.com.serialtest.serial.SerialPortManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityMainBinding binding;
    private String TAG = getClass().getSimpleName();
    private SerialPortManager manager;
    private ThreadPoolExecutor executor;
    private final int CORE_POOL_SIZE = 10;
    private final int MAX_POOL_SIZE = 20;
    private final long ALIVE_TIME = 15;//空闲线程存活时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 19) {
            hideNav();
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        String CPU_ABI = android.os.Build.CPU_ABI;
        Log.e(TAG, "设备ABI: " + CPU_ABI);
        setListener();
        executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, ALIVE_TIME, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(10), new ThreadPoolExecutor.CallerRunsPolicy());
//        manager = new SerialPortManager();
//        boolean success = manager.openSerialPort("/dev/ttyS0", 9600);

//        Log.e(TAG, "打开结果: " + success);
    }

    private void hideNav() {
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void setListener() {
        binding.btnTurnOn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_turn_on:
                test();
                break;
        }
    }

    private void test() {
//        initRecommendPrices(0);
//        SerialPortManager manager = new SerialPortManager();
//        boolean serialPort = manager.openSerialPort("/dev/ttyS1", 9600);
//        System.out.println("打开串口: " + serialPort);
//        manager.closeSerialPort();
//        System.out.println("关闭串口: ");
//"/system/bin/"
//        String command = "input keyevent 4";
//        File file = new File("/system/bin/su");
//        chmod777(file);
//        getEtherGateWay(command);

        BaseInputConnection connection = new BaseInputConnection(binding.layoutContent, true);
        KeyEvent kv = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_POWER);
        KeyEvent ku = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_POWER);
        connection.sendKeyEvent(kv);
        connection.sendKeyEvent(ku);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Instrumentation inst = new Instrumentation();
//                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_VOLUME_UP);
//
//            }
//        }).start();

    }

    /**
     * 文件设置最高权限 777 可读 可写 可执行
     *
     * @param file 文件
     * @return 权限修改是否成功
     */
    boolean chmod777(File file) {
        if (null == file || !file.exists()) {
            // 文件不存在
            return false;
        }
        try {
            // 获取ROOT权限
            Process su = Runtime.getRuntime().exec("/system/bin/su");
            // 修改文件属性为 [可读 可写 可执行]
            String cmd = "chmod 777 " + file.getAbsolutePath() + "\n" + "exit\n";
            su.getOutputStream().write(cmd.getBytes());
            if (0 == su.waitFor() && file.canRead() && file.canWrite() && file.canExecute()) {
                return true;
            }
        } catch (IOException | InterruptedException e) {
            // 没有ROOT权限
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取以太网网关,子线程获取,通过handler回调结果
     */
    private void getEtherGateWay(final String command) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String result = "";
                try {
                    Runtime runtime = Runtime.getRuntime();
//            Process process = runtime.exec("/system/bin/ping -c 4 -w 5 " + ipAddress);
                    Process process = runtime.exec(command, null, null);

                    InputStream inputStream = process.getInputStream();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();
                    inputStream.close();

                    result = sb.toString();
                    Log.e(TAG, "获取结果: " + result);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private List<Integer> initRecommendPrices(double price) {
        int[] arr;
        if (price < 1)
            arr = new int[]{1, 5, 10, 20, 50, 100};//人民币面额
        else
            arr = new int[]{5, 10, 20, 50, 100};//人民币面额

        List<Integer> list = new ArrayList<>();
        for (int anArr1 : arr) {
            list.add(anArr1);//初始化list
        }
        for (int i = 0; i < list.size(); i++) {
            int p = list.get(list.size() - 4);
            if (p < price) {//遍历list 如果倒数第四位的数值比传入的价格还小,那么就拿最有一个数值,与面额再加一遍,即加了5个数,在重复遍历
                int last = list.get(list.size() - 1);
                while (list.size() - 4 < i) {
                    int add;
                    for (int anArr : arr) {
                        add = last + anArr;
                        list.add(add);
                    }
                }
            }
        }

        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {//把集合中数值小于价格的都去除,以便在显示界面的时候只需要按顺序取就行
            int p = iterator.next();
            if (p < price)
                iterator.remove();
        }

        list.add(0, (int) Math.ceil(price));//再集合中添加最小的向上取整的小数,放在第一位

        Log.e(TAG, "集合: " + Arrays.toString(list.toArray()));
        return list;
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
            hideNav();
    }
}
