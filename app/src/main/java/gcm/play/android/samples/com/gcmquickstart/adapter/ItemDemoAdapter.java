package gcm.play.android.samples.com.gcmquickstart.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import gcm.play.android.samples.com.gcmquickstart.R;
import gcm.play.android.samples.com.gcmquickstart.demo_object.DemoItem;
import gcm.play.android.samples.com.gcmquickstart.demo_object.forecast.Forecast;
import gcm.play.android.samples.com.gcmquickstart.demo_object.shop.ShopItem;

/**
 * Created by Neo on 2018/5/29.
 */

public class ItemDemoAdapter extends RecyclerView.Adapter<ItemDemoAdapter.ViewHolder> {
    private RecyclerView parentRecycler;
    private final ArrayList<DemoItem> mFiltered = new ArrayList<>();
    private ArrayList<DemoItem> mDatas;
    @NonNull
    private Class<? extends DemoItem> mFilterClass;

    public ItemDemoAdapter(ArrayList<DemoItem> datas, @NonNull Class<? extends DemoItem> filter) {
        mDatas = datas;
        mFilterClass = filter;
    }

    public void filter(@NonNull Class<? extends DemoItem> filter) {
        mFilterClass = filter;
        mFiltered.clear();
        for (DemoItem item : mDatas) {
            if (mFilterClass.isAssignableFrom(item.getClass())) {
                mFiltered.add(item);
            }
        }
        notifyDataSetChanged();
    }

    public DemoItem getItemByDeviceId(long device_id) {
        for (DemoItem item : mFiltered) {
            if (item.getDevice_id() == device_id) {
                return item;
            }
        }
        return null;
    }

    public int getItemIndex(DemoItem item) {
        return mFiltered.indexOf(item);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        parentRecycler = recyclerView;
    }

    @Override
    public int getItemCount() {
        return mFiltered.size();
    }

    @Override
    public int getItemViewType(final int position) {
        if (mFilterClass.equals(ShopItem.class)) {
            return R.layout.item_shop_card;
        } else {
            return R.layout.item_city_card;
        }
    }

    @NonNull
    @Override
    public ItemDemoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        switch (viewType) {
            case R.layout.item_shop_card:
                return new ShopViewHolder(v);
            case R.layout.item_city_card:
                return new ForecastViewHolder(v);
        }
        throw new IllegalArgumentException("no such view type...");
    }

    @Override
    public void onBindViewHolder(@NonNull ItemDemoAdapter.ViewHolder holder, int position) {
        holder.onBind(position);
    }


    public abstract class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void onBind(final int position);
    }

    public class ShopViewHolder extends ViewHolder {
        private ImageView image;

        public ShopViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
        }

        @Override
        public void onBind(int position) {
            final ShopItem shop = (ShopItem) mFiltered.get(position);
            Glide.with(image.getContext())
                    .load(shop.getImage())
                    .into(image);
        }
    }

    public class ForecastViewHolder extends ViewHolder {
        private ImageView imageView;
        private TextView textView;

        public ForecastViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.city_image);
            textView = (TextView) itemView.findViewById(R.id.city_name);
        }

        @Override
        public void onBind(int position) {
            final Context context = imageView.getContext();
            Forecast forecast = (Forecast) mFiltered.get(position);
            int iconTint = ContextCompat.getColor(context, R.color.grayIconTint);
            Glide.with(context)
                    .load(forecast.getImage())
                    .listener(new TintOnLoad(imageView, iconTint))
                    .into(imageView);
        }

        private class TintOnLoad implements RequestListener<Integer, GlideDrawable> {

            private ImageView imageView;
            private int tintColor;

            public TintOnLoad(ImageView view, int tintColor) {
                this.imageView = view;
                this.tintColor = tintColor;
            }

            @Override
            public boolean onException(Exception e, Integer model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, Integer model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                imageView.setColorFilter(tintColor);
                return false;
            }
        }
    }
}
