package ouyang.com.serialtest;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import ouyang.com.serialtest.databinding.ActivityMainBinding;
import ouyang.com.serialtest.serial.SerialPortManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityMainBinding binding;
    private String TAG = getClass().getSimpleName();
    private SerialPortManager manager;

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
        SerialPortManager manager = new SerialPortManager();
        boolean serialPort = manager.openSerialPort("/dev/ttyS1", 9600);
        System.out.println("打开串口: " + serialPort);
        manager.closeSerialPort();
        System.out.println("关闭串口: ");
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
