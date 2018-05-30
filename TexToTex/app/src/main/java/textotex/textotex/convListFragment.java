package textotex.textotex;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class convListFragment extends ListFragment {


    public convListFragment() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final String[][] convListTmp = {{ "grosminet", "user3" }, {"Content of message 1.", "fonfweon cewoifwe feo"}, {"25-12-2015", "35-13-4124"}};

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.conv_list_row, R.id.label, convListTmp);
        convListAdapter adapter = new convListAdapter(getContext(), convListTmp);

        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

    }
}
