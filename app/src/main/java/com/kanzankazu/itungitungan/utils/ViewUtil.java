package com.kanzankazu.itungitungan.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.Px;

import com.kanzankazu.itungitungan.MyApplication;
import com.kanzankazu.itungitungan.R;

import java.util.ArrayList;
import java.util.List;

public class ViewUtil {
    public static LinearLayout linearLayoutView(Activity activity, @Nullable LinearLayout.LayoutParams params, @Px int paddingPixel, int orientation, int gravity) {
        LinearLayout linearLayout = new LinearLayout(activity);
        LinearLayout.LayoutParams paramChildLL;
        if (params == null) {
            paramChildLL = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        } else {
            paramChildLL = params;
        }
        linearLayout.setLayoutParams(paramChildLL);
        linearLayout.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);
        linearLayout.setOrientation(orientation);
        linearLayout.setGravity(gravity);
        return linearLayout;
    }

    public static LinearLayout checkBox(Activity activity, int gravity, int orientation, List<ViewModel> viewModels) {
        LinearLayout row = new LinearLayout(activity);
        row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        row.setGravity(gravity);
        row.setOrientation(orientation);

        for (int i = 0; i < viewModels.size(); i++) {
            ViewModel model = viewModels.get(i);
            CheckBox checkBox = new CheckBox(activity);
            checkBox.setId(i);
            checkBox.setTag(model.getTag());
            checkBox.setText(model.getTitle());
            row.addView(checkBox);
        }

        return row;
    }

    public static RadioGroup radioButton(Context activity, List<ViewModel> viewModels) {
        RadioGroup rg = new RadioGroup(activity); //create the RadioGroup
        rg.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
        for (int i = 0; i < viewModels.size(); i++) {
            ViewModel model = viewModels.get(i);

            RadioButton radioButton = new RadioButton(activity);
            radioButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            radioButton.setId(i);
            radioButton.setTag(model.getTag());
            radioButton.setText(model.getTitle());
            rg.addView(radioButton);
        }

        return rg;
    }

    public static EditText editText(Context activity) {
        EditText editText = new EditText(activity);
        editText.setTag("jawaban");
        editText.setHint("Masukan ");
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        return editText;
    }

    public static void spinner(Activity activity, List<ViewModel> viewModels) {
        Spinner spinner = new Spinner(activity);

        //Spinner
        List<String> strings = new ArrayList<String>();
        strings.add("Silahkan pilih");
        for (int i = 0; i < viewModels.size(); i++) {
            ViewModel model = viewModels.get(i);
            strings.add(model.getTitle());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, strings) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
                //return
            }

            @Override
            public View getDropDownView(int position, @android.support.annotation.Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    changeColText(R.color.colorAccent, tv);
                } else {
                    changeColText(R.color.colorPrimary, tv);
                }
                return view;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public static ImageView imageView(Context activity){
        ImageView imageView = new ImageView(activity);
        imageView.setImageResource(R.drawable.ic_launcher_foreground);
        return imageView;
    }

    private static void changeColText(int androidRed, TextView textView) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            textView.setTextColor(MyApplication.getContext().getResources().getColor(androidRed));
        } else {
            textView.setTextColor(ContextCompat.getColor(MyApplication.getContext(), androidRed));
        }
    }

    public static TextView textView(Context activity) {
        TextView textView = new TextView(activity);
        return textView;

    }

    public static class ViewModel implements Parcelable {
        public static final Creator<ViewModel> CREATOR = new Creator<ViewModel>() {
            @Override
            public ViewModel createFromParcel(Parcel in) {
                return new ViewModel(in);
            }

            @Override
            public ViewModel[] newArray(int size) {
                return new ViewModel[size];
            }
        };
        private int id;
        private int subId;
        private String Title;
        private String subTitle;
        private String Value;
        private String SubValue;
        private String Tag;
        private String SubTag;

        public ViewModel() {
        }

        public ViewModel(int id, String title) {
            this.id = id;
            Title = title;
        }

        public ViewModel(int id, String title, String value) {
            this.id = id;
            Title = title;
            Value = value;
        }

        public ViewModel(int id, int subId, String title, String subTitle, String value, String subValue, String tag, String subTag) {
            this.id = id;
            this.subId = subId;
            Title = title;
            this.subTitle = subTitle;
            Value = value;
            SubValue = subValue;
            Tag = tag;
            SubTag = subTag;
        }

        protected ViewModel(Parcel in) {
            id = in.readInt();
            subId = in.readInt();
            Title = in.readString();
            subTitle = in.readString();
            Value = in.readString();
            SubValue = in.readString();
            Tag = in.readString();
            SubTag = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeInt(subId);
            dest.writeString(Title);
            dest.writeString(subTitle);
            dest.writeString(Value);
            dest.writeString(SubValue);
            dest.writeString(Tag);
            dest.writeString(SubTag);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSubId() {
            return subId;
        }

        public void setSubId(int subId) {
            this.subId = subId;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getValue() {
            return Value;
        }

        public void setValue(String value) {
            Value = value;
        }

        public String getSubValue() {
            return SubValue;
        }

        public void setSubValue(String subValue) {
            SubValue = subValue;
        }

        public String getTag() {
            return Tag;
        }

        public void setTag(String tag) {
            Tag = tag;
        }

        public String getSubTag() {
            return SubTag;
        }

        public void setSubTag(String subTag) {
            SubTag = subTag;
        }
    }
}
