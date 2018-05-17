package newwater.com.newwater.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import com.alibaba.fastjson.JSONArray;
import com.danikula.videocache.HttpProxyCacheServer;

import java.util.ArrayList;
import java.util.List;

import newwater.com.newwater.App;
import newwater.com.newwater.MainActivity;
import newwater.com.newwater.view.CustomerVideoView;

/**
 * Created by Administrator on 2018/4/27 0027.
 */

public class VideoAdapter extends PagerAdapter {
    private JSONArray videList;
    private List<CustomerVideoView> mList = new ArrayList<CustomerVideoView>();
    private Context context;
    public VideoAdapter(Context context,JSONArray  videoList) {
        this.videList = videoList;
        this.context = context;
    }


    @Override
    public int getCount() {
        return videList.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        CustomerVideoView videoView = new CustomerVideoView(context);
        Uri data = Uri.parse(videList.getString(position));
        HttpProxyCacheServer proxy = App.getProxy(context);
        String proxyUrl = proxy.getProxyUrl(videList.getString(position));
        videoView.setVideoPath(proxyUrl);
        videoView.start();
        container.addView(videoView);

//        ImageView imageView = new ImageView(context);
//        imageView.setImageResource(imageIds[position]);
//        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        container.addView(imageView);
        mList.add(videoView);
        return videoView;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mList.get(position));
    }
}
