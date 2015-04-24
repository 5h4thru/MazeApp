package dxp141030.utdallas.edu.mazeapp.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;

import dxp141030.utdallas.edu.mazeapp.R;
import dxp141030.utdallas.edu.mazeapp.models.Score;

/**
 * @author Durga Sai Preetham Palagummi
 * @NetID dxp141030
 * @Course: CS6301 UI Design - Spring 2015
 * Date 17th April 2015
 * Modified on 22nd April 2015
 */


/**
 * BaseAdapter class for listing the high scores
 */
@SuppressLint("InflateParams")
public class CustomAdapter extends BaseAdapter {

    // Variables the help store the scores
    private ArrayList<Score> scoresList;
    private LayoutInflater inflater;
    private TimerUtil timerUtil;
    
    // Constructor
    public CustomAdapter(Activity activity, ArrayList<Score> scoresList) {
        this.scoresList = scoresList;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        timerUtil = new TimerUtil();
    }
    
    @Override
    public int getCount() {
        return scoresList.size();
    }

    @Override
    public Object getItem(int position) {
        return scoresList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // ViewHolder Class
    public static class ViewHolder {
        public TextView textViewName, textViewScore;
    }
    
    // getView of each listItem
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        
        if (convertView == null) {
            view = inflater.inflate(R.layout.list_item, null); //Inflate Item with Custom layout
            holder = new ViewHolder();
            //Initialize Views
            holder.textViewName = (TextView) view
                    .findViewById(R.id.textViewName);
            holder.textViewScore = (TextView) view.findViewById(R.id.textViewScore);
            //Set Tag to holder for each ListItem view
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        
        // Set values to Views
        holder.textViewName.setText(scoresList.get(position).getName());
        holder.textViewScore.setText(timerUtil.formatTime(scoresList.get(position).getScoreTime()));
        
        return view;
    }
}
