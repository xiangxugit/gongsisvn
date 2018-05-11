package newwater.com.newwater.utils;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import newwater.com.newwater.App;
import newwater.com.newwater.beans.DeiviceParams;

/**
 * Created by Administrator on 2018/5/10 0010.
 */

public class UploadLocalData {
    public  void upload(String url,String localdata){
        RequestParams params = new RequestParams(url);
        params.addParameter("postdata",localdata);
        x.http().request(HttpMethod.PUT, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //TODO 删除本地数据库存储的文件
                List<DeiviceParams> list = null;
                try {
                    list = App.db.findAll(DeiviceParams.class);
//                    App.db.delete(list.get(0));
                    App.db.delete(list);
                } catch (DbException e) {
                    e.printStackTrace();
                }

                LogUtil.i("---删除数据");
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                // TODO 不做处理

            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });
    }
}
