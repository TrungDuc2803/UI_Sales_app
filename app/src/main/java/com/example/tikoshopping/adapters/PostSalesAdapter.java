package com.example.tikoshopping.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tikoshopping.API._Constant;
import com.example.tikoshopping.ProductDetailsActivity;
import com.example.tikoshopping.R;
import com.example.tikoshopping.models.PostSales;

import java.util.List;

public class PostSalesAdapter extends RecyclerView.Adapter<PostSalesAdapter.ViewHolder> {
    private List<PostSales> mData = null;
    private Context context;
    String url ="https://www.simplilearn.com/ice9/free_resources_article_thumb/what_is_image_Processing.jpg";
    public PostSalesAdapter(List<PostSales> data,Context context) {
        mData = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.popular_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PostSales item = mData.get(position);
        holder.name.setText(item.getTitle());
        holder.pop_des.setText(item.getDescription());
        holder.discount.setText("Discount " + item.getDiscount()+ "% Off");
//        holder.price.setText(item.getPrice().toString());
        Glide.with(context).load(_Constant.baseUrl+item.getImages().get(0).getPath()).into(holder.popImg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);

                intent.putExtra("idPost",item.getIDPost());
                intent.putExtra("Name",item.getTitle());
                intent.putExtra("Price",item.getPrice());
                intent.putExtra("Description",item.getDescription());
                intent.putExtra("Path",item.getImages().get(0).getPath());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView popImg;
        TextView name,pop_des,discount,price;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.pop_prod);
            pop_des = itemView.findViewById(R.id.pop_des);
            popImg = itemView.findViewById(R.id.pop_img);
            discount = itemView.findViewById(R.id.pop_discount);
        }
    }
}
