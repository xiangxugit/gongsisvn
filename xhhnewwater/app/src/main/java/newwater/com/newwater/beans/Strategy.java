package newwater.com.newwater.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/27 0027.
 */

public class Strategy {
    private String VideoplayTime;
    private ArrayList<String> VideoList;

    public ArrayList<String> getVideoList() {
        return VideoList;
    }

    public void setVideoList(ArrayList<String> videoList) {
        VideoList = videoList;
    }

    public String getVideoplayTime() {
        return VideoplayTime;
    }

    public void setVideoplayTime(String videoplayTime) {
        VideoplayTime = videoplayTime;
    }
}
