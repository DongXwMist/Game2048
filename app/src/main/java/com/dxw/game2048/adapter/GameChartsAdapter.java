package com.dxw.game2048.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dxw.game2048.R;
import com.dxw.game2048.entity.GameScore;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class GameChartsAdapter extends RecyclerView.Adapter<GameChartsAdapter.ChartsViewHolder> {

    private Context mCx;
    private List<GameScore> scores = new ArrayList<GameScore>();

    public GameChartsAdapter(Context mCx, List<GameScore> scores) {
        this.mCx = mCx;
        this.scores = scores;
        //刷新数据
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChartsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCx).inflate(R.layout.item_ry_charts, parent, false);
        return new ChartsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChartsViewHolder holder, int position) {

        int i=position+1;
        holder.itemNum.setText("第" + i + "名");
        System.out.println("position："+position);
        holder.itemName.setText(scores.get(position).getUsername());
        holder.itemScore.setText(scores.get(position).getGameScore() + "");
    }

    @Override
    public int getItemCount() {
        return scores == null ? 0 : scores.size();
    }

    static class ChartsViewHolder extends RecyclerView.ViewHolder {

        public TextView itemNum, itemName, itemScore;

        public ChartsViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNum = (TextView) itemView.findViewById(R.id.chartNum);
            itemName = (TextView) itemView.findViewById(R.id.chartName);
            itemScore = (TextView) itemView.findViewById(R.id.chartNameScore);
        }
    }

}
