package mc.group.seven.mcprojectlightshift;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/**
 * Adapter for grid.
 * Created by Matthew 2017-11-01
 */
public class CustomGridAdapter extends BaseAdapter {

    private Context context;
    private String[] items;
    LayoutInflater inflater;

    public CustomGridAdapter(Context context, String[] items){
        this.context = context;
        this.items = items;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.cell,null);
        }
        TextView tv = (TextView) view.findViewById(R.id.tv);
        tv.setText(items[i]);
        return view;
    }
}

