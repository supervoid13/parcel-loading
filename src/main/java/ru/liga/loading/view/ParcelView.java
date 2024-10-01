package ru.liga.loading.view;

import org.springframework.stereotype.Component;
import ru.liga.loading.models.Parcel;

import java.util.List;

@Component
public class ParcelView {

    /**
     * Метод отображения списка посылок в консоль.
     * @param parcels список посылок.
     */
    public void displayParcels(List<Parcel> parcels) {
        for (Parcel parcel : parcels) {
            displayParcel(parcel);
        }
    }

    /**
     * Метод отображения определённой посылки в консоль.
     * @param parcel посылка.
     */
    public void displayParcel(Parcel parcel) {
        StringBuilder sb = new StringBuilder();
        sb.append("Form:\n");

        for (char[] box : parcel.getBox()) {
            sb.append(box).append("\n");
        }

        System.out.println("Name: " + parcel.getName());
        System.out.println("Symbol: " + parcel.getSymbol());
        System.out.println(sb);
    }
}
