package ru.liga.loading.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.liga.loading.utils.LoadingUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "parcels", schema = "loading")
@Data
@NoArgsConstructor
public class Parcel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "symbol", nullable = false, unique = true)
    private char symbol;

    @Column(name = "box", nullable = false, unique = true)
    private char[][] box;

    public Parcel(String name, char symbol, char[][] box) {
        this.name = name;
        this.symbol = symbol;
        this.box = box;
    }

    public Parcel(char[][] box) {
        this.box = box;
    }

    /**
     * Получение значения поля {@link Parcel#box}
     * @return копия тела посылки
     */
    public char[][] getBox() {
        return LoadingUtils.getArrayCopy(box);
    }

    /**
     * Получение ширины нижнего слоя посылки
     * @return ширина нижнего слоя посылки
     */
    public int calculateBottomWidth() {
        int height = box.length;

        return box[height - 1].length;
    }

    /**
     * Получение площади посылки
     * @return общая площадь посылки
     */
    public int calculateSquare() {
        int result = 0;

        for (char[] charArr : box) {
            result += charArr.length;
        }
        return result;
    }

    /**
     * Получение высоты.
     * @return высоту посылки.
     */
    public int calculateHeight() {
        return box.length;
    }

    /**
     * Получение максимальной ширины.
     * @return ширину посылки.
     */
    public int calculateMaxWidth() {
        int temp = 0;
        for (char[] layer : box) {
            int width = layer.length;
            if (width > temp)
                temp = width;
        }
        return temp;
    }

    /**
     * Метод конвертации посылки в удобно-читаемую форму.
     * @return строку, представляющую посылку.
     */
    public String convertToPrettyOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(name).append("\n");
        sb.append("Symbol: ").append(symbol).append("\n");

        sb.append("Form:\n");

        for (char[] box : box) {
            sb.append(box).append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parcel parcel = (Parcel) o;
        return Objects.deepEquals(box, parcel.box);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(box);
    }
}
