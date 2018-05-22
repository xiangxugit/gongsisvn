/*
* Created by Flyee on 2018/5/1
*
* ComUtil：通讯工具类
*描述：初始化串口，发送报文及接收报文
* 构造函数中，参数 File dev,dev=new File(filePath)，dev必须读写权限（dev.canRead() && dev.canWrite()）
* 串口对应文件路径/dev/ttySx,可能必须root权限
*
* 主要函数：
* pollData：应答式通讯，负责发送报文及接收报文
*
*
* 版本：1.0  最后修改：2018/5/4 flyee
*
*/

package android.serialport;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

//import android.os.CountDownTimer;


public class ComUtil {

    public static final int TIMEOUT = 2000;//报文等待响应最大时间ms
    public static final int SMREAMIDLE = 32;//数据流空闲间隔ms

    private SerialPort mSerialPort;
    public Boolean openSuccess(){return mSerialPort != null;}
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    //关闭
    public void closeSerialPort() {
        if (mSerialPort != null) {
            try{
                mSerialPort.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            mSerialPort = null;
            mInputStream = null;
            mOutputStream =null;
        }
    }

    //调试用
    public static final int MESSAGE_TX= 1;
    public static final int MESSAGE_RX= 2;
    public static final int MESSAGE_ER= 3;

    private Handler hMain=null;
    public void setMainHandler(Handler hnd){hMain=hnd;}
    private String erMsg="";
    public String getErrMsg(){return erMsg;}
    private String txCode="";
    public String getTxCode(){return txCode;}
    private String rxCode="";
    public String getRxCode(){return rxCode;}

    //构造
    public ComUtil(File device, int baudrate, int flags, Handler hnd) throws NullPointerException {
        hMain = hnd;

        try {
            if (accessPermission(device))
                mSerialPort = new SerialPort(device, baudrate, flags);

        } catch (IOException e) {
            e.printStackTrace();
            sendMessage(MESSAGE_ER,e.toString());
        } catch (SecurityException e) {
            e.printStackTrace();
            sendMessage(MESSAGE_ER,e.toString());
        }

        if (mSerialPort != null) {
            mInputStream = mSerialPort.getInputStream();
            mOutputStream = mSerialPort.getOutputStream();
        } else {
            sendMessage(MESSAGE_ER,(new NullPointerException("Open Serial port Failed")).toString());
            //throw new NullPointerException("Open Serial port Failed");

        }
    }

    //发送及接收
    public /*synchronized*/ int  pollData(byte[] data, int count, int bufSize) throws NullPointerException {

        if (mOutputStream == null) {
            //throw new NullPointerException("outputStream is null");
            sendMessage(MESSAGE_TX, "serial port can't write");
            return -1;//Write Exception
        }
        if (mInputStream == null) {
            //throw new NullPointerException("inputStream is null");
            sendMessage(MESSAGE_RX, "serial port can't read");
            return -2; //Read Exception
        }
        //Send Data
        try {
            mOutputStream.write(data, 0, count);
            sendMessage(MESSAGE_TX, buff2String(data, count));
            delay(10);
        } catch (IOException e) {
            //e.printStackTrace();
            sendMessage(MESSAGE_TX, "write stream error");
            return -1;// Write Exception
        }

        // receive data
        try {
            byte[] buf = new byte[bufSize];
            int rLen = 0;
            int len = 0;

            long loopTick = SystemClock.uptimeMillis();
            long startTick = SystemClock.uptimeMillis();
            long nowTick;
            do {
                try {
                    if (mInputStream.available() > 0) {
                        len = mInputStream.read(buf);
                        if (len > 0) {
                            if (len + rLen > bufSize) {
                                sendMessage(MESSAGE_RX, "stream buffer Overflow");
                                return -3; // Buffer Overflow
                            }

                            for (int i = 0; i < len; i++)
                                data[rLen + i] = buf[i];
                            rLen += len;
                            loopTick = SystemClock.uptimeMillis();
                        }
                    }

                } catch (IOException e) {
                    //e.printStackTrace();
                    //loop
                }
                nowTick = SystemClock.uptimeMillis();
                delay(15);

            } while (nowTick - startTick < TIMEOUT && !(rLen > 0 && nowTick - loopTick > SMREAMIDLE));
            if(rLen > 0) {
                sendMessage(MESSAGE_RX, buff2String(data, rLen));
            }
            else {
                sendMessage(MESSAGE_RX, "reply timeout");
            }

            return rLen;

        } catch (Exception e) {
            //e.printStackTrace();
            sendMessage(MESSAGE_RX, "read stream error");
            return -2; //Read Exception
        }
    }

    //用于调试显示报文
    private void sendMessage(int msg, String s){
        if(hMain==null)
            return;

        Message message;
        switch (msg){
            case MESSAGE_TX:
                txCode = "Tx: " + s;
                message = Message.obtain();
                message.arg1 = MESSAGE_TX;
                hMain.sendMessage(message);
                break;

            case MESSAGE_RX:
                rxCode = "Rx: " + s;
                message = Message.obtain();
                message.arg1 = MESSAGE_RX;
                hMain.sendMessage(message);
                break;
            case MESSAGE_ER:
                erMsg = "Error: " + s;
                message = Message.obtain();
                message.arg1 = MESSAGE_ER;
                hMain.sendMessage(message);
                break;
        }
    }

    public static String buff2String(byte[] data, int count) {
        String s = "";
        for (int i = 0; i < count; i++)
            s = s + String.format("%02X", data[i]) + " ";
        return s;
    }

    public static void delay(int ms) {
        try {
            Thread.currentThread();
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String getNowStr() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return fmt.format(new Date(System.currentTimeMillis()));
    }

    public static  String getCodeHead(){
        SimpleDateFormat fmt = new SimpleDateFormat("[mm:ss]->");
        return String.format("%s", fmt.format((new Date()).getTime()));
    }

    public static boolean accessPermission(File device){
        if (!device.canRead() || !device.canWrite()) {

            try {
                Process su;
                su = Runtime.getRuntime().exec("su");
                String cmd = "chmod 666 " + device.getAbsolutePath() + "\n" + "exit\n";
                su.getOutputStream().write(cmd.getBytes());
                if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite()) {
                    //throw new SecurityException();
                    return false;
                }
                return true;

            } catch (Exception e) {
                e.printStackTrace();
                //throw new SecurityException();
                return false;
            }
        }
        return true;
    }
}
