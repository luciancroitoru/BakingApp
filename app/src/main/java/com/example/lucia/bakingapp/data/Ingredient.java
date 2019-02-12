package com.example.lucia.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

//Ingredient object that contains ingredient related to a single Recipe item
public class Ingredient implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel parcel) {
            return new Ingredient(parcel);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    /**
     * {@link Ingredient} Attributes
     * Each attribute has a corresponding @SerializedName that is needed for GSON
     * to map the JSON keys with the attributes of {@link Ingredient} object.
     */

    @SerializedName("quantity")
    private Double mIngredientQuantity;

    @SerializedName("measure")
    private String mIngredientMeasure;

    @SerializedName("ingredient")
    private String mIngredient;


    /** Empty Constructor */
    public Ingredient() {}


    /**
     * Getter and Setter methods for class Ingredient
     */

    public Double getIngredientQuantity() {
        return mIngredientQuantity;
    }

    public void setIngredientQuantity(Double quantity) {
        mIngredientQuantity = quantity;
    }

    public String getIngredientMeasure() {
        return mIngredientMeasure;
    }

    public void setIngredientMeasure(String measure) {
        mIngredientMeasure = measure;
    }

    public String getIngredient() {
        return mIngredient;
    }

    public void setIngredient(String ingredient) {
        mIngredient = ingredient;
    }


    /**
     * Default Constructor - Constructs a new {@link Ingredient} object
     * Scope for this constructor is private so CREATOR can access it
     * @param parcel
     */
    public Ingredient(Parcel parcel) {
        mIngredientQuantity = parcel.readByte() == 0x00 ? null : parcel.readDouble();
        mIngredientMeasure = parcel.readByte() == 0x00 ? null : parcel.readString();
        mIngredient = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mIngredientQuantity == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(mIngredientQuantity);
        }

        if (mIngredientMeasure == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeString(mIngredientMeasure);
        }

        dest.writeString(mIngredient);
    }

}