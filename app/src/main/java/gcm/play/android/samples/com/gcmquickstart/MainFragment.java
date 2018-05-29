package gcm.play.android.samples.com.gcmquickstart;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollLayoutManager;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

import fcm.android.play.google.quickstart.FirebaseInstanceIDBaseService;
import gcm.play.android.samples.com.gcmquickstart.adapter.ItemDemoAdapter;
import gcm.play.android.samples.com.gcmquickstart.demo_object.DemoItem;
import gcm.play.android.samples.com.gcmquickstart.demo_object.forecast.Forecast;
import gcm.play.android.samples.com.gcmquickstart.demo_object.shop.ShopItem;
import gcm.play.android.samples.com.gcmquickstart.demo_object.shop.ShopStation;
import gcm.play.android.samples.com.gcmquickstart.demo_object.forecast.WeatherStation;
import gcm.play.android.samples.com.gcmquickstart.notification.MyDefinitionReceiveMessage;

public class MainFragment extends Fragment {
    private final static String TAG = "MainFragment";
    private final ArrayList<DemoItem> mDatas = new ArrayList<>();
    private DiscreteScrollView mPicker;
    //    private ShopAdapter mAdapter;
    private ItemDemoAdapter mAdapter;
    private Button mLauncherFcmSender;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View child = inflater.inflate(R.layout.fragment_main, container, false);
        final List<ShopItem> shopdata = ShopStation.get().getData();
        final List<Forecast> forecastdatas = WeatherStation.get().getForecasts();
        for (ShopItem item : shopdata) {
            mDatas.add(item);
        }
        for (Forecast item : forecastdatas) {
            mDatas.add(item);
        }
        mPicker = (DiscreteScrollView) child.findViewById(R.id.item_picker);
        mPicker.setOrientation(DSVOrientation.HORIZONTAL);
//        mAdapter = new ShopAdapter(shopdata);
        mAdapter = new ItemDemoAdapter(mDatas, ShopItem.class);
        final InfiniteScrollAdapter adapter = InfiniteScrollAdapter.wrap(mAdapter);
        mPicker.setAdapter(adapter);
        mAdapter.filter(ShopItem.class);
        mPicker.addOnItemChangedListener(new DiscreteScrollView.OnItemChangedListener() {
            @Override
            public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
                int positionInDataSet = adapter.getRealPosition(adapterPosition);
                Log.d(TAG, "onCurrentItemChanged Show positionInDataSet : " + positionInDataSet);
            }
        });
        mPicker.setSlideOnFling(true);
        mPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());
        mLauncherFcmSender = (Button) child.findViewById(R.id.launcher_fcm_sender);
        mLauncherFcmSender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick : " + (String) v.getTag());
                Intent intent = v.getContext().getPackageManager().getLaunchIntentForPackage("fcm.sender.v1.demo");
                if (intent != null) {
                    intent.putExtra("FCM_Recive_Client", (String) v.getTag());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    intent = v.getContext().getPackageManager().getLaunchIntentForPackage("fcm.sender.v1.demo.debug");
                    if (intent != null) {
                        intent.putExtra("FCM_Recive_Client", (String) v.getTag());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        // Bring user to the market or let them choose an app?
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setData(Uri.parse("market://details?id=" + "com.package.name"));
                        startActivity(intent);
                    }
                }
            }
        });
        child.findViewById(R.id.filter_shop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.filter(ShopItem.class);
            }
        });
        child.findViewById(R.id.filter_forecast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.filter(Forecast.class);
            }
        });
        return child;
    }

    @Override
    public void onResume() {
        super.onResume();
        final Bundle arguments = getArguments();
        if (arguments != null) {
            if (!TextUtils.isEmpty(arguments.getString(FirebaseInstanceIDBaseService.FIREBASE_TOKEN))) {
                mLauncherFcmSender.setVisibility(View.VISIBLE);
                mLauncherFcmSender.setTag(arguments.getString(FirebaseInstanceIDBaseService.FIREBASE_TOKEN));
            }
            final MyDefinitionReceiveMessage pushmessage = arguments.getParcelable(C.PUSH_NOTIFICATION_PAYLOAD);
            if (pushmessage != null) {
                arguments.putParcelable(C.PUSH_NOTIFICATION_PAYLOAD, null);
                final int msgrawtype = pushmessage.getMsgRawType();
                final long device_id = pushmessage.getDeviceId();
                //FIXME here ask account to know device and then  get device ask DeviceAdapter to know position
                switch (msgrawtype) {
                    case MyDefinitionReceiveMessage.MESSAGE_SHOP:
                        mAdapter.filter(ShopItem.class);
                        break;
                    case MyDefinitionReceiveMessage.MESSAGE_FORECAST:
                        mAdapter.filter(Forecast.class);
                        break;
                }
                final DemoItem item = mAdapter.getItemByDeviceId(device_id);//FIXME here is ask account
                final int position = mAdapter.getItemIndex(item); //FIXME here get position @  DeviceAdapter
//                // FIXME
                final DiscreteScrollLayoutManager dlm = (DiscreteScrollLayoutManager) mPicker.getLayoutManager();
                dlm.scrollToPosition(position);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
