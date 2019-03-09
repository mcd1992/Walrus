/*
 * Copyright 2018 Daniel Underhay & Matthew Daley.
 *
 * This file is part of Walrus.
 *
 * Walrus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Walrus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Walrus.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.bugfuzz.android.projectwalrus.card;

import android.location.Location;

import com.bugfuzz.android.projectwalrus.card.carddata.CardData;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.parceler.Parcel;

import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

@DatabaseTable()
@Parcel
@SuppressWarnings("WeakerAccess")
public class Card {
    public static final String NAME_FIELD_NAME = "name";

    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField(columnName = NAME_FIELD_NAME)
    public String name;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    public CardData cardData;

    @DatabaseField
    public Date cardCreated = new Date();
    @DatabaseField
    public Date cardDataAcquired;

    @DatabaseField
    public String notes = "";

    @DatabaseField
    public Double cardLocationLat;
    @DatabaseField
    public Double cardLocationLng;

    public Card() {
        // Use a timestamp for the default name
        Date curtime = Calendar.getInstance().getTime();
        SimpleDateFormat ftime = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");
        name = ftime.format(curtime);
    }

    public Card(String name, CardData cardData, Date cardCreated, Date cardDataAcquired,
            String notes, Double cardLocationLat, Double cardLocationLng) {
        this.name = name;
        this.cardData = cardData;
        this.cardCreated = cardCreated;
        this.cardDataAcquired = cardDataAcquired;
        this.notes = notes;
        this.cardLocationLat = cardLocationLat;
        this.cardLocationLng = cardLocationLng;
    }

    public static Card copyOf(Card other) {
        try {
            return new Card(
                    other.name,
                    other.cardData != null ? (CardData) other.cardData.clone() : null,
                    other.cardCreated != null ? new Date(other.cardCreated.getTime()) : null,
                    other.cardDataAcquired != null
                            ? new Date(other.cardDataAcquired.getTime()) : null,
                    other.notes,
                    other.cardLocationLat,
                    other.cardLocationLng);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public void setCardData(CardData cardData, Location location) {
        this.cardData = cardData;

        cardDataAcquired = new Date();

        if (location != null) {
            cardLocationLat = location.getLatitude();
            cardLocationLng = location.getLongitude();
        } else {
            cardLocationLat = cardLocationLng = null;
        }
    }
}
