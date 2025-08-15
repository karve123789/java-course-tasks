package com.example.app;


import com.example.geometry.shapes.Circle;
import com.example.geometry.shapes.Rectangle;
import com.example.geometry.shapes.Triangle;
import com.example.geometry.shapes3d.Cube;
import com.example.geometry.shapes3d.Sphere;
import com.example.geometry.utils.MeasurementConverter;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- Расчеты для геометрических фигур ---");

        Circle circle = new Circle(10.0);
        System.out.println("\nКруг с радиусом 10:");
        System.out.println("Площадь: " + circle.getArea());
        System.out.println("Периметр (длина окружности): " + circle.getPerimeter());

        Rectangle rectangle = new Rectangle(5.0, 8.0);
        System.out.println("\nПрямоугольник 5x8:");
        System.out.println("Площадь: " + rectangle.getArea());
        System.out.println("Периметр: " + rectangle.getPerimeter());

        Triangle triangle = new Triangle(3.0, 4.0, 5.0);
        System.out.println("\nТреугольник со сторонами 3, 4, 5:");
        System.out.println("Площадь: " + triangle.getArea());
        System.out.println("Периметр: " + triangle.getPerimeter());
        System.out.println("----------------------------после обновления версии");
        Circle circle1 = new Circle(10.0);
        System.out.println("\nКруг с радиусом 10:");
        System.out.println("Площадь: " + circle.getArea());
        System.out.println("Периметр (длина окружности): " + circle.getPerimeter());
        System.out.println("Диаметр: " + circle.getDiameter());
        System.out.println("----------------------------тестированиее утилит");
        System.out.println("\n--- Тестирование утилит ---");
        double inches = 10.0;
        double cm = MeasurementConverter.inchesToCentimeters(inches);

        System.out.println(inches + " дюймов = " + cm + " сантиметров.");
        System.out.println("----------------------------3D тела");
        System.out.println("\n--- Расчеты для 3D фигур ---");
        Cube cube = new Cube(5);
        System.out.println("Куб со стороной 5:");
        System.out.println("Объем: " + cube.getVolume());
        System.out.println("Площадь поверхности: " + cube.getSurfaceArea());

        Sphere sphere = new Sphere(3);
        System.out.println("\nСфера с радиусом 3:");
        System.out.println("Объем: " + sphere.getVolume());
        System.out.println("Площадь поверхности: " + sphere.getSurfaceArea());
    }
}