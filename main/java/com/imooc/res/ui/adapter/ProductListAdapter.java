package com.imooc.res.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.imooc.res.R;
import com.imooc.res.config.Config;
import com.imooc.res.ui.activity.ProductDetailActivity;
import com.imooc.res.ui.vo.ProductItem;
import com.imooc.res.utils.T;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by zhanghongyang01 on 16/10/18.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.OrderItemViewHolder> {


    private Context mContext;
    private LayoutInflater mInflater;
    private List<ProductItem> mDatas;

    public ProductListAdapter(Context context, List<ProductItem> datas) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
    }

    @Override
    public OrderItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_product_list, parent, false);
        return new OrderItemViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onBindViewHolder(OrderItemViewHolder holder, int position) {

        ProductItem product = mDatas.get(position);

        Picasso.with(mContext)
                .load(Config.baseUrl + product.getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(holder.mIvImage);

        holder.mTvName.setText(product.getName());
        holder.mTvLabel.setText(product.getLabel());
        holder.mTvPrice.setText(product.getPrice() + "元/份");
        holder.mTvCount.setText("" + product.getCount());
    }

    public interface OnProductListener {

        void onProductAdd(ProductItem productItem);

        void onProductSub(ProductItem productItem);
    }

    private OnProductListener mOnProductListener;

    public void setOnProductListener(OnProductListener listener) {
        mOnProductListener = listener;
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView mIvImage;
        public TextView mTvName;
        public TextView mTvLabel;
        public TextView mTvPrice;
        public ImageView mIvAdd;
        public ImageView mIvSub;
        public TextView mTvCount;


        public OrderItemViewHolder(View itemView) {
            super(itemView);

            mIvImage = (ImageView) itemView.findViewById(R.id.id_iv_image);
            mTvName = (TextView) itemView.findViewById(R.id.id_tv_name);
            mTvLabel = (TextView) itemView.findViewById(R.id.id_tv_label);
            mTvPrice = (TextView) itemView.findViewById(R.id.id_tv_price);

            mIvAdd = (ImageView) itemView.findViewById(R.id.id_iv_add);
            mIvSub = (ImageView) itemView.findViewById(R.id.id_iv_sub);
            mTvCount = (TextView) itemView.findViewById(R.id.id_tv_count);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductDetailActivity.launch(mContext, mDatas.get(getAdapterPosition()));
                }
            });

            mIvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    // 修改数据集
                    ProductItem productItem = mDatas.get(position);
                    productItem.setCount(productItem.getCount() + 1);
                    // 修改UI
                    mTvCount.setText("" + productItem.getCount());
                    // 回调
                    if (mOnProductListener != null) {
                        mOnProductListener.onProductAdd(productItem);
                    }
                }
            });

            mIvSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    // 修改数据集
                    ProductItem productItem = mDatas.get(position);

                    if (productItem.getCount() == 0) {
                        T.showToast("已经是0了，你想干嘛~~~");
                        return;
                    }

                    productItem.setCount(productItem.getCount() - 1);
                    // 修改UI
                    mTvCount.setText("" + productItem.getCount());
                    // 回调
                    if (mOnProductListener != null) {
                        mOnProductListener.onProductSub(productItem);
                    }
                }
            });
        }


    }

}
