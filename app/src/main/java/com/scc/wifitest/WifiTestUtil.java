package com.scc.wifitest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WifiTestUtil {
    public static boolean isNetWorkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()) {  //判断是否有网络
//                showMessage(activeNetworkInfo.getTypeName());
                switch (activeNetworkInfo.getType()) {
                    case ConnectivityManager.TYPE_MOBILE://mobile
                        NetworkInfo nwiMobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                        if (nwiMobile != null && nwiMobile.isAvailable()) {
                            showMessage("mobile");
                            return ping();
                        }
                        break;
                    case ConnectivityManager.TYPE_WIFI://wifi
                        NetworkInfo nwiWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                        if (nwiWifi != null && nwiWifi.isAvailable()) {
                            showMessage("wifi");
                            return ping();
                        }
                        break;
                }

            }
        }
        showMessage("network is not available");
        return false;
    }

    public static void showMessage(String msg) {
        Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_LONG).show();
    }

    /* @author suncat
     * @category 判断是否有外网连接（普通方法不能判断外网的网络是否连接，比如连接上局域网）
     * @return
     */
    public static final boolean ping() {
        String result = null;
        try {
            String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
            Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);// ping网址3次
            // 读取ping的内容，可以不加
            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer stringBuffer = new StringBuffer();
            String content = "";
            while ((content = in.readLine()) != null) {
                stringBuffer.append(content);
            }
            Log.d("------ping-----", "result content : " + stringBuffer.toString());
            // ping的状态
            int status = p.waitFor();
            if (status == 0) {
                result = "success";
                return true;
            } else {
                result = "failed";
            }
        } catch (IOException e) {
            result = "IOException";
        } catch (InterruptedException e) {
            result = "InterruptedException";
        } finally {
            Log.d("----result---", "result = " + result);
        }
        return false;
    }

}
