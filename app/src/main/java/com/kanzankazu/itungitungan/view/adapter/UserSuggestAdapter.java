package com.kanzankazu.itungitungan.view.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.kanzankazu.itungitungan.R;
import com.kanzankazu.itungitungan.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserSuggestAdapter extends ArrayAdapter<User> {
    Context context;
    int resource;
    int textViewResourceId;
    List<User> items;
    List<User> tempItems;
    List<User> suggestions;
    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((User) resultValue).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (User people : tempItems) {
                    if (people.getName().toLowerCase().contains(constraint.toString().toLowerCase()) || people.getEmail().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<User> filterList = (ArrayList<User>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (User people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };

    public UserSuggestAdapter(Context context, int resource, int textViewResourceId, List<User> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<>(items); // this makes the difference.
        suggestions = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_user_suggest_adapter, parent, false);
        }

        User people = items.get(position);
        if (people != null) {
            TextView lblName = view.findViewById(R.id.lbl_name);
            TextView lblEmail = view.findViewById(R.id.lbl_email);
            lblName.setText(people.getName());
            lblEmail.setText(people.getEmail());
        }

        return view;
    }

    @Override
    public User getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }
}
