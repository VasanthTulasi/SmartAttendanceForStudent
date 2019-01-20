package com.smart.student.attendance.vasanth.smartattendanceforstudent;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdapterClassForStudents extends ArrayAdapter<CardClass> {

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    public static int positionValue;
    static String subjectName = "";

    private static class ViewHolder {
        TextView numberForMember;
        TextView nameForMember;
    }

    public AdapterClassForStudents(Context context, int resource, ArrayList<CardClass> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {



        //sets up the image loader library

        //get the persons information
        final int numberForMember= getItem(position).getIndexNumber();

        String nameForMember = getItem(position).getNameOfPerson();

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;



        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.numberForMember= (TextView) convertView.findViewById(R.id.numberForMember);
            holder.nameForMember = (TextView) convertView.findViewById(R.id.nameForMember);
            result = convertView;

            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        final CardView studentCard = (CardView) convertView.findViewById(R.id.studentCard);

        studentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"Student card clicked",Toast.LENGTH_SHORT).show();
                studentCard.setCardBackgroundColor(Color.parseColor("#81C784"));
            }
        });






        positionValue = position;
        holder.numberForMember.setText(String.valueOf(position+1));
        holder.nameForMember.setText(nameForMember);



        return convertView;

    }

}
