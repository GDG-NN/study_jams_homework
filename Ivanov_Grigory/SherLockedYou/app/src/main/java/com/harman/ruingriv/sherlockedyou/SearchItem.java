package com.harman.ruingriv.sherlockedyou;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchItem implements Parcelable{
    private String mSpinnerItem;
    private String mConditionsList;
    private int mPosition;

    SearchItem (String spinnerItem, String conditionsList, int position){
        this.mSpinnerItem = spinnerItem;
        this.mConditionsList = conditionsList;
        this.mPosition = position;
    }

    public void setSpinnerItem (String spinnerItem){
        mSpinnerItem = spinnerItem;
    }

    public void setConditionsList(String conditionsList){
        mConditionsList = conditionsList;
    }

    public void setPosition(int position){
        mPosition = position;
    }

    public String getSpinnerItem (){
        return mSpinnerItem;
    }

    public String getConditionsList(){
        return mConditionsList;
    }

    public int getPosition (){
        return mPosition;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mSpinnerItem);
        dest.writeString(mConditionsList);
        dest.writeInt(mPosition);
    }

    public static final Creator<SearchItem> CREATOR = new Creator<SearchItem>() {
        @Override
        public SearchItem createFromParcel(Parcel in) {
            return new SearchItem(in);
        }

        @Override
        public SearchItem[] newArray(int size) {
            return new SearchItem[size];
        }
    };

    private SearchItem(Parcel in) {
        mSpinnerItem = in.readString();
        mConditionsList = in.readString();
        mPosition = in.readInt();
    }
}
