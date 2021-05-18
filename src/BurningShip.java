import java.awt.geom.Rectangle2D;


//Класс наследуется от FractalGenerator. Используется для вычисления фрактала Burning Ship.
public class BurningShip extends FractalGenerator {

    //Константа с максимальным кол-вом итераций.
    public static final int MAX_ITERATIONS = 2000;

    //Область рисования фрактала
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -2.5;
        range.width = 4;
        range.height = 4;
    }


    //Метод реализует итерационную функцию для фрактала Tricorn
    public int numIterations(double x, double y)
    {
        int iteration = 0;
        double zreal = 0;
        double zimaginary = 0;


        while (iteration < MAX_ITERATIONS &&
                zreal * zreal + zimaginary * zimaginary < 4)
        {
            double zrealUpdated = zreal * zreal - zimaginary * zimaginary + x;
            double zimaginaryUpdated = 2 * Math.abs(zreal) * Math.abs(zimaginary) + y;
            zreal = zrealUpdated;
            zimaginary = zimaginaryUpdated;
            iteration += 1;
        }

        //Если кол-во итераций дошло до максимального, возвращаем -1,
        //чтобы показать, что точка не выходит за границы.
        if (iteration == MAX_ITERATIONS)
        {
            return -1;
        }

        return iteration;
    }

    // Возвращает название фрактала
    public String toString() {
        return "Burning Ship";
    }
}