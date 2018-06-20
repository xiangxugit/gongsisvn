/*
 * flyee 2018-05-04
 *
 * DevUtil：控制板工具类
 * 适配通讯规约：《商用机智能控制板通信协议V3.docx》
 *
 * 描述：获取控制板运行状态，向控制板发送指令，判断控制当前通讯状态及数据刷新时间
 *
 *
 * 版本：1.0  最后修改：2018/5/12 flyee
 *
 *
 */

package android.serialport;

import android.os.Handler;

import java.io.File;

public class DevUtil {
    //Const
    public static final int MAXBUF=256;//接收缓存长度
    public static final int MAXERRCOUNT=3;//最大错误数，当达到MAXERRCOUNT时，表示设备离线

    //ErrorCode
    public static final byte ERR_NULL=0;//OK
    public static final byte ERR_TIMEOUT=1;//接收超时，设备无反应
    public static final byte ERR_READ=2;//接收故障
    public static final byte ERR_WRITE=3;//发送故障
    public static final byte ERR_OVERFLOW=4;//接收缓存溢出，最大MAXBUF
    public static final byte ERR_BUFFER=5;//接收报文不正确
    public static final byte ERR_CONTROL=6;//设备执行不正确或拒动
    public static final byte ERR_OTHER=7;//其它故障
    public static final byte ERR_SERIAL=0x0F;//串口故障

    //class member
    private static ComUtil mComUtil=null;
    private Handler mHandler=null;
    private int mErrCount=0;

    //打开通讯
    public boolean openCOM(File dev, int baudrate, int flg){
        closeCom();
        mComUtil = new ComUtil(dev, baudrate, 0, mHandler);
        return mComUtil.openSuccess();
    }
    public boolean isComOpened(){return  (mComUtil != null) && mComUtil.openSuccess();}

    //关闭通讯
    public void closeCom(){
        if (mComUtil != null){
            mComUtil.closeSerialPort();
            mComUtil=null;
        }
    }
    //调试辅助信息
    public String getComErrMsg(){
        if(mComUtil!=null)
            return mComUtil.getErrMsg();
        else
            return "null";
    }
    public String getComTxCode(){
        if(mComUtil!=null)
            return mComUtil.getTxCode();
        else
            return "null";
    }
    public String getComRxCode(){
        if(mComUtil!=null)
            return mComUtil.getRxCode();
        else
            return "null";
    }

    //列表(数组)行号
    private int index_hotWater=-1;
    public int get_index_hotWater(){return index_hotWater;}
    private int index_normalWater=-1;
    public int get_index_normalWater(){return index_normalWater;}
    private int index_coolWater=-1;
    public int get_index_coolWater(){return index_coolWater;}
    private int index_hotEnabled=-1;
    public int get_index_hotEnabled(){return index_hotEnabled;}
    private int index_coolEnabled=-1;
    public int get_index_coolEnabled(){return index_coolEnabled;}
    private int index_turnOnOff=-1;
    public int get_index_turnOnOff(){return index_turnOnOff;}
    private int index_cup=-1;
    public int get_index_cup(){return index_cup;}
    private int index_rinse=-1;
    public int get_index_rinse(){return index_rinse;}



    //运行参数
    private int run_sTDS=20;     //原水TDS值
    private int run_oTDS=10;     //出水TDS值
    private int run_hotTemp=42;  //热水温度
    private int run_coolTemp=42; //冷水温度
    private byte run_bHot=1;     //加热状态，01 未加热，02 加热
    private byte run_bCool=1;    //制冷状态，01 未制冷，02 制冷
    private byte run_bWater=1;   //制水状态，01 未制水，02 制水
    private byte run_bRinse=1;   //冲洗状态，01 未冲洗，02 冲洗
    private byte run_bFault=1;   //源水缺水，01 不缺水，02 缺水
    private byte run_bLeak=1;    //漏水，01 未漏水，02 漏水
    private byte run_bSwitch=1;  //开关机状态，01 ，02 关
    private int run_bCup=1;      //缺杯，01 不缺杯，02 缺杯
    private boolean run_hotWaterSW=false;     //热水出开关
    private boolean run_normalWaterSW=false;  //温水出开关
    private boolean run_coolWaterSW=false;    //冷水出开关
    private int run_waterFlow=0;     //本次总出水计量


    private byte run_bSta=-1;    //通讯状态，0:offline;1:online;-1:Stop
    private String run_upTime = ComUtil.getNowStr();//刷新时间

    //定值参数
    private int pam_rinseInterval=5;     //冲洗间隔，单位分钟
    private int pam_rinseTimeLong=10;    //冲洗时长，单位秒
    private int pam_hotTemp=75;          //加热温度
    private int pam_coolTemp=8;          //制冷温度
    private boolean pam_hotEnabled=true;  //加热使能
    private boolean pam_coolEnabled=true; //制冷使能

    public DevUtil(Handler hnd){mHandler=hnd; }

    //数据描述
    public String  get_run_bSta_title(){return "通讯状态";}
    //数据值
    public int     get_run_bSta_value(){return run_bSta;}
    //数据值描述
    public String  get_run_bSta_valAlias(){
        switch (run_bSta){
            case 1:
                return "在线";
            case 0:
                return "离线";
            default:
                return "停止";
        }
    }
    //设置数据值(下同)
    public void set_run_bSta_value(byte sta){
        run_bSta = sta;
        run_upTime = ComUtil.getNowStr();
    }

    public String  get_run_upTime_title(){return "刷新时间";}
    public String  get_run_upTime_value(){return run_upTime;}

    public String  get_run_sTDS_title(){return "原水TDS值";}
    public int     get_run_sTDS_value(){return run_sTDS;}
    public String  get_run_sTDS_valAlias(){return String.format("%d", run_sTDS);}

    public String  get_run_oTDS_title(){return "出水TDS值";}
    public int     get_run_oTDS_value(){return run_oTDS;}
    public String  get_run_oTDS_valAlias(){return String.format("%d", run_oTDS);}

    public String  get_run_hotTemp_title(){return "热水温度";}
    public int     get_run_hotTemp_value(){return run_hotTemp;}
    public String  get_run_hotTemp_valAlias(){return String.format("%d", run_hotTemp);}

    public String  get_run_coolTemp_title(){return "冷水温度";}
    public int     get_run_coolTemp_value(){return run_coolTemp;}
    public String  get_run_coolTemp_valAlias(){return String.format("%d", run_coolTemp);}

    public String  get_run_bHot_title(){return "加热状态";}
    public int     get_run_bHot_value(){return run_bHot;}
    public String  get_run_bHot_valAlias(){
        switch (run_bHot){
            case 1:
                return "加热停止";
            case 2:
                return "正在加热";
            default:
                return "未知状态";
        }
    }

    public String  get_run_bCool_title(){return "制冷状态";}
    public int     get_run_bCool_value(){return run_bCool;}
    public String  get_run_bCool_valAlias(){
        switch (run_bCool){
            case 1:
                return "制冷停止";
            case 2:
                return "正在制冷";
            default:
                return "未知状态";
        }
    }

    public String  get_run_bWater_title(){return "制水状态";}
    public int     get_run_bWater_value(){return run_bWater;}
    public String  get_run_bWater_valAlias(){
        switch (run_bWater){
            case 1:
                return "制水停止";
            case 2:
                return "正在制水";
            default:
                return "未知状态";
        }
    }

    public String  get_run_bRinse_title(){return "冲洗状态";}
    public int     get_run_bRinse_value(){return run_bRinse;}
    public String  get_run_bRinse_valAlias(){
        switch (run_bRinse){
            case 1:
                return "冲洗停止";
            case 2:
                return "正在冲洗";
            default:
                return "未知状态";
        }
    }

    public String  get_run_bFault_title(){return "原水状态";}
    public int     get_run_bFault_value(){return run_bFault;}
    public String  get_run_bFault_valAlias(){
        switch (run_bFault){
            case 1:
                return "正常(不缺水)";
            case 2:
                return "缺水报警";
            default:
                return "未知状态";
        }
    }

    public String  get_run_bLeak_title(){return "漏水状态";}
    public int     get_run_bLeak_value(){return run_bLeak;}
    public String  get_run_bLeak_valAlias(){
        switch (run_bLeak){
            case 1:
                return "正常(未漏水)";
            case 2:
                return "漏水报警";
            default:
                return "未知状态";
        }
    }

    public String  get_run_bSwitch_title(){return "开关机状态";}
    public int     get_run_bSwitch_value(){return run_bSwitch;}
    public String  get_run_bSwitch_valAlias(){
        switch (run_bSwitch){
            case 1:
                return "开机";
            case 2:
                return "关机";
            default:
                return "未知状态";
        }
    }

    public String  get_run_bCup_title(){return "水杯状态";}
    public int     get_run_bCup_value(){return run_bCup;}
    public String  get_run_bCup_valAlias(){
        switch (run_bCup){
            case 1:
                return "正常(不缺杯)";
            case 2:
                return "缺杯报警";
            default:
                return "未知状态";
        }
    }

    public String  get_run_hotWaterSW_title(){return "热水出水状态";}
    public boolean get_run_hotWaterSW_value(){return run_hotWaterSW;}
    public String  get_run_hotWaterSW_valAlias(){
        if(run_hotWaterSW)
            return "正在出水...";
        else
            return "停止";
    }

    public String  get_run_normalWaterSW_title(){return "温水出水状态";}
    public boolean get_run_normalWaterSW_value(){return run_normalWaterSW;}
    public String  get_run_normalWaterSW_valAlias(){
        if(run_normalWaterSW)
            return "正在出水...";
        else
            return "停止";
    }

    public String  get_run_coolWaterSW_title(){return "冷水出水状态";}
    public boolean get_run_coolWaterSW_value(){return run_coolWaterSW;}
    public String  get_run_coolWaterSW_valAlias(){
        if(run_coolWaterSW)
            return "正在出水...";
        else
            return "停止";
    }

    public String  get_run_waterFlow_title(){return "本次总出水计量";}
    public int     get_run_waterFlow_value(){return run_waterFlow;}
    public String  get_run_waterFlow_valAlias(){return String.format("%d", run_waterFlow);}

    public String  get_pam_rinseInterval_title(){return "冲洗间隔设置";}
    public int     get_pam_rinseInterval_value(){return pam_rinseInterval;}
    public String  get_pam_rinseInterval_valAlias(){return String.format("%d", pam_rinseInterval);}

    public String  get_pam_rinseTimeLong_title(){return "冲洗时长设置";}
    public int     get_pam_rinseTimeLong_value(){return pam_rinseTimeLong;}
    public String  get_pam_rinseTimeLong_valAlias(){return String.format("%d", pam_rinseTimeLong);}

    public String  get_pam_hotTemp_title(){return "加热温度设置";}
    public int     get_pam_hotTemp_value(){return pam_hotTemp;}
    public String  get_pam_hotTemp_valAlias(){return String.format("%d", pam_hotTemp);}

    public String  get_pam_coolTemp_title(){return "制冷温度设置";}
    public int     get_pam_coolTemp_value(){return pam_coolTemp;}
    public String  get_pam_coolTemp_valAlias(){return String.format("%d", pam_coolTemp);}

    public String  get_pam_hotEnabled_title(){return "加热使能设置";}
    public boolean get_pam_hotEnabled_value(){return pam_hotEnabled;}
    public String  get_pam_hotEnabled_valAlias(){
        if(pam_hotEnabled)
            return "允许";
        else
            return "禁止";
    }
    //public void set_pam_hotEnabled(boolean enabled){pam_hotEnabled=enabled;}

    public String  get_pam_coolEnabled_title(){return "制冷使能设置";}
    public boolean get_pam_coolEnabled_value(){return pam_coolEnabled;}
    public String  get_pam_coolEnabled_valAlias(){
        if(pam_coolEnabled)
            return "允许";
        else
        return "禁止";
    }
    //public void set_pam_coolEnabled(boolean enabled){pam_coolEnabled=enabled;}


    //检查通讯状态
    private void checkSta(){
        if(run_bSta != 0) {
            mErrCount += 1;
            if (mErrCount >= MAXERRCOUNT) {
                run_bSta = 0;
                run_upTime = ComUtil.getNowStr();
            }
        }
    }
    //获取通讯报文起始位置
    private byte[] getHead6(byte fun1, byte fun2){
        byte[] head=new byte[]{(byte)0xBB, (byte)0x66,(byte)0x66,(byte)0x80,(byte)fun1,(byte)fun2};
        return head;
    }

    //获取所有运行参数
		//返回 ErrorCode
    public byte get_ioRunData(){
        if(mComUtil==null)
            return ERR_SERIAL;

        byte[]buf=new byte[MAXBUF];
        int len=0;
        buf[0] = (byte) 0xAA;
        buf[1] = (byte) 0x33;
        buf[2] = (byte) 0x33;
        buf[3] = (byte) 0x80;
        buf[4] = (byte) 0xFF;
        buf[5] = (byte) 0x01;
        buf[6] = (byte) 0xFC;
        buf[7] = (byte) 0xFF;
        len = 8;

        len = mComUtil.pollData(buf, len, MAXBUF, false);

        if(len< 0){
            checkSta();
            byte r=0;
            switch (len){
                case -1:
                    r = ERR_WRITE;
                    break;
                case -2:
                    r = ERR_READ;
                    break;
                case -3:
                    r = ERR_OVERFLOW;
                    break;
                default:
                    r = ERR_OTHER;
            }
            return r;
        }
        else if(len==0){
            checkSta();
            return ERR_TIMEOUT;
        }

        byte[] head6=getHead6((byte)0xFF,(byte)0x01);
        int ip = findHead6Byte(buf, len, head6);

        if (!checkData(buf, len) || ip < 0) {
            return ERR_BUFFER;
        }

        mErrCount = 0;
        run_bSta = 1;
        run_upTime = ComUtil.getNowStr();

        ip +=6;
        run_sTDS     = (int)buf[ip]*0x100 + buf[ip+1];
        run_oTDS     = (int)buf[ip+2]*0x100 + buf[ip+3];
        run_hotTemp  = buf[ip+4];
        run_coolTemp = buf[ip+5];
        run_bHot     = buf[ip+6];
        run_bCool    = buf[ip+7];
        run_bWater   = buf[ip+8];
        run_bRinse   = buf[ip+9];
        run_bFault   = buf[ip+10];
        run_bLeak    = buf[ip+11];
        run_bSwitch  = buf[ip+12];
        run_bCup     = buf[ip+13];

        return ERR_NULL;
    }

		/*设置参数
    set_ioParam：设置参数指令
    返回 ErrorCode
		参数
			rIntv：冲洗间隔
			rLong：冲洗时长
			hTemp：加热温度
			cTemp：制冷温度

    */
    public byte set_ioParam(int rIntv, int rLong, int hTemp, int cTemp){
        if(mComUtil==null)
            return ERR_SERIAL;

        pam_rinseInterval = rIntv;
        pam_rinseTimeLong = rLong;
        pam_hotTemp = hTemp;
        pam_coolTemp = cTemp;

        byte[]buf=new byte[MAXBUF];
        int len=0;

        buf[0] = (byte) 0xAA;
        buf[1] = (byte) 0x33;
        buf[2] = (byte) 0x33;
        buf[3] = (byte) 0x80;
        buf[4] = (byte) 0xFF;
        buf[5] = (byte) 0x02;

        buf[6] = (byte) (pam_rinseInterval &0xFF);
        buf[7] = (byte) (pam_rinseTimeLong &0xFF);
        buf[8] = (byte) (pam_hotTemp   &0xFF);
        buf[9] = (byte) (pam_coolTemp  &0xFF);

        buf[10] = (byte) 0x00;
        buf[11] = (byte) 0x00;
        buf[12] = (byte) 0x00;
        buf[13] = (byte) 0x00;
        buf[14] = (byte) 0x00;
        buf[15] = (byte) 0x00;
        buf[16] = (byte) 0x00;
        buf[17] = (byte) 0x00;
        buf[18] = (byte) 0x00;
        buf[19] = (byte) 0x00;

        buf[20] = (byte) 0xFC;
        buf[21] = (byte) 0xFF;
        len = 22;

        len = mComUtil.pollData(buf, len, MAXBUF, true);

        if(len< 0){
            checkSta();
            byte r=0;
            switch (len){
                case -1:
                    r = ERR_WRITE;
                    break;
                case -2:
                    r = ERR_READ;
                    break;
                case -3:
                    r = ERR_OVERFLOW;
                    break;
                default:
                    r = ERR_OTHER;
            }
            return r;
        }
        else if(len==0){
            checkSta();
            return ERR_TIMEOUT;
        }

        if (!checkData(buf, len)) {
            return ERR_BUFFER;
        }

        return ERR_NULL;
    }

    /*
    do_ioWater：出关水指令
    参数：
    water：1-热水；2-温水；3-冷水，255-所有
    sw：1-开；2-关；
    */
    public byte do_ioWater(int water, int sw){
        if(mComUtil==null)
            return ERR_SERIAL;

        byte[]buf=new byte[MAXBUF];
        int len=0;

        buf[0] = (byte) 0xAA;
        buf[1] = (byte) 0x33;
        buf[2] = (byte) 0x33;
        buf[3] = (byte) 0x80;
        buf[4] = (byte) 0xFF;
        buf[5] = (byte) 0x03;

        buf[6] = (byte) (water & 0xFF);
        buf[7] = (byte) (sw & 0xFF);

        buf[8] = (byte) 0xFC;
        buf[9] = (byte) 0xFF;
        len = 10;

        len = mComUtil.pollData(buf, len, MAXBUF, true);

        if(len< 0){
            checkSta();
            byte r=0;
            switch (len){
                case -1:
                    r = ERR_WRITE;
                    break;
                case -2:
                    r = ERR_READ;
                    break;
                case -3:
                    r = ERR_OVERFLOW;
                    break;
                default:
                    r = ERR_OTHER;
            }
            return r;
        }
        else if(len==0){
            checkSta();
            return ERR_TIMEOUT;
        }

        if (!checkData(buf, len)) {
            return ERR_BUFFER;
        }

        if (sw==1){
            run_waterFlow = 0;
            switch (water) {
                case 1:
                    run_hotWaterSW = true;
                    break;
                case 2:
                    run_normalWaterSW = true;
                    break;
                case 3:
                    run_coolWaterSW = true;
                    break;
                case 255:
                    run_hotWaterSW = true;
                    run_normalWaterSW = true;
                    run_coolWaterSW = true;
                    break;
            }
        }
        else if(sw == 02) {
            switch (water) {
                case 1:
                    run_hotWaterSW = false;
                    break;
                case 2:
                    run_normalWaterSW = false;
                    break;
                case 3:
                    run_coolWaterSW = false;
                    break;
                case 255:
                    run_hotWaterSW = false;
                    run_normalWaterSW = false;
                    run_coolWaterSW = false;
                    break;
            }
            byte[] head6 = getHead6((byte) 0xFF, (byte) 0x03);
            int ip = findHead6Byte(buf, len, head6);
            if (ip < 0) {
                //run_waterFlow =0;
                //return ERR_BUFFER;
            }
            else {
                ip += 7;
                run_waterFlow = (int)buf[ip]*0x100 + buf[ip+1];
            }
        }

        return ERR_NULL;
    }
    /*
    do_ioRinse：冲洗指令
    */
    public byte do_ioRinse(){
        if(mComUtil==null)
            return ERR_SERIAL;

        byte[]buf=new byte[MAXBUF];
        int len=0;

        buf[0] = (byte) 0xAA;
        buf[1] = (byte) 0x33;
        buf[2] = (byte) 0x33;
        buf[3] = (byte) 0x80;
        buf[4] = (byte) 0xFF;
        buf[5] = (byte) 0x04;
        buf[6] = (byte) 0xFC;
        buf[7] = (byte) 0xFF;
        len = 8;

        len = mComUtil.pollData(buf, len, MAXBUF,true);

        if(len< 0){
            checkSta();
            byte r=0;
            switch (len){
                case -1:
                    r = ERR_WRITE;
                    break;
                case -2:
                    r = ERR_READ;
                    break;
                case -3:
                    r = ERR_OVERFLOW;
                    break;
                default:
                    r = ERR_OTHER;
            }
            return r;
        }
        else if(len==0){
            checkSta();
            return ERR_TIMEOUT;
        }

        if (!checkData(buf, len)) {
            return ERR_BUFFER;
        }

        return ERR_NULL;
    }

    /*
    set_ioHeatEnabled：加热使能指令
     */
    public byte set_ioHeatEnabled(boolean enabled){
        if(mComUtil==null)
            return ERR_SERIAL;

        byte[]buf=new byte[MAXBUF];
        int len=0;

        buf[0] = (byte) 0xAA;
        buf[1] = (byte) 0x33;
        buf[2] = (byte) 0x33;
        buf[3] = (byte) 0x80;
        buf[4] = (byte) 0xFF;
        buf[5] = (byte) 0x05;
        if(enabled)
            buf[6] = (byte) 0x01;
        else
            buf[6] = (byte) 0x02;
        buf[7] = (byte) 0xFC;
        buf[8] = (byte) 0xFF;
        len = 9;

        len = mComUtil.pollData(buf, len, MAXBUF,true);

        if(len< 0){
            checkSta();
            byte r=0;
            switch (len){
                case -1:
                    r = ERR_WRITE;
                    break;
                case -2:
                    r = ERR_READ;
                    break;
                case -3:
                    r = ERR_OVERFLOW;
                    break;
                default:
                    r = ERR_OTHER;
            }
            return r;
        }
        else if(len==0){
            checkSta();
            return ERR_TIMEOUT;
        }

        if (!checkData(buf, len)) {
            return ERR_BUFFER;
        }
        pam_hotEnabled = enabled;
        return ERR_NULL;
    }

    /*
    set_ioColdEnabled：制冷使能指令
     */
    public byte set_ioColdEnabled(boolean enabled){
        if(mComUtil==null)
            return ERR_SERIAL;

        byte[]buf=new byte[MAXBUF];
        int len=0;

        buf[0] = (byte) 0xAA;
        buf[1] = (byte) 0x33;
        buf[2] = (byte) 0x33;
        buf[3] = (byte) 0x80;
        buf[4] = (byte) 0xFF;
        buf[5] = (byte) 0x06;
        if(enabled)
            buf[6] = (byte) 0x01;
        else
            buf[6] = (byte) 0x02;
        buf[7] = (byte) 0xFC;
        buf[8] = (byte) 0xFF;
        len = 9;

        len = mComUtil.pollData(buf, len, MAXBUF,true);

        if(len< 0){
            checkSta();
            byte r=0;
            switch (len){
                case -1:
                    r = ERR_WRITE;
                    break;
                case -2:
                    r = ERR_READ;
                    break;
                case -3:
                    r = ERR_OVERFLOW;
                    break;
                default:
                    r = ERR_OTHER;
            }
            return r;
        }
        else if(len==0){
            checkSta();
            return ERR_TIMEOUT;
        }

        if (!checkData(buf, len)) {
            return ERR_BUFFER;
        }
        pam_coolEnabled = enabled;
        return ERR_NULL;
    }
    /*
    do_ioSWitch：开关机指令
    参数：isOpen
    true 开
    false 关
     */
    public byte do_ioSWitch(boolean isOpen){
        if(mComUtil==null)
            return ERR_SERIAL;

        byte[]buf=new byte[MAXBUF];
        int len=0;

        buf[0] = (byte) 0xAA;
        buf[1] = (byte) 0x33;
        buf[2] = (byte) 0x33;
        buf[3] = (byte) 0x80;
        buf[4] = (byte) 0xFF;
        buf[5] = (byte) 0x07;
        if(isOpen)
            buf[6] = (byte) 0x01;
        else
            buf[6] = (byte) 0x02;
        buf[7] = (byte) 0xFC;
        buf[8] = (byte) 0xFF;
        len = 9;

        len = mComUtil.pollData(buf, len, MAXBUF, true);

        if(len< 0){
            checkSta();
            byte r=0;
            switch (len){
                case -1:
                    r = ERR_WRITE;
                    break;
                case -2:
                    r = ERR_READ;
                    break;
                case -3:
                    r = ERR_OVERFLOW;
                    break;
                default:
                    r = ERR_OTHER;
            }
            return r;
        }
        else if(len==0){
            checkSta();
            return ERR_TIMEOUT;
        }

        if (!checkData(buf, len)) {
            return ERR_BUFFER;
        }

        return ERR_NULL;
    }

    /*
    do_ioEmptying：排空指令
     */
    public byte do_ioEmptying(){
        if(mComUtil==null)
            return ERR_SERIAL;

        byte[]buf=new byte[MAXBUF];
        int len=0;

        buf[0] = (byte) 0xAA;
        buf[1] = (byte) 0x33;
        buf[2] = (byte) 0x33;
        buf[3] = (byte) 0x80;
        buf[4] = (byte) 0xFF;
        buf[5] = (byte) 0x08;
        buf[6] = (byte) 0xFC;
        buf[7] = (byte) 0xFF;
        len = 8;

        len = mComUtil.pollData(buf, len, MAXBUF,true);

        if(len< 0){
            checkSta();
            byte r=0;
            switch (len){
                case -1:
                    r = ERR_WRITE;
                    break;
                case -2:
                    r = ERR_READ;
                    break;
                case -3:
                    r = ERR_OVERFLOW;
                    break;
                default:
                    r = ERR_OTHER;
            }
            return r;
        }
        else if(len==0){
            checkSta();
            return ERR_TIMEOUT;
        }

        if (!checkData(buf, len)) {
            return ERR_BUFFER;
        }

        return ERR_NULL;
    }

    /*
    do_ioCup：出杯指令
     */
    public byte do_ioCup(){
        if(mComUtil==null)
            return ERR_SERIAL;

        byte[]buf=new byte[MAXBUF];
        int len=0;

        buf[0] = (byte) 0xAA;
        buf[1] = (byte) 0x33;
        buf[2] = (byte) 0x33;
        buf[3] = (byte) 0x80;
        buf[4] = (byte) 0xFF;
        buf[5] = (byte) 0x09;
        buf[6] = (byte) 0xFC;
        buf[7] = (byte) 0xFF;
        len = 8;

        len = mComUtil.pollData(buf, len, MAXBUF,true);

        if(len< 0){
            checkSta();
            byte r=0;
            switch (len){
                case -1:
                    r = ERR_WRITE;
                    break;
                case -2:
                    r = ERR_READ;
                    break;
                case -3:
                    r = ERR_OVERFLOW;
                    break;
                default:
                    r = ERR_OTHER;
            }
            return r;
        }
        else if(len==0){
            checkSta();
            return ERR_TIMEOUT;
        }

        if (!checkData(buf, len)) {
            return ERR_BUFFER;
        }
        byte[] head6 = getHead6((byte) 0xFF, (byte) 0x09);
        int ip = findHead6Byte(buf, len, head6);
        if (ip < 0) {
            return ERR_BUFFER;
        }
        ip+=6;
        if(buf[ip]==1)
            return ERR_NULL;
        else
            return ERR_CONTROL;
    }

    /*
   do_ioCover：开盖指令
    */
    public byte do_ioCover(){
        if(mComUtil==null)
            return ERR_SERIAL;

        byte[]buf=new byte[MAXBUF];
        int len=0;

        buf[0] = (byte) 0xAA;
        buf[1] = (byte) 0x33;
        buf[2] = (byte) 0x33;
        buf[3] = (byte) 0x80;
        buf[4] = (byte) 0xFF;
        buf[5] = (byte) 0x10;
        buf[6] = (byte) 0xFC;
        buf[7] = (byte) 0xFF;
        len = 8;

        len = mComUtil.pollData(buf, len, MAXBUF,true);

        if(len< 0){
            checkSta();
            byte r=0;
            switch (len){
                case -1:
                    r = ERR_WRITE;
                    break;
                case -2:
                    r = ERR_READ;
                    break;
                case -3:
                    r = ERR_OVERFLOW;
                    break;
                default:
                    r = ERR_OTHER;
            }
            return r;
        }
        else if(len==0){
            checkSta();
            return ERR_TIMEOUT;
        }

        if (!checkData(buf, len)) {
            return ERR_BUFFER;
        }

        return ERR_NULL;
    }

    //二维数组导出，调试用
    public String[][] toArray() {
        String[][] data = new String[24][2];

        data[0][0] = get_run_bSta_title();
        data[0][1] = get_run_bSta_valAlias();

        data[1][0] = get_run_upTime_title();
        data[1][1] = get_run_upTime_value();

        data[2][0] = get_run_sTDS_title();
        data[2][1] = get_run_sTDS_valAlias();

        data[3][0] = get_run_oTDS_title();
        data[3][1] = get_run_oTDS_valAlias();

        data[4][0] = get_run_hotTemp_title();
        data[4][1] = get_run_hotTemp_valAlias();

        data[5][0] = get_run_coolTemp_title();
        data[5][1] = get_run_coolTemp_valAlias();

        data[6][0] = get_run_bHot_title();
        data[6][1] = get_run_bHot_valAlias();

        data[7][0] = get_run_bCool_title();
        data[7][1] = get_run_bCool_valAlias();

        data[8][0] = get_run_bWater_title();
        data[8][1] = get_run_bWater_valAlias();

        data[9][0] = get_run_bRinse_title();
        data[9][1] = get_run_bRinse_valAlias();
        index_rinse = 9;

        data[10][0] = get_run_bFault_title();
        data[10][1] = get_run_bFault_valAlias();

        data[11][0] = get_run_bLeak_title();
        data[11][1] = get_run_bLeak_valAlias();

        data[12][0] = get_run_bSwitch_title();
        data[12][1] = get_run_bSwitch_valAlias();
        index_turnOnOff=12;

        data[13][0] = get_run_bCup_title();
        data[13][1] = get_run_bCup_valAlias();
        index_cup=13;

        data[14][0] = get_run_hotWaterSW_title();
        data[14][1] = get_run_hotWaterSW_valAlias();
        index_hotWater=14;

        data[15][0] = get_run_normalWaterSW_title();
        data[15][1] = get_run_normalWaterSW_valAlias();
        index_normalWater=15;

        data[16][0] = get_run_coolWaterSW_title();
        data[16][1] = get_run_coolWaterSW_valAlias();
        index_coolWater=16;

        data[17][0] = get_run_waterFlow_title();
        data[17][1] = get_run_waterFlow_valAlias();

        data[18][0] = get_pam_rinseInterval_title();
        data[18][1] = get_pam_rinseInterval_valAlias();

        data[19][0] = get_pam_rinseTimeLong_title();
        data[19][1] = get_pam_rinseTimeLong_valAlias();

        data[20][0] = get_pam_hotTemp_title();
        data[20][1] = get_pam_hotTemp_valAlias();

        data[21][0] = get_pam_coolTemp_title();
        data[21][1] = get_pam_coolTemp_valAlias();

        data[22][0] = get_pam_hotEnabled_title();
        data[22][1] = get_pam_hotEnabled_valAlias();
        index_hotEnabled=22;

        data[23][0] = get_pam_coolEnabled_title();
        data[23][1] = get_pam_coolEnabled_valAlias();
        index_coolEnabled=23;

        return data;
    }

    //定位报文起始位置
    public static int findHead6Byte(byte[] data, int count, byte[] head6){
        if (head6.length != 6)
            return -1;

        int ip=0;
        while(ip < count-6){
            if(
                    data[ip]==head6[0] &&
                            data[ip+1]==head6[1] &&
                            data[ip+2]==head6[2] &&
                            data[ip+3]==head6[3] &&
                            data[ip+4]==head6[4] &&
                            data[ip+5]==head6[5]
                    )
                return ip;
            else
                ip += 1;
        }
        return -1;
    }
    //判断相应报文是否有效
    public static boolean checkData(byte[] data, int count){

        if (count < 8)
            return false;
        //BB 66 66 80 ...FC FF
        return  data[0]==(byte)0xBB &&
                data[1]==(byte)0x66 &&
                data[2]==(byte)0x66 &&
                data[3]==(byte)0x80 &&
                data[count-2]==(byte)0xFC &&
                data[count-1]==(byte)0xFF;
    }

}
