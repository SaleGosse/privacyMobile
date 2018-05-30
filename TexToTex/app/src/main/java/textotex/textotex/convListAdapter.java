package textotex.textotex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class convListAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private LayoutInflater mInflater;
    private String[][] mDatas = new String[3][];

    public convListAdapter(Context context, String[][] datas) {
        super(context, R.layout.conv_list_row, datas[0]);

        this.mContext = context;

        this.mDatas[0] = datas[0];
        this.mDatas[1] = datas[1];
        this.mDatas[2] = datas[2];

        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = mInflater.inflate(R.layout.conv_list_row, parent, false);
        TextView mItemUsername = (TextView)rowView.findViewById(R.id.row_username);
//        TextView mItemLastTxt = (TextView)rowView.findViewById(R.id.row_message);
//        TextView mItemDate = (TextView)rowView.findViewById(R.id.row_date);
//        ImageView mItemIcon = (ImageView)rowView.findViewById(R.id.row_icon);

        mItemUsername.setText(mDatas[0][position]);
//        mItemLastTxt.setText(mDatas[1][position]);
//        mItemDate.setText(mDatas[2][position]);


        //TODO: do something with the first letter of the username to display the right conv icon
        //if(mDatas[0][position][0] == 'a')


        return rowView;
    }


}
